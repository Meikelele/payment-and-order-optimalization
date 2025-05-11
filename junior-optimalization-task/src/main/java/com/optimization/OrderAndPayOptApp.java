package com.optimization;

import com.optimization.model.Order;
import com.optimization.model.PaymentMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import com.optimization.model.Allocation;

public class OrderAndPayOptApp
{
    public static void main( String[] args ) {
        if (args.length < 2) {
            System.out.println("Please give two params: java -jar app.jar <orders.json> <paymentmethods.json>");
            System.exit(1);
        }

        String ordersFilePath = args[0];
        String paymentMethodsFilePath = args[1];

        ObjectMapper mapper = new ObjectMapper();

        List<PaymentMethod> paymentMethodsList = null;
        List<Order> ordersList = null;
        try {
            ordersList = mapper.readValue(
                    new File(ordersFilePath),
                    new TypeReference<List<Order>>() {
                    }
            );
            paymentMethodsList = mapper.readValue(
                    new File(paymentMethodsFilePath),
                    new TypeReference<List<PaymentMethod>>() {
                    }
            );
        } catch (IOException e) {
            System.err.println("Error reading or parsing input files:");
            System.exit(2);
        }

        Map<String, PaymentMethod> paymentMethodsById = paymentMethodsList.stream()
                .collect(Collectors.toMap(PaymentMethod::getId, pm -> pm));

        // POINT method
        PaymentMethod pointsMethod = paymentMethodsById.get("PUNKTY");
        double remainingPoints = pointsMethod.getLimit();

        // CARDS method
        Map<String, Double> remainingCardsLimit = paymentMethodsList.stream()
                        .filter(pm -> !"PUNKTY".equals(pm.getId()))
                        .collect(Collectors.toMap(PaymentMethod::getId, pm -> pm.getLimit()));

        // usage follow spend amount
        Map<String, Double> usage = new HashMap<>();
        usage.put("PUNKTY", 0.0);
        for (String card : remainingCardsLimit.keySet()) {
            usage.put(card, 0.0);
        }


        // getting the best payment scenario
        for (Order order : ordersList) {
            Allocation availableAllocation = optimizeOrder(order, pointsMethod, remainingPoints, remainingCardsLimit, paymentMethodsById);
            if (availableAllocation == null) {
                continue;
            }

            // decreasing limits
            remainingPoints = remainingPoints - availableAllocation.getPointsUsage();
            usage.put("PUNKTY", usage.get("PUNKTY") + availableAllocation.getPointsUsage());

            // null if payment by points, otherwise there is cardId
            if (availableAllocation.getCardId() != null) {
                double newLimit = remainingCardsLimit.get(availableAllocation.getCardId()) - availableAllocation.getCardUsage();
                remainingCardsLimit.put(availableAllocation.getCardId(), newLimit);
                usage.put(availableAllocation.getCardId(), usage.get(availableAllocation.getCardId()) + availableAllocation.getCardUsage());
            }
        }

        // printing to console
        usage.forEach((methodId, spent) -> System.out.printf("%s %.2f%n", methodId, spent));
    }

    /**
     *
     * @param order - the order that need to be pay
     * @param pointsMethod - "PUNKTY" object
     * @param remainingPoints - remaining point that can be used in the next order
     * @param remainingCardLimit - map with card limits
     * @return good allocation or null if order cannot be pay
     */
    private static Allocation optimizeOrder(Order order, PaymentMethod pointsMethod, double remainingPoints, Map<String, Double> remainingCardLimit, Map<String, PaymentMethod> paymentMethodMapById) {
        double orderValue = order.getValue();

        // firstly considering payment full by points
        double pointsUsageForFullPayment = orderValue * (1 - pointsMethod.getDiscount() / 100d );
        double pointsDiscountFullPayment = orderValue * ( pointsMethod.getDiscount() / 100d );
        boolean isFullPaymentByPointsPossible = remainingPoints >= pointsUsageForFullPayment;

        // secondly considering payment partial by points and card
        double pointsUsageForPartialPayment = orderValue * 0.10;
        double pointsDiscountForPartialPayment = pointsUsageForPartialPayment;
        double remainingPriceToPayAfterPartialPointsDiscount = orderValue - pointsUsageForPartialPayment;

        boolean isPartialPaymentByPointsPossible = !isFullPaymentByPointsPossible &&
                        remainingPoints >= pointsUsageForPartialPayment &&
                        remainingCardLimit.values()
                                .stream()
                                .anyMatch(cardLimitation -> cardLimitation >= remainingPriceToPayAfterPartialPointsDiscount);
        String cardUsedForPartialPayment = null;
        if (isPartialPaymentByPointsPossible) {
            for (String cardId : remainingCardLimit.keySet()) {
                if (remainingCardLimit.get(cardId) >= remainingPriceToPayAfterPartialPointsDiscount) {
                    cardUsedForPartialPayment = cardId;
                    break;
                }
            }
        }

        // thirdly considering full payment by card
        String bestCardId = null;
        double bestCardUsage = 0.0;
        double bestCardDiscount = 0.0;
        // looking for card that get the best promotion
        if (order.getPromotions() != null) {
            for (String cardId : order.getPromotions()) {
                double limit = remainingCardLimit.getOrDefault(cardId, 0.0);
                if (limit >= orderValue) {
                    double cardDiscount = orderValue * paymentMethodMapById.get(cardId).getDiscount() / 100d;

                    if (cardDiscount > bestCardDiscount) {
                        bestCardId = cardId;
                        bestCardDiscount = cardDiscount;
                        bestCardUsage = orderValue - cardDiscount;
                    }
                }
            }
        }

        // considering any card that have enough amount of money to cover the orderValue
        if (bestCardId == null) {
            for (String cardId : remainingCardLimit.keySet()) {
                double cardLimit = remainingCardLimit.get(cardId);

                if (cardLimit >= orderValue) {
                    bestCardId = cardId;
                    bestCardDiscount = 0.0;
                    bestCardUsage = orderValue;
                    break;
                }
            }
        }
        boolean isFullPaymentByCardPossible = bestCardId != null;

        // gathering the available options
        List<Allocation> availablePaymentOptions = new ArrayList<Allocation>();
        if (isFullPaymentByPointsPossible) {
            availablePaymentOptions.add(new Allocation("PUNKTY", pointsUsageForFullPayment, 0.0, pointsDiscountFullPayment));
        }
        if (isPartialPaymentByPointsPossible) {
            availablePaymentOptions.add(new Allocation("PUNKTY", cardUsedForPartialPayment, pointsUsageForPartialPayment, remainingPriceToPayAfterPartialPointsDiscount, pointsDiscountForPartialPayment));
        }
        if (isFullPaymentByCardPossible) {
            availablePaymentOptions.add(new Allocation(bestCardId, 0.0, bestCardUsage, bestCardDiscount));
        }


        // comparing and choosing the best option
        // prioritize the loyal points (less cardUsage)
        return availablePaymentOptions
                .stream()
                .max(Comparator
                        .comparingDouble((Allocation a) -> a.getDiscount())
                        .thenComparingDouble(a -> -a.getCardUsage())
                ).orElse(null);
    }
}













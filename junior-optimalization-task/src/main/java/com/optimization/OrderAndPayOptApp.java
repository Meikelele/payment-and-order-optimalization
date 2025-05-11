package com.optimization;

import com.optimization.model.Order;
import com.optimization.model.PaymentMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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








        System.out.println("Prison Hello world!");
    }

    /**
     *
     * @param order - the order that need to be pay
     * @param pointsMethod - "PUNKTY" object
     * @param remainingPoints - remaining point that can be used in the next order
     * @param remainingCardLimit - map with card limits
     * @return good allocation or null if order cannot be pay
     */
    private static Allocation optimizeOrder(Order order, PaymentMethod pointsMethod, double remainingPoints, Map<String, Double> remainingCardLimit) {
        double orderValue = order.getValue();

        // firstly considering payment full by points
        double pointsUsageForFullPayment = orderValue * (1 - pointsMethod.getDiscount() / 100 );
        double pointsDiscountFullPayment = orderValue * ( pointsMethod.getDiscount() / 100 );
        boolean isFullPaymentByPointsPossible = remainingPoints >= pointsUsageForFullPayment;

        // secondly considering payment partial by points and card
        double pointsUsageForPartialPayment = orderValue * 0.10;
        double pointsDiscountForPartialPayment = pointsUsageForPartialPayment;
        double remainingPriceToPayAfterPartialPointsDiscount = orderValue - pointsUsageForPartialPayment;

        boolean hasEnoughPointsForPartialPayment = remainingPoints >= pointsUsageForPartialPayment;
        boolean hasPromotions = order.getPromotions() != null;
        boolean isSomeCardCoverRemainingPrice = hasPromotions && order.getPromotions()
                .stream()
                .anyMatch(id -> remainingCardLimit.getOrDefault(id, 0.0) >= remainingPriceToPayAfterPartialPointsDiscount);
        boolean isPartialPaymentByPointsPossible = !isFullPaymentByPointsPossible && hasEnoughPointsForPartialPayment && hasPromotions && isSomeCardCoverRemainingPrice;

        // thirdly considering full payment by card
        String bestCardId = null;
        double bestCardUsage = 0.0;
        double bestCardDiscount = 0.0;
        if (hasPromotions) {
            for
        }




    }

    /**
     * This is the class for every order,
     * for every order there is three scenario -
     * 1) pay loyal points
     * 2) pay partial loyal points with card
     * 3) only by card
     */
    private static class Allocation {
        String methodId;
        double pointsUsage;
        double cardUsage;
        double discount;

        Allocation(String methodId, double pointsUsage, double cardUsage, double discount) {
            this.methodId = methodId;
            this.pointsUsage = pointsUsage;
            this.cardUsage = cardUsage;
            this.discount = discount;
        }

    }
}













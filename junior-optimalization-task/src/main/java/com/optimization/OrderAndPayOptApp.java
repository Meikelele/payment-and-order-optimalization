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
}

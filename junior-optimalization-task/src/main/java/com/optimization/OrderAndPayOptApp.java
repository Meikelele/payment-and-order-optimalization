package com.optimization;

import com.optimization.model.Order;
import com.optimization.model.PaymentMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class OrderAndPayOptApp
{
    public static void main( String[] args )
    {
        if (args.length < 2) {
            System.out.println("Please give two params: java -jar app.jar <orders.json> <paymentmethods.json>");
            System.exit(1);
        }

        String ordersFilePath = args[0];
        String paymentMethodsFilePath = args[1];

        ObjectMapper mapper = new ObjectMapper();

        try {
            List<Order> ordersList = mapper.readValue(
                    new File(ordersFilePath),
                    new TypeReference<List<Order>>() {}
            );
            List<PaymentMethod> paymentMethodsList = mapper.readValue(
                    new File(paymentMethodsFilePath),
                    new TypeReference<List<PaymentMethod>>() {}
            );
            System.out.println("Loaded: " + paymentMethodsList.size() + " -> payments & " + ordersList.size() + " -> orders" );
            System.out.println(ordersList);
            System.out.println(paymentMethodsList);

        } catch (IOException e) {

            System.err.println("Error reading or parsing input files:");
            System.exit(2);
        }

        //TODO: dalsza logika

        System.out.println("Another Hello world!");
    }
}

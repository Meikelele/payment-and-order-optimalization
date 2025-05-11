package com.optimization;

import com.optimization.model.Allocation;
import com.optimization.model.Order;
import com.optimization.model.PaymentMethod;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class OrderAndPayOptTest {

    @Test
    void validateArguemnts_zeroArgumentShouldThrowException() {
        // ACT
        String[] args = {};

        // ARRANGE
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> OrderAndPayOptApp.validateArguments(args));

        // ASSERT
        assertTrue(ex.getMessage().contains("Please give two params: <orders.json> <paymentmethods.json>"));
    }

    @Test
    void validateArguemnts_oneArgumentShouldThrowException() {
        // ACT
        String[] args = {"orders.json"};

        // ARRANGE
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> OrderAndPayOptApp.validateArguments(args));

        // ASSERT
        assertTrue(ex.getMessage().contains("Please give two params: <orders.json> <paymentmethods.json>"));
    }

    @Test
    void validateArguemnts_twoArgumentShouldNotThrowException() {
        // ACT
        String[] args = {"orders.json", "paymentmethods.json"};

        // ARRANGE & ASSERT
        assertDoesNotThrow(() -> OrderAndPayOptApp.validateArguments(args));
    }

    @Test
    void optimizeOrder_noPointsAndCardLimitToPay_shouldReturnNull() {
        // ARRANGE
        Order order = new Order("ORDER_X", 50.0, List.of("EmpikCard"));
        PaymentMethod points = new PaymentMethod("PUNKTY", 15, 0.0);
        Map<String, Double> cardLimits = Map.of("EmpikCard", 0.0);
        Map<String, PaymentMethod> methodsById = Map.of(
                "PUNKTY", points,
                "EmpikCard", new PaymentMethod("EmpikCard", 10, 0.0)
        );

        // ACT
        Allocation actualResult = OrderAndPayOptApp.optimizeOrder(order, points, points.getLimit(), cardLimits, methodsById);

        // ASSERT
        assertNull(actualResult);
    }

    @Test
    void optimizeOrder_partialPointsAndAnyCardPayment() {
        // ARRANGE
        Order order = new Order("ORDER_Z", 50.0, null);
        PaymentMethod points = new PaymentMethod("PUNKTY", 15, 10.0);
        Map<String, Double> cardLimits = Map.of("VeloCard", 100.0);
        Map<String, PaymentMethod> methodsById = Map.of(
                "PUNKTY", points,
                "VeloCard", new PaymentMethod("VeloCard", 10, 100.0)
        );

        // ACT
        Allocation actualResult = OrderAndPayOptApp.optimizeOrder(order, points, points.getLimit(), cardLimits, methodsById);

        // ASSERT
        assertNotNull(actualResult);
        assertEquals("PUNKTY", actualResult.getMethodId());
        assertEquals("VeloCard", actualResult.getCardId());
        assertEquals(5.0, actualResult.getPointsUsage());
        assertEquals(45.0, actualResult.getCardUsage());
        assertEquals(5.0, actualResult.getDiscount());
    }
}

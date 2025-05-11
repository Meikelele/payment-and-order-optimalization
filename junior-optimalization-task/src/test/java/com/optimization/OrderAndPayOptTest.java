package com.optimization;

import org.junit.jupiter.api.Test;
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
}

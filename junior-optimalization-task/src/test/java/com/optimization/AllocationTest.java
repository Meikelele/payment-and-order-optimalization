package com.optimization;

import com.optimization.model.Allocation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class AllocationTest {

    @Test
    void gettersShouldReturnContructorData_FullPointsPayment() {
        // ARRANGE
        String expectedMethodId = "PUNKTY";
        String expectedCardId = null;
        double expectedPointsUsage = 69.0;
        double expectedCardUsage = 0.0;
        double expectedDiscount = 15.0;

        Allocation testAllocation = new Allocation(expectedMethodId, expectedCardId, expectedPointsUsage, expectedCardUsage, expectedDiscount);

        // ACT
        String actualMethodId = testAllocation.getMethodId();
        String actualCardId = testAllocation.getCardId();
        double actualPointsUsage = testAllocation.getPointsUsage();
        double actualCardUsage = testAllocation.getCardUsage();
        double actualDiscount = testAllocation.getDiscount();

        // ASSERT
        assertEquals(expectedMethodId, actualMethodId);
        assertNull(actualCardId);
        assertEquals(expectedPointsUsage, actualPointsUsage);
        assertEquals(expectedCardUsage, actualCardUsage);
        assertEquals(expectedDiscount, actualDiscount);
    }

    @Test
    void constructorShouldSetMethodIdAsCardId_FullCardPayment() {
        // ARRANGE
        String expectedMethodId = "mZysk";
        double expectedPointsUsage = 0.0;
        double expectedCardUsage = 180.0;
        double expectedDiscount = 18.0;

        // ACT
        Allocation testAllocation = new Allocation(expectedMethodId, expectedPointsUsage, expectedCardUsage, expectedDiscount);

        // ASSERT
        assertEquals(expectedMethodId, testAllocation.getMethodId());
        assertEquals(expectedMethodId, testAllocation.getCardId());
        assertEquals(expectedPointsUsage, testAllocation.getPointsUsage());
        assertEquals(expectedCardUsage, testAllocation.getCardUsage());
        assertEquals(expectedDiscount, testAllocation.getDiscount());
    }

    @Test
    void gettersShouldReturnConstructorData_PartialPayment() {
        // ARRANGE
        String expectedMethodId = "PUNKTY";
        String expectedCardId = "BosBankrut";
        double expectedPointsUsage = 250.0;
        double expectedCardUsage = 2250;
        double expectedDiscount = 100;

        // ACT
        Allocation testAllocation = new Allocation(expectedMethodId, expectedCardId, expectedPointsUsage, expectedCardUsage, expectedDiscount);

        // ASSERT
        assertEquals(expectedMethodId, testAllocation.getMethodId());
        assertEquals(expectedCardId, testAllocation.getCardId());
        assertEquals(expectedPointsUsage, testAllocation.getPointsUsage());
        assertEquals(expectedCardUsage, testAllocation.getCardUsage());
        assertEquals(expectedDiscount, testAllocation.getDiscount());
    }









}

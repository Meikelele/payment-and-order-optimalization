package com.optimization.model;

import lombok.Getter;


/**
 * This is the class for every order,
 * for every order there is three scenario -
 * 1) pay loyal points
 * 2) pay partial loyal points with card
 * 3) only by card
 */
@Getter
public class Allocation {
    private final String methodId;
    private final String cardId;
    private final double pointsUsage;
    private final double cardUsage;
    private final double discount;

    // constructor for full payments (card or points)
    public Allocation(String methodId, double pointsUsage, double cardUsage, double discount) {
        this.methodId = methodId;
        this.cardId = methodId.equals("PUNKTY") ? null : methodId;
        this.pointsUsage = pointsUsage;
        this.cardUsage = cardUsage;
        this.discount = discount;
    }

    // constructor for partial payments
    public Allocation(String methodId, String cardId, double pointsUsage, double cardUsage, double discount) {
        this.methodId = methodId;
        this.cardId = cardId;
        this.pointsUsage = pointsUsage;
        this.cardUsage = cardUsage;
        this.discount = discount;
    }

}

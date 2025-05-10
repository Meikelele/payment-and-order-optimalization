package com.optimization.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentMethod {
    private String id;
    private int discount;
    private double limit;
}

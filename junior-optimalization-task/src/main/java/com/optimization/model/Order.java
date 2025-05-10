package com.optimization.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {
    private String id;
    private double value;
    private List<String> promotions;
}

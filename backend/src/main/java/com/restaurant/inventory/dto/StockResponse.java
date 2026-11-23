
package com.restaurant.inventory.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class StockResponse {

    private Long id;

    private Long ingredientId;

    private String ingredientName;

    private String unit;

    private BigDecimal availableQuantity;

    private LocalDateTime updatedAt;
}

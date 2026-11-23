
package com.restaurant.inventory.dto;

import com.restaurant.inventory.entity.StockMovementType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class StockMovementResponse {

    private Long id;

    private Long ingredientId;

    private String ingredientName;

    private String unit;

    private StockMovementType movementType;

    private BigDecimal quantity;

    private BigDecimal previousQuantity;

    private BigDecimal newQuantity;

    private String remarks;

    private LocalDateTime createdAt;
}

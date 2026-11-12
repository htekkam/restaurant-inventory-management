package com.restaurant.inventory.dto;

import com.restaurant.inventory.entity.IngredientUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IngredientResponse(

        Long id,
        String name,
        String category,
        IngredientUnit unit,
        String description,
        BigDecimal minimumStockLevel,
        boolean active,
        LocalDateTime createdAt

) {
}
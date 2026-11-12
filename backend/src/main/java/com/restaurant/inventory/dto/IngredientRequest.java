package com.restaurant.inventory.dto;

import com.restaurant.inventory.entity.IngredientUnit;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record IngredientRequest(

        @NotBlank(message = "Ingredient name is required")
        String name,

        @NotBlank(message = "Category is required")
        String category,

        @NotNull(message = "Unit is required")
        IngredientUnit unit,

        String description,

        @NotNull(message = "Minimum stock level is required")
        @DecimalMin(value = "0.0",
                message = "Minimum stock level cannot be negative")
        BigDecimal minimumStockLevel

) {
}

package com.restaurant.inventory.repository;

import com.restaurant.inventory.entity.StockMovement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository
        extends JpaRepository<StockMovement, Long> {

    @EntityGraph(attributePaths = "ingredient")
    List<StockMovement> findByIngredientIdOrderByCreatedAtDesc(
            Long ingredientId
    );

}

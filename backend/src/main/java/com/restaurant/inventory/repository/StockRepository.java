
package com.restaurant.inventory.repository;

import com.restaurant.inventory.entity.Stock;
import org.springframework.data.jpa.repository.*;

import com.restaurant.inventory.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.Optional;

import java.util.Optional;

public interface StockRepository
        extends JpaRepository<Stock, Long> {

    //Optional<Stock> findByIngredientId(Long ingredientId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       SELECT s
       FROM Stock s
       WHERE s.ingredient.id = :ingredientId
       """)
    Optional<Stock> findByIngredientIdForUpdate(
            @Param("ingredientId") Long ingredientId
    );

    @EntityGraph(attributePaths = "ingredient")
    Optional<Stock> findByIngredientId(Long ingredientId);

}

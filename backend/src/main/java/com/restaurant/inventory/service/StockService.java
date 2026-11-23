
package com.restaurant.inventory.service;

import com.restaurant.inventory.entity.Ingredient;
import com.restaurant.inventory.entity.Stock;
import com.restaurant.inventory.entity.StockMovement;
import com.restaurant.inventory.entity.StockMovementType;
import com.restaurant.inventory.exception.ResourceNotFoundException;
import com.restaurant.inventory.repository.StockMovementRepository;
import com.restaurant.inventory.repository.StockRepository;
import java.util.List;
import com.restaurant.inventory.entity.StockMovement;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockMovementRepository stockMovementRepository;

    public StockService(
            StockRepository stockRepository,
            StockMovementRepository stockMovementRepository) {

        this.stockRepository = stockRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional
    public StockMovement stockIn(
            Long ingredientId,
            BigDecimal quantity,
            String remarks) {

        validateQuantity(quantity);

        Stock stock = getStockForUpdate(ingredientId);

        BigDecimal previousQuantity =
                stock.getAvailableQuantity();

        BigDecimal newQuantity =
                previousQuantity.add(quantity);

        stock.setAvailableQuantity(newQuantity);

        stockRepository.save(stock);

        return recordMovement(
                stock.getIngredient(),
                StockMovementType.STOCK_IN,
                quantity,
                previousQuantity,
                newQuantity,
                remarks
        );
    }

    @Transactional
    public StockMovement stockOut(
            Long ingredientId,
            BigDecimal quantity,
            String remarks) {

        validateQuantity(quantity);

        Stock stock = getStockForUpdate(ingredientId);

        BigDecimal previousQuantity =
                stock.getAvailableQuantity();

        if (previousQuantity.compareTo(quantity) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient stock available"
            );
        }

        BigDecimal newQuantity =
                previousQuantity.subtract(quantity);

        stock.setAvailableQuantity(newQuantity);

        stockRepository.save(stock);

        return recordMovement(
                stock.getIngredient(),
                StockMovementType.STOCK_OUT,
                quantity,
                previousQuantity,
                newQuantity,
                remarks
        );
    }

    @Transactional
    public StockMovement adjustStock(
            Long ingredientId,
            BigDecimal actualQuantity,
            String remarks) {

        if (actualQuantity == null ||
                actualQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Actual quantity cannot be negative or null"
            );
        }

        Stock stock = getStockForUpdate(ingredientId);

        BigDecimal previousQuantity =
                stock.getAvailableQuantity();

        if (previousQuantity.compareTo(actualQuantity) == 0) {
            throw new IllegalArgumentException(
                    "Stock is already at the specified quantity"
            );
        }

        BigDecimal movementQuantity =
                actualQuantity.subtract(previousQuantity).abs();

        stock.setAvailableQuantity(actualQuantity);

        stockRepository.save(stock);

        return recordMovement(
                stock.getIngredient(),
                StockMovementType.ADJUSTMENT,
                movementQuantity,
                previousQuantity,
                actualQuantity,
                remarks
        );
    }

    private Stock getStockForUpdate(Long ingredientId) {

        Stock stock = stockRepository
                .findByIngredientIdForUpdate(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Stock record not found for ingredient ID: " + ingredientId
                ));

        if (!stock.getIngredient().isActive()) {
            throw new IllegalArgumentException(
                    "Cannot update stock for an inactive ingredient"
            );
        }

        return stock;
    }

    private void validateQuantity(BigDecimal quantity) {

        if (quantity == null ||
                quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }
    }

    private StockMovement recordMovement(
            Ingredient ingredient,
            StockMovementType movementType,
            BigDecimal quantity,
            BigDecimal previousQuantity,
            BigDecimal newQuantity,
            String remarks) {

        StockMovement movement = new StockMovement();

        movement.setIngredient(ingredient);
        movement.setMovementType(movementType);
        movement.setQuantity(quantity);
        movement.setPreviousQuantity(previousQuantity);
        movement.setNewQuantity(newQuantity);
        movement.setRemarks(remarks);

        return stockMovementRepository.save(movement);
    }


    public Stock getCurrentStock(Long ingredientId) {

        return stockRepository.findByIngredientId(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Stock record not found for ingredient ID: "
                                + ingredientId
                ));
    }

    public List<StockMovement> getMovementHistory(Long ingredientId) {

        return stockMovementRepository
                .findByIngredientIdOrderByCreatedAtDesc(ingredientId);
    }
}

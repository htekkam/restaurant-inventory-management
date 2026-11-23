
package com.restaurant.inventory.controller;

import com.restaurant.inventory.dto.StockAdjustmentRequest;
import com.restaurant.inventory.dto.StockMovementRequest;
import com.restaurant.inventory.dto.StockMovementResponse;
import com.restaurant.inventory.dto.StockResponse;

import com.restaurant.inventory.entity.Stock;
import com.restaurant.inventory.entity.StockMovement;

import com.restaurant.inventory.service.StockService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // STOCK IN
    @PostMapping("/in")
    public ResponseEntity<StockMovementResponse> stockIn(
            @Valid @RequestBody StockMovementRequest request) {

        StockMovement movement = stockService.stockIn(
                request.getIngredientId(),
                request.getQuantity(),
                request.getRemarks()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToResponse(movement));
    }

    // STOCK OUT
    @PostMapping("/out")
    public ResponseEntity<StockMovementResponse> stockOut(
            @Valid @RequestBody StockMovementRequest request) {

        StockMovement movement = stockService.stockOut(
                request.getIngredientId(),
                request.getQuantity(),
                request.getRemarks()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToResponse(movement));
    }

    // STOCK ADJUSTMENT
    @PostMapping("/adjust")
    public ResponseEntity<StockMovementResponse> adjustStock(
            @Valid @RequestBody StockAdjustmentRequest request) {

        StockMovement movement = stockService.adjustStock(
                request.getIngredientId(),
                request.getActualQuantity(),
                request.getRemarks()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToResponse(movement));
    }

    // GET CURRENT STOCK
    @GetMapping("/{ingredientId}")
    public ResponseEntity<StockResponse> getCurrentStock(
            @PathVariable Long ingredientId) {

        Stock stock = stockService.getCurrentStock(ingredientId);

        StockResponse response = new StockResponse();

        response.setId(stock.getId());
        response.setIngredientId(stock.getIngredient().getId());
        response.setIngredientName(stock.getIngredient().getName());
        response.setUnit(stock.getIngredient().getUnit().name());
        response.setAvailableQuantity(stock.getAvailableQuantity());
        response.setUpdatedAt(stock.getUpdatedAt());

        return ResponseEntity.ok(response);
    }

    // GET STOCK MOVEMENT HISTORY
    @GetMapping("/{ingredientId}/movements")
    public ResponseEntity<List<StockMovementResponse>> getMovementHistory(
            @PathVariable Long ingredientId) {

        List<StockMovementResponse> responses =
                stockService.getMovementHistory(ingredientId)
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    // ENTITY TO DTO MAPPING
    private StockMovementResponse mapToResponse(
            StockMovement movement) {

        StockMovementResponse response = new StockMovementResponse();

        response.setId(movement.getId());
        response.setIngredientId(movement.getIngredient().getId());
        response.setIngredientName(movement.getIngredient().getName());
        response.setUnit(movement.getIngredient().getUnit().name());
        response.setMovementType(movement.getMovementType());
        response.setQuantity(movement.getQuantity());
        response.setPreviousQuantity(movement.getPreviousQuantity());
        response.setNewQuantity(movement.getNewQuantity());
        response.setRemarks(movement.getRemarks());
        response.setCreatedAt(movement.getCreatedAt());

        return response;
    }
}

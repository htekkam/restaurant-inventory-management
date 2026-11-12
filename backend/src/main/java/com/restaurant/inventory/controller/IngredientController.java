package com.restaurant.inventory.controller;

import com.restaurant.inventory.dto.IngredientRequest;
import com.restaurant.inventory.dto.IngredientResponse;
import com.restaurant.inventory.service.IngredientService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "http://localhost:5173")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    // Create ingredient
    @PostMapping
    public ResponseEntity<IngredientResponse> createIngredient(
            @Valid @RequestBody IngredientRequest request) {

        IngredientResponse response =
                ingredientService.createIngredient(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all ingredients
    @GetMapping
    public ResponseEntity<List<IngredientResponse>> getAllIngredients() {

        return ResponseEntity.ok(
                ingredientService.getAllIngredients());
    }

    // Get ingredient by ID
    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponse> getIngredientById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ingredientService.getIngredientById(id));
    }

    // Update ingredient
    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> updateIngredient(
            @PathVariable Long id,
            @Valid @RequestBody IngredientRequest request) {

        return ResponseEntity.ok(
                ingredientService.updateIngredient(id, request));
    }

    // Deactivate ingredient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateIngredient(
            @PathVariable Long id) {

        ingredientService.deactivateIngredient(id);

        return ResponseEntity.noContent().build();
    }
}
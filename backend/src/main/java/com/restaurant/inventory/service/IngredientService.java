package com.restaurant.inventory.service;

import com.restaurant.inventory.dto.IngredientRequest;
import com.restaurant.inventory.dto.IngredientResponse;
import com.restaurant.inventory.entity.Ingredient;
import com.restaurant.inventory.exception.ResourceNotFoundException;
import com.restaurant.inventory.repository.IngredientRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    // Create ingredient
    public IngredientResponse createIngredient(IngredientRequest request) {

        if (ingredientRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException(
                    "Ingredient already exists: " + request.name());
        }

        Ingredient ingredient = new Ingredient();

        mapRequestToEntity(request, ingredient);

        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        return mapToResponse(savedIngredient);
    }

    // Get all active ingredients
    @Transactional(readOnly = true)
    public List<IngredientResponse> getAllIngredients() {

        return ingredientRepository.findAll()
                .stream()
                .filter(Ingredient::isActive)
                .map(this::mapToResponse)
                .toList();
    }

    // Get ingredient by ID
    @Transactional(readOnly = true)
    public IngredientResponse getIngredientById(Long id) {

        Ingredient ingredient = getActiveIngredient(id);

        return mapToResponse(ingredient);
    }

    // Update ingredient
    public IngredientResponse updateIngredient(
            Long id, IngredientRequest request) {

        Ingredient ingredient = getActiveIngredient(id);

        boolean duplicate = ingredientRepository
                .existsByNameIgnoreCaseAndIdNot(request.name(), id);

        if (duplicate) {
            throw new IllegalArgumentException(
                    "Another ingredient already exists with name: "
                            + request.name());
        }

        mapRequestToEntity(request, ingredient);

        Ingredient updatedIngredient = ingredientRepository.save(ingredient);

        return mapToResponse(updatedIngredient);
    }

    // Deactivate ingredient (soft delete)
    public void deactivateIngredient(Long id) {

        Ingredient ingredient = getActiveIngredient(id);

        ingredient.setActive(false);

        ingredientRepository.save(ingredient);
    }

    // Reusable method to retrieve active ingredient
    private Ingredient getActiveIngredient(Long id) {

        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ingredient not found with ID: " + id));

        if (!ingredient.isActive()) {
            throw new ResourceNotFoundException(
                    "Ingredient is inactive with ID: " + id);
        }

        return ingredient;
    }

    // Request DTO to Entity mapping
    private void mapRequestToEntity(
            IngredientRequest request, Ingredient ingredient) {

        ingredient.setName(request.name().trim());
        ingredient.setCategory(request.category().trim());
        ingredient.setUnit(request.unit());
        ingredient.setDescription(request.description());
        ingredient.setMinimumStockLevel(request.minimumStockLevel());
    }

    // Entity to Response DTO mapping
    private IngredientResponse mapToResponse(Ingredient ingredient) {

        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getCategory(),
                ingredient.getUnit(),
                ingredient.getDescription(),
                ingredient.getMinimumStockLevel(),
                ingredient.isActive(),
                ingredient.getCreatedAt()
        );
    }
}
package com.example.springdata.restaurantbackend.Mapper;

import com.example.springdata.restaurantbackend.DTO.IngredientDTO;
import com.example.springdata.restaurantbackend.Entity.Ingredient;

import java.time.LocalDateTime;

public class IngredientMapper {
    public static IngredientDTO toDTO(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }
        return new IngredientDTO(
                ingredient.getId(),
                ingredient.getNom(),
                ingredient.getQuantite(),
                ingredient.getSeuil(),
                ingredient.getPrix(),
                ingredient.getUpdatedAt() // Ajout de updatedAt
        );
    }


    public static Ingredient toEntity(IngredientDTO ingredientDTO) {
        if (ingredientDTO == null) {
            return null;
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientDTO.getId());
        ingredient.setNom(ingredientDTO.getNom());
        ingredient.setQuantite(ingredientDTO.getQuantite());
        ingredient.setSeuil(ingredientDTO.getSeuil());
        ingredient.setPrix(ingredientDTO.getPrix());
        ingredient.setUpdatedAt(ingredientDTO.getUpdatedAt() != null ? ingredientDTO.getUpdatedAt() : LocalDateTime.now());
        return ingredient;
    }

}

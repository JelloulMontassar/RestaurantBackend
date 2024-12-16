package com.example.springdata.restaurantbackend.Mapper;

import com.example.springdata.restaurantbackend.DTO.IngredientDTO;
import com.example.springdata.restaurantbackend.DTO.RepasDTO;
import com.example.springdata.restaurantbackend.Entity.Repas;

import java.util.stream.Collectors;

public class RepasMapper {

    // Conversion d'un repas en DTO
    public static RepasDTO toDTO(Repas repas) {
        if (repas == null) {
            return null;
        }
        return new RepasDTO(
                repas.getId(),
                repas.getNom(),
                repas.getType(),
                repas.getCreatedAt(),
                repas.getUpdatedAt(),
                repas.getIngredients().stream()
                        .map(ingredient -> {
                            // Conversion des ingrédients avec ajout de quantiteRestante
                            IngredientDTO ingredientDTO = IngredientMapper.toDTO(ingredient);
                            ingredientDTO.setQuantiteRestante(ingredient.getQuantite()); // Ajout de quantiteRestante
                            return ingredientDTO;
                        })
                        .collect(Collectors.toList()),
                repas.getPrixTotal() // Ajouter le prix total dans le DTO
        );
    }

    // Conversion d'un DTO en entité Repas
    public static Repas toEntity(RepasDTO repasDTO) {
        if (repasDTO == null) {
            return null;
        }
        Repas repas = new Repas();
        repas.setId(repasDTO.getId());
        repas.setNom(repasDTO.getNom());
        repas.setType(repasDTO.getType());
        repas.setIngredients(
                repasDTO.getIngredients().stream()
                        .map(IngredientMapper::toEntity) // Conversion des ingrédients en entité
                        .collect(Collectors.toList())
        );
        repas.setPrixTotal(calculatePrixTotal(repas)); // Calcul du prix total lors de la conversion
        return repas;
    }

    // Calcul du prix total lors de la conversion depuis un DTO vers une entité
    private static double calculatePrixTotal(Repas repas) {
        if (repas.getIngredients() != null) {
            return repas.getIngredients().stream()
                    .mapToDouble(ingredient -> ingredient.getQuantite() * ingredient.getPrix())
                    .sum();
        }
        return 0.0;
    }
}

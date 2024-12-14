package com.example.springdata.restaurantbackend.Mapper;

import com.example.springdata.restaurantbackend.DTO.IngredientDTO;
import com.example.springdata.restaurantbackend.DTO.RepasDTO;
import com.example.springdata.restaurantbackend.Entity.Repas;

import java.util.stream.Collectors;

public class RepasMapper {
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
                        .map(IngredientMapper::toDTO)
                        .collect(Collectors.toList()),
                repas.getPrixTotal()

        );
    }

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
                        .map(IngredientMapper::toEntity)
                        .collect(Collectors.toList())
        );

        return repas;
    }
}

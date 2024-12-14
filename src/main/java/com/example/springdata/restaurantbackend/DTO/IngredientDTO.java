package com.example.springdata.restaurantbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {

    private Long id;
    private String nom;
    private double quantite;
    private double seuil;
    private LocalDateTime updatedAt;
}
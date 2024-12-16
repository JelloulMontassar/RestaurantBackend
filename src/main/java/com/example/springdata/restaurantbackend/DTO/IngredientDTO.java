package com.example.springdata.restaurantbackend.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {

    private Long id;
    private String nom;
    private double quantite;
    private double seuil;
    private double prix;
    private LocalDateTime updatedAt;
    private double quantiteRestante;
    }
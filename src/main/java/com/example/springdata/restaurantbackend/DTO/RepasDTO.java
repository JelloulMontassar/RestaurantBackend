package com.example.springdata.restaurantbackend.DTO;

import com.example.springdata.restaurantbackend.Enums.TypeRepas;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepasDTO {

    private Long id;
    private String nom;
    private TypeRepas type;
    private double prix;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<IngredientDTO> ingredients;
}
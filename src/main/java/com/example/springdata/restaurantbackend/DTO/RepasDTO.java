package com.example.springdata.restaurantbackend.DTO;

import com.example.springdata.restaurantbackend.Enums.TypeRepas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Le nom du repas ne peut pas être vide.")
    private String nom;

    @NotBlank(message = "Le type du repas est obligatoire.")
    private TypeRepas type;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull(message = "La liste des ingrédients ne peut pas être nulle.")
    private List<IngredientDTO> ingredients;
    private double prixTotal;


}
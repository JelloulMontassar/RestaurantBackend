package com.example.springdata.restaurantbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeRepasDTO {

    private Long id;
    private UtilisateurDTO etudiant;
    private RepasDTO repas;
    private int quantite;
    private LocalDateTime dateCommande;
}
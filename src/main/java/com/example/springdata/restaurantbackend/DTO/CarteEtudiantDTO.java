package com.example.springdata.restaurantbackend.DTO;

import com.example.springdata.restaurantbackend.Enums.StatutCarte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarteEtudiantDTO {

        private Long id;
        private double solde;
        private StatutCarte statut;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private UtilisateurDTO etudiant;

}

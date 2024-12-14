package com.example.springdata.restaurantbackend.DTO;

import com.example.springdata.restaurantbackend.Enums.StatutCarte;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarteEtudiantDTO {

        private Long id;
        private double solde;
        private StatutCarte statut;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private int version;
        private UtilisateurDTO etudiant;

}

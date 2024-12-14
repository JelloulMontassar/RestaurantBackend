package com.example.springdata.restaurantbackend.DTO;

import com.example.springdata.restaurantbackend.Enums.TypePaiement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementDTO {

    private Long id;
    private UtilisateurDTO utilisateur;
    private double montant;
    private TypePaiement type;
    private LocalDateTime datePaiement;
}
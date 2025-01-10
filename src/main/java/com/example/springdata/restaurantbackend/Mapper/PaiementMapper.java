package com.example.springdata.restaurantbackend.Mapper;

import com.example.springdata.restaurantbackend.DTO.PaiementDTO;
import com.example.springdata.restaurantbackend.Entity.Paiement;

public class PaiementMapper {
    public static PaiementDTO toDTO(Paiement paiement) {
        if (paiement == null) {
            return null;
        }
        return new PaiementDTO(
                paiement.getId(),
                UtilisateurMapper.toDTO(paiement.getUtilisateur()),
                paiement.getMontant(),
                paiement.getType(),
                paiement.getDatePaiement()
        );
    }

    public static Paiement toEntity(PaiementDTO paiementDTO) {
        if (paiementDTO == null) {
            return null;
        }
        Paiement paiement = new Paiement();
        paiement.setId(paiementDTO.getId());
        paiement.setUtilisateur(UtilisateurMapper.toEntity(paiementDTO.getUtilisateur()));
        paiement.setMontant(paiementDTO.getMontant());
        paiement.setType(paiementDTO.getType());
        paiement.setDatePaiement(paiementDTO.getDatePaiement());
        return paiement;
    }
}

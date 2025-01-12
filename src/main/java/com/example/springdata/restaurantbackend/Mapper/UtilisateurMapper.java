package com.example.springdata.restaurantbackend.Mapper;

import com.example.springdata.restaurantbackend.DTO.UtilisateurDTO;
import com.example.springdata.restaurantbackend.Entity.Utilisateur;

public class UtilisateurMapper {

    public static UtilisateurDTO toDTO(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        return new UtilisateurDTO(
                utilisateur.getId(),
                utilisateur.getNomUtilisateur(),
                utilisateur.getPrenomUtilisateur(),
                utilisateur.getGenre(),
                utilisateur.getEmail(),
                utilisateur.getNumeroTelephone(),
                utilisateur.getRole(),
                utilisateur.getCreatedAt(),
                utilisateur.getUpdatedAt()

        );
    }

    public static Utilisateur toEntity(UtilisateurDTO utilisateurDTO) {
        if (utilisateurDTO == null) {
            return null;
        }
        return new Utilisateur(
                utilisateurDTO.getId(),
                utilisateurDTO.getNomUtilisateur(),
                utilisateurDTO.getPrenomUtilisateur(),
                utilisateurDTO.getGenre(),
                null,
                utilisateurDTO.getEmail(),
                utilisateurDTO.getRole(),
                utilisateurDTO.getNumeroTelephone(),
                utilisateurDTO.getCreatedAt(),
                utilisateurDTO.getUpdatedAt()
        );
    }
}

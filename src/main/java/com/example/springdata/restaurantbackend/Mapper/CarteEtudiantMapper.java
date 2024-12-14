package com.example.springdata.restaurantbackend.Mapper;

import com.example.springdata.restaurantbackend.DTO.CarteEtudiantDTO;
import com.example.springdata.restaurantbackend.Entity.CarteEtudiant;

public class CarteEtudiantMapper {

    public static CarteEtudiantDTO toDTO(CarteEtudiant carteEtudiant) {
        if (carteEtudiant == null) {
            return null;
        }
        return new CarteEtudiantDTO(
                carteEtudiant.getId(),
                carteEtudiant.getSolde(),
                carteEtudiant.getStatut(),
                carteEtudiant.getCreatedAt(),
                carteEtudiant.getUpdatedAt(),
                carteEtudiant.getVersion(),
                UtilisateurMapper.toDTO(carteEtudiant.getEtudiant())
        );
    }

    public static CarteEtudiant toEntity(CarteEtudiantDTO carteEtudiantDTO) {
        if (carteEtudiantDTO == null) {
            return null;
        }
        return new CarteEtudiant(
                        carteEtudiantDTO.getId(),
                        carteEtudiantDTO.getSolde(),
                        carteEtudiantDTO.getStatut(),
                        carteEtudiantDTO.getCreatedAt(),
                        carteEtudiantDTO.getUpdatedAt(),
                UtilisateurMapper.toEntity(carteEtudiantDTO.getEtudiant()),
                carteEtudiantDTO.getVersion()
                );
    }
}

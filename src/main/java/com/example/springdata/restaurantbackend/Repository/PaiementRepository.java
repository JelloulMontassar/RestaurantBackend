package com.example.springdata.restaurantbackend.Repository;

import com.example.springdata.restaurantbackend.Entity.Paiement;
import com.example.springdata.restaurantbackend.Entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    @Query("SELECT p FROM Paiement p WHERE p.utilisateur.id = :utilisateurId AND p.datePaiement BETWEEN :startOfDay AND :endOfDay")
    List<Paiement> findByUtilisateurIdAndDatePaiementBetween(
            @Param("utilisateurId") Long utilisateurId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    //List<Paiement> findByUtilisateur(Long utilisateurId);

    List<Paiement> findByUtilisateur_Id(Long utilisateurId);

    @Query("SELECT p FROM Paiement p WHERE p.utilisateur.id = :utilisateurId")
    List<Paiement> findByUtilisateur(@Param("utilisateurId") Long utilisateurId);

}

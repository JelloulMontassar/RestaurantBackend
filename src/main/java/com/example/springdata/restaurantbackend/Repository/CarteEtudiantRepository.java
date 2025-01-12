package com.example.springdata.restaurantbackend.Repository;

import com.example.springdata.restaurantbackend.Entity.CarteEtudiant;
import com.example.springdata.restaurantbackend.Entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CarteEtudiantRepository extends JpaRepository<CarteEtudiant, Integer> {
    @Query("SELECT p FROM Paiement p WHERE p.utilisateur.id = :utilisateurId AND FUNCTION('DATE', p.datePaiement) = :datePaiement")
    List<Paiement> findByUtilisateurIdAndDatePaiement(@Param("utilisateurId") Long utilisateurId, @Param("datePaiement") LocalDate datePaiement);



}

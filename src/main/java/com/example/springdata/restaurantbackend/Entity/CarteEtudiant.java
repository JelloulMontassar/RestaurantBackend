package com.example.springdata.restaurantbackend.Entity;

import com.example.springdata.restaurantbackend.Enums.StatutCarte;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cartes_etudiants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarteEtudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double solde = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCarte statut = StatutCarte.ACTIVE;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "id_etudiant", nullable = false)
    private Utilisateur etudiant;
}

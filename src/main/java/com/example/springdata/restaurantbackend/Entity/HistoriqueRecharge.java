package com.example.springdata.restaurantbackend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historique_recharge")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueRecharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "carte_etudiant_id", nullable = false)
    private CarteEtudiant carteEtudiant;

    @Column(nullable = false)
    private double montant;

    @Column(nullable = false)
    private LocalDateTime dateRecharge = LocalDateTime.now();
}

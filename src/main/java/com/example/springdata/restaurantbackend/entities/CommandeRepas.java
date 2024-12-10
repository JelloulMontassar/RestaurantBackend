package com.example.springdata.restaurantbackend.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "commandes_repas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeRepas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_etudiant", nullable = false)
    private Utilisateur etudiant;

    @ManyToOne
    @JoinColumn(name = "id_repas", nullable = false)
    private Repas repas;

    @Column(nullable = false)
    private int quantite;

    @Column(name = "date_commande", nullable = false)
    private LocalDateTime dateCommande = LocalDateTime.now();
}

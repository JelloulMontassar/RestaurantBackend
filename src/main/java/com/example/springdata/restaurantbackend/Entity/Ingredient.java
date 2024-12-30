package com.example.springdata.restaurantbackend.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    private double quantite;

    @Column(nullable = false)
    private double seuil;

    @Column(nullable = false)
    private double prix;

    @Transient // Ne sera pas persisté dans la base de données
    private double quantiteRestante;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    /*@PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }*/
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}

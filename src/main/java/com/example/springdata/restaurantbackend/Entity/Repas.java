package com.example.springdata.restaurantbackend.Entity;

import com.example.springdata.restaurantbackend.Enums.TypeRepas;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "repas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeRepas type;

    @Column(nullable = false)
    private double prixTotal = 0.0;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "repas_ingredients",
            joinColumns = @JoinColumn(name = "id_repas"),
            inverseJoinColumns = @JoinColumn(name = "id_ingredient")
    )
    private List<Ingredient> ingredients;

    @PrePersist
    @PreUpdate
    public void calculerPrixTotal() {
        if (ingredients != null) {
            this.prixTotal = ingredients.stream()
                    .mapToDouble(ingredient -> {
                        // Trouver la quantité choisie pour cet ingrédient dans le repas
                        double quantiteChoisie = ingredient.getQuantite();
                        // Calculer le prix pour la quantité choisie
                        return quantiteChoisie * ingredient.getPrix();
                    })
                    .sum();
        }
        this.updatedAt = LocalDateTime.now();
    }
}

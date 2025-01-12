package com.example.springdata.restaurantbackend.Entity;

import com.example.springdata.restaurantbackend.Enums.TypeRepas;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @ManyToMany(mappedBy = "repas")
    private List<Menu> menus = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "repas_ingredients",
            joinColumns = @JoinColumn(name = "id_repas"),
            inverseJoinColumns = @JoinColumn(name = "id_ingredient")
    )
    private List<Ingredient> ingredients;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        calculerPrixTotal();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculerPrixTotal();
    }

    public void calculerPrixTotal() {
        if (ingredients != null) {
            this.prixTotal = ingredients.stream()
                    .mapToDouble(ingredient -> ingredient.getQuantite() * ingredient.getPrix())
                    .sum();
            System.out.println("Prix total: " + prixTotal);
        }
    }

}

package com.example.springdata.restaurantbackend.Repository;

import com.example.springdata.restaurantbackend.Entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    boolean existsByNom(String nom);

}

package com.example.springdata.restaurantbackend.DAO;

import com.example.springdata.restaurantbackend.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientDAO extends JpaRepository<Ingredient, Long> {
}

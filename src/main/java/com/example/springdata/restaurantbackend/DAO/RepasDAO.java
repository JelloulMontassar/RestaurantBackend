package com.example.springdata.restaurantbackend.DAO;

import com.example.springdata.restaurantbackend.entities.Repas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepasDAO extends JpaRepository<Repas, Integer> {
}

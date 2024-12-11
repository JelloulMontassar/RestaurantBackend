package com.example.springdata.restaurantbackend.Repository;

import com.example.springdata.restaurantbackend.Entity.Repas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepasDAO extends JpaRepository<Repas, Integer> {
}

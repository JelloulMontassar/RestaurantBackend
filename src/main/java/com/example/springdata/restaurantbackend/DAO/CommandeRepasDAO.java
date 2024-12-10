package com.example.springdata.restaurantbackend.DAO;

import com.example.springdata.restaurantbackend.entities.CommandeRepas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepasDAO extends JpaRepository<CommandeRepas, Long> {
}

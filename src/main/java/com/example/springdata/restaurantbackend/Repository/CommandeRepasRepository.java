package com.example.springdata.restaurantbackend.Repository;

import com.example.springdata.restaurantbackend.Entity.CommandeRepas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepasRepository extends JpaRepository<CommandeRepas, Long> {
}

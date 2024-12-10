package com.example.springdata.restaurantbackend.DAO;

import com.example.springdata.restaurantbackend.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementDAO extends JpaRepository<Paiement, Long> {
}

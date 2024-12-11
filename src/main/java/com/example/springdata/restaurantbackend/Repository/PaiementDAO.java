package com.example.springdata.restaurantbackend.Repository;

import com.example.springdata.restaurantbackend.Entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementDAO extends JpaRepository<Paiement, Long> {
}

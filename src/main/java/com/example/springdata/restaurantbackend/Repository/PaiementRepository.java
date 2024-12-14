package com.example.springdata.restaurantbackend.Repository;

import com.example.springdata.restaurantbackend.Entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
}

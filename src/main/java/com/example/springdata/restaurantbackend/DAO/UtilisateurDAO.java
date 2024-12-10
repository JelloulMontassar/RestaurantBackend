package com.example.springdata.restaurantbackend.DAO;

import com.example.springdata.restaurantbackend.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurDAO extends JpaRepository<Utilisateur, Integer> {
}

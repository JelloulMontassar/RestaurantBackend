package com.example.springdata.restaurantbackend.DAO;

import com.example.springdata.restaurantbackend.entities.CarteEtudiant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarteEtudiantDAO extends JpaRepository<CarteEtudiant, Integer> {
}

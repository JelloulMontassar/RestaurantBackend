package com.example.springdata.restaurantbackend.Repository;

import com.example.springdata.restaurantbackend.Entity.CarteEtudiant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarteEtudiantRepository extends JpaRepository<CarteEtudiant, Integer> {
}

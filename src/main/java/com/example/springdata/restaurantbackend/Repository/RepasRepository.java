package com.example.springdata.restaurantbackend.Repository;

import com.example.springdata.restaurantbackend.Entity.Repas;
import com.example.springdata.restaurantbackend.Enums.TypeRepas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepasRepository extends JpaRepository<Repas, Long> {

    List<Repas> findByType(TypeRepas type);
}

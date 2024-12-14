package com.example.springdata.restaurantbackend.Repository;

import com.example.springdata.restaurantbackend.Entity.HistoriqueRecharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueRechargeRepository extends JpaRepository<HistoriqueRecharge, Long> {
    List<HistoriqueRecharge> findByCarteEtudiantId(Long carteEtudiantId);
}

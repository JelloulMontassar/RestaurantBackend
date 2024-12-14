package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.Entity.Repas;
import com.example.springdata.restaurantbackend.Repository.RepasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepasService {

    @Autowired
    private RepasRepository repasRepository;

    public List<Repas> getAllRepas() {
        return repasRepository.findAll();
    }

    public Repas getRepasById(Long id) {
        return repasRepository.findById(id).orElse(null);
    }

    public Repas saveRepas(Repas repas) {
        return repasRepository.save(repas);
    }

    public void deleteRepas(Long id) {
        repasRepository.deleteById(id);
    }

    public Repas updateRepas(Repas repas) {
        return repasRepository.save(repas);
    }
}
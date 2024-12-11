package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.Repository.CarteEtudiantRepository;
import com.example.springdata.restaurantbackend.Entity.CarteEtudiant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarteEtudiantService {
    @Autowired
    private CarteEtudiantRepository carteEtudiantRepository;
    public CarteEtudiant saveCarteEtudiant(CarteEtudiant carteEtudiant) {
        return carteEtudiantRepository.save(carteEtudiant);
    }
    public CarteEtudiant getCarteEtudiantById(int id) {
        return carteEtudiantRepository.findById(id).orElse(null);
    }
    public void deleteCarteEtudiant(int id) {
        carteEtudiantRepository.deleteById(id);
    }
    public CarteEtudiant updateCarteEtudiant(CarteEtudiant carteEtudiant) {
        return carteEtudiantRepository.save(carteEtudiant);
    }








}

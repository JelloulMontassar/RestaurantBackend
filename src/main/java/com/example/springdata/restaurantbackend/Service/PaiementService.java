package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.Entity.CarteEtudiant;
import com.example.springdata.restaurantbackend.Entity.Paiement;
import com.example.springdata.restaurantbackend.Enums.TypePaiement;
import com.example.springdata.restaurantbackend.Repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    public Paiement getPaiementById(Long id) {
        return paiementRepository.findById(id).orElse(null);
    }

    public Paiement enregistrerPaiement(CarteEtudiant carteEtudiant, double montant, TypePaiement typePaiement) {
        Paiement paiement = new Paiement();
        paiement.setUtilisateur(carteEtudiant.getEtudiant());
        paiement.setMontant(montant);
        paiement.setType(typePaiement);
        paiement.setDatePaiement(LocalDateTime.now());
        Paiement savedPaiement = paiementRepository.save(paiement);
        System.out.println("Paiement enregistré : " + savedPaiement);
        return savedPaiement;
    }

    public void deletePaiement(Long id) {
        paiementRepository.deleteById(id);
    }

    public Paiement updatePaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }
}
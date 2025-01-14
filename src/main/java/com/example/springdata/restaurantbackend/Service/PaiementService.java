package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.Entity.CarteEtudiant;
import com.example.springdata.restaurantbackend.Entity.Paiement;
import com.example.springdata.restaurantbackend.Enums.TypePaiement;
import com.example.springdata.restaurantbackend.Repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        System.out.println("Paiement à enregistrer : " + paiement);
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

    /*public List<Paiement> getPaiementsByUtilisateur(Long utilisateurId, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return paiementRepository.findByUtilisateurIdAndDatePaiementBetween(utilisateurId, startDate, endDate);
        } else {
            return paiementRepository.findByUtilisateur(utilisateurId);
        }
    }*/

    public List<Paiement> getPaiementsByUtilisateur(Long utilisateurId, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return paiementRepository.findByUtilisateurIdAndDatePaiementBetween(utilisateurId, startDate, endDate);
        } else {
            return paiementRepository.findByUtilisateur_Id(utilisateurId);
        }
    }


    private LocalDateTime parseDate(String dateStr, boolean isStartOfDay) {
        if (dateStr.length() == 10) { // Format: yyyy-MM-dd
            LocalDate date = LocalDate.parse(dateStr); // Parse to LocalDate
            return isStartOfDay ? date.atStartOfDay() : date.atTime(23, 59, 59);
        }
        return LocalDateTime.parse(dateStr); // Parse full LocalDateTime
    }



}

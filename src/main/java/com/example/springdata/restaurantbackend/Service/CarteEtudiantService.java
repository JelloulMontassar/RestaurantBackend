package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.DTO.CarteEtudiantDTO;
import com.example.springdata.restaurantbackend.DTO.UtilisateurDTO;
import com.example.springdata.restaurantbackend.Entity.CarteEtudiant;
import com.example.springdata.restaurantbackend.Entity.HistoriqueRecharge;
import com.example.springdata.restaurantbackend.Entity.Paiement;
import com.example.springdata.restaurantbackend.Entity.Utilisateur;
import com.example.springdata.restaurantbackend.Enums.StatutCarte;
import com.example.springdata.restaurantbackend.Enums.TypePaiement;
import com.example.springdata.restaurantbackend.Mapper.CarteEtudiantMapper;
import com.example.springdata.restaurantbackend.Repository.CarteEtudiantRepository;
import com.example.springdata.restaurantbackend.Repository.HistoriqueRechargeRepository;
import com.example.springdata.restaurantbackend.Repository.PaiementRepository;
import com.example.springdata.restaurantbackend.Requests.PayerRepasRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarteEtudiantService {

    @Autowired
    private CarteEtudiantRepository carteEtudiantRepository;

    @Autowired
    private HistoriqueRechargeRepository historiqueRechargeRepository;
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private RepasService repasService;
    @Autowired
    private PaiementService paiementService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private PaiementRepository paiementRepository;


    public List<CarteEtudiantDTO> getAllCartesEtudiants() {
        return carteEtudiantRepository.findAll().stream()
                .map(CarteEtudiantMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CarteEtudiantDTO saveCarteEtudiant(CarteEtudiantDTO carteEtudiantDTO) {
        UtilisateurDTO utilisateur = utilisateurService.getUtilisateurById(carteEtudiantDTO.getEtudiant().getId());
        if (utilisateur == null) {
            return null;
        }
        System.out.println("utilisateur = " + utilisateur.getNomUtilisateur());
        carteEtudiantDTO.setEtudiant(utilisateur);
        CarteEtudiant carteEtudiant = CarteEtudiantMapper.toEntity(carteEtudiantDTO);
        return CarteEtudiantMapper.toDTO(carteEtudiantRepository.save(carteEtudiant));
    }

    public CarteEtudiantDTO getCarteEtudiantById(Long id) {
        CarteEtudiant carteEtudiant = carteEtudiantRepository.findById(Math.toIntExact(id)).orElse(null);
        return CarteEtudiantMapper.toDTO(carteEtudiant);
    }

    public void deleteCarteEtudiant(Long id) {
        carteEtudiantRepository.deleteById(Math.toIntExact(id));
    }

    public CarteEtudiantDTO updateSolde(Long id, double solde) {
        CarteEtudiant carteEtudiant = carteEtudiantRepository.findById(Math.toIntExact(id)).orElse(null);
        if (carteEtudiant != null) {
            carteEtudiant.setSolde(solde+carteEtudiant.getSolde());
            carteEtudiantRepository.save(carteEtudiant);
            HistoriqueRecharge historiqueRecharge = new HistoriqueRecharge();
            historiqueRecharge.setCarteEtudiant(carteEtudiant);
            historiqueRecharge.setMontant(solde);
            historiqueRechargeRepository.save(historiqueRecharge);

            return CarteEtudiantMapper.toDTO(carteEtudiant);
        }
        return null;
    }

    public CarteEtudiantDTO blockCarteEtudiant(Long id) {
        CarteEtudiant carteEtudiant = carteEtudiantRepository.findById(Math.toIntExact(id)).orElse(null);
        if (carteEtudiant != null) {
            carteEtudiant.setStatut(StatutCarte.BLOQUEE);
            return CarteEtudiantMapper.toDTO(carteEtudiantRepository.save(carteEtudiant));
        }
        return null;
    }

    public List<HistoriqueRecharge> getHistoriqueRecharge(Long carteEtudiantId) {
        return historiqueRechargeRepository.findByCarteEtudiantId(carteEtudiantId);
    }

    @Transactional
    public void payerRepas(Long carteId, List<Long> repasIds, TypePaiement typePaiement) {
        CarteEtudiant carteEtudiant = carteEtudiantRepository.findById(Math.toIntExact(carteId))
                .orElseThrow(() -> new IllegalArgumentException("Carte non trouvée"));

        LocalDate today = LocalDate.now();
        for (Long repasId : repasIds) {
            if (!menuService.isRepasAvailableInMenuForDate(repasId, today)) {
                throw new IllegalArgumentException("Le repas ID " + repasId + " n'est pas disponible dans le menu de cette semaine.");
            }
        }

        double prixTotal = repasService.calculerPrixTotalRepas(repasIds);
        if (carteEtudiant.getSolde() < prixTotal) {
            throw new IllegalArgumentException("Solde insuffisant.");
        }

        carteEtudiant.setSolde(carteEtudiant.getSolde() - prixTotal);
        carteEtudiantRepository.save(carteEtudiant);

        paiementService.enregistrerPaiement(carteEtudiant, prixTotal, typePaiement);

        HistoriqueRecharge historiqueRecharge = new HistoriqueRecharge();
        historiqueRecharge.setCarteEtudiant(carteEtudiant);
        historiqueRecharge.setMontant(-prixTotal);
        historiqueRechargeRepository.save(historiqueRecharge);
    }

    public String genererRecuPaiement(Long utilisateurId, LocalDate datePaiement) {
        LocalDateTime startOfDay = datePaiement.atStartOfDay();
        LocalDateTime endOfDay = datePaiement.atTime(23, 59, 59);

List<Paiement> paiements = paiementRepository.findByUtilisateurIdAndDatePaiementBetween(utilisateurId, startOfDay, endOfDay);
        System.out.println("Paiements récupérés : " + paiements);

        if (paiements.isEmpty()) {
            throw new IllegalArgumentException("Aucun paiement trouvé pour cet utilisateur à cette date.");
        }

        StringBuilder recu = new StringBuilder();
        recu.append("=== Reçu de Paiement ===\n");
        recu.append("ID Utilisateur : ").append(utilisateurId).append("\n");
        recu.append("Date : ").append(datePaiement).append("\n");
        recu.append("========================\n");

        double total = 0;
        for (Paiement paiement : paiements) {
            recu.append("ID Paiement : ").append(paiement.getId()).append("\n");
            recu.append("Montant : ").append(paiement.getMontant()).append("\n");
            recu.append("Type : ").append(paiement.getType()).append("\n");
            recu.append("------------------------\n");
            total += paiement.getMontant();
        }

        recu.append("Total Payé : ").append(total).append("\n");
        recu.append("========================");

        return recu.toString();
    }

}

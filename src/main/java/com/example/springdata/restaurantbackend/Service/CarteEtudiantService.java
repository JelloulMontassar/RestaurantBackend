package com.example.springdata.restaurantbackend.Service;

import com.sinch.sdk.domains.sms.BatchesService;
import com.sinch.sdk.domains.sms.SMSService;
import com.sinch.sdk.domains.sms.models.BatchText;
import com.sinch.sdk.domains.sms.models.requests.SendSmsBatchTextRequest;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
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
    private SMSService smsService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RepasService repasService;

    @Autowired
    private PaiementService paiementService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private PaiementRepository paiementRepository;

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("choeurproject@gmail.com");
        javaMailSender.send(message);
    }

    public List<CarteEtudiantDTO> getAllCartesEtudiants() {
        return carteEtudiantRepository.findAll().stream()
                .map(CarteEtudiantMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CarteEtudiantDTO saveCarteEtudiant(CarteEtudiantDTO carteEtudiantDTO) {
        UtilisateurDTO utilisateur = utilisateurService.getUtilisateurById(carteEtudiantDTO.getEtudiant().getId());
        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur introuvable.");
        }
        carteEtudiantDTO.setEtudiant(utilisateur);
        CarteEtudiant carteEtudiant = CarteEtudiantMapper.toEntity(carteEtudiantDTO);
        return CarteEtudiantMapper.toDTO(carteEtudiantRepository.save(carteEtudiant));
    }

    public CarteEtudiantDTO getCarteEtudiantById(Long id) {
        return carteEtudiantRepository.findById(Math.toIntExact(id))
                .map(CarteEtudiantMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Carte étudiant introuvable."));
    }

    public void deleteCarteEtudiant(Long id) {
        if (!carteEtudiantRepository.existsById(Math.toIntExact(id))) {
            throw new IllegalArgumentException("Carte étudiant introuvable pour suppression.");
        }
        carteEtudiantRepository.deleteById(Math.toIntExact(id));
    }

    public CarteEtudiantDTO updateSolde(Long id, double solde) {
        CarteEtudiant carteEtudiant = carteEtudiantRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Carte étudiant introuvable."));

        carteEtudiant.setSolde(carteEtudiant.getSolde() + solde);
        carteEtudiantRepository.save(carteEtudiant);

        HistoriqueRecharge historiqueRecharge = new HistoriqueRecharge();
        historiqueRecharge.setCarteEtudiant(carteEtudiant);
        historiqueRecharge.setMontant(solde);
        historiqueRechargeRepository.save(historiqueRecharge);

        return CarteEtudiantMapper.toDTO(carteEtudiant);
    }

    public CarteEtudiantDTO blockCarteEtudiant(Long id) {
        CarteEtudiant carteEtudiant = carteEtudiantRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Carte étudiant introuvable."));

        carteEtudiant.setStatut(StatutCarte.BLOQUEE);
        return CarteEtudiantMapper.toDTO(carteEtudiantRepository.save(carteEtudiant));
    }

    public List<HistoriqueRecharge> getHistoriqueRecharge(Long carteEtudiantId) {
        return historiqueRechargeRepository.findByCarteEtudiantId(carteEtudiantId);
    }

    @Transactional
    @Scheduled(fixedDelay = 3600000) // Executes every hour
    public void checkAndNotifyLowBalance() {
        List<CarteEtudiant> lowBalanceCards = carteEtudiantRepository.findAll()
                .stream()
                .filter(carte -> carte.getSolde() < 10)
                .collect(Collectors.toList());

        lowBalanceCards.forEach(carte -> {
            Utilisateur student = carte.getEtudiant();
            notifyLowBalance(student);
        });
    }

    private void notifyLowBalance(Utilisateur student) {
        String body = "Votre solde de carte étudiant est faible. Veuillez recharger votre carte.";
        String emailSubject = "Alerte Solde de Carte Étudiant";

        try {
            sendEmail(student.getEmail(), emailSubject, body);
            System.out.println("Email envoyé avec succès à : " + student.getEmail());
        } catch (Exception e) {
            System.err.println("Échec de l'envoi de l'email à : " + student.getEmail());
            e.printStackTrace();
        }

        try {

            System.out.println("SMS envoyé avec succès à : " + student.getNumeroTelephone());
        } catch (Exception e) {
            System.err.println("Échec de l'envoi du SMS à : " + student.getNumeroTelephone());
            e.printStackTrace();
        }
    }

    public void payerRepas(Long carteId, List<Long> repasIds, TypePaiement typePaiement) {
        CarteEtudiant carteEtudiant = carteEtudiantRepository.findById(Math.toIntExact(carteId))
                .orElseThrow(() -> new IllegalArgumentException("Carte étudiant introuvable."));

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

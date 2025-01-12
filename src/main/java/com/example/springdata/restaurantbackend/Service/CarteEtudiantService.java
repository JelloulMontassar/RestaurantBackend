package com.example.springdata.restaurantbackend.Service;

import com.sinch.sdk.domains.sms.BatchesService;
import com.sinch.sdk.domains.sms.SMSService;
import com.sinch.sdk.domains.sms.models.BatchText;
import com.sinch.sdk.domains.sms.models.requests.SendSmsBatchTextRequest;
import com.example.springdata.restaurantbackend.DTO.CarteEtudiantDTO;
import com.example.springdata.restaurantbackend.DTO.UtilisateurDTO;
import com.example.springdata.restaurantbackend.Entity.CarteEtudiant;
import com.example.springdata.restaurantbackend.Entity.HistoriqueRecharge;
import com.example.springdata.restaurantbackend.Entity.Utilisateur;
import com.example.springdata.restaurantbackend.Enums.StatutCarte;
import com.example.springdata.restaurantbackend.Mapper.CarteEtudiantMapper;
import com.example.springdata.restaurantbackend.Repository.CarteEtudiantRepository;
import com.example.springdata.restaurantbackend.Repository.HistoriqueRechargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    private final SMSService smsService;
    @Autowired
    private JavaMailSender javaMailSender;

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("choeurproject@gmail.com");
        javaMailSender.send(message);
    }
    public CarteEtudiantService(SMSService smsService) {
        this.smsService = smsService;
    }


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
            carteEtudiant.setSolde(solde + carteEtudiant.getSolde());
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
    // Scheduler method to check balances and send SMS
    @Transactional
    @Scheduled(fixedDelay = 3600000) // Executes every hour
    public void checkAndNotifyLowBalance() {
        List<CarteEtudiant> lowBalanceCards = carteEtudiantRepository.findAll()
                .stream()
                .filter(carte -> carte.getSolde() < 10)
                .collect(Collectors.toList());

        if (lowBalanceCards.isEmpty()) {
            System.out.println("No cards with low balance found.");
            return;
        }

        System.out.println("Found " + lowBalanceCards.size() + " cards with low balance.");
        for (CarteEtudiant carte : lowBalanceCards) {
            Utilisateur student = carte.getEtudiant();
            sendSmsNotification(student.getNumeroTelephone(),student.getEmail(), smsService);
        }
    }

    private void sendSmsNotification(String phoneNumber, String email, SMSService smsService) {
        String body = "This is a test SMS message using the Sinch Java SDK.";
        String fromNumber = "+447418629291";
        BatchesService batchesService = smsService.batches();


        String emailSubject = "Alerte Solde de Carte Étudiant";
        String emailBody = String.format(
                "Bonjour,\n\nVotre solde de carte étudiant est faible."
        );

        try {
            sendEmail(email, emailSubject, emailBody);
            System.out.println("Email sent successfully to: " + email);
        } catch (Exception e) {
            System.err.println("Failed to send email to: " + email);
            e.printStackTrace();
        }
    }

}

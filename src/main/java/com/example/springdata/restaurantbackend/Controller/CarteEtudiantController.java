package com.example.springdata.restaurantbackend.Controller;

import com.example.springdata.restaurantbackend.DTO.CarteEtudiantDTO;
import com.example.springdata.restaurantbackend.DTO.UtilisateurDTO;
import com.example.springdata.restaurantbackend.Entity.HistoriqueRecharge;
import com.example.springdata.restaurantbackend.Entity.Utilisateur;
import com.example.springdata.restaurantbackend.Requests.RecuPaiementRequest;
import com.example.springdata.restaurantbackend.Service.CarteEtudiantService;
import com.example.springdata.restaurantbackend.Service.UtilisateurService;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cartes-etudiants")
public class CarteEtudiantController {

    @Autowired
    private CarteEtudiantService carteEtudiantService;
    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<CarteEtudiantDTO>> getAllCartesEtudiants() {
        return ResponseEntity.ok(carteEtudiantService.getAllCartesEtudiants());
    }
    @GetMapping("/getCarteEtudiantByUtilisateur")
    public ResponseEntity<CarteEtudiantDTO> getCarteEtudiantByUtilisateurId(Authentication authentication) {
        UtilisateurDTO utilisateurDTO = utilisateurService.getUserByEmail(authentication.getName());
        Long utilisateurId = utilisateurDTO.getId();
        Utilisateur user = utilisateurService.getUserById(utilisateurId);
        return ResponseEntity.ok(carteEtudiantService.getCarteEtudiantByUser(user));
    }
    @PostMapping("/ajouter")
    public ResponseEntity<CarteEtudiantDTO> saveCarteEtudiant(@RequestBody CarteEtudiantDTO carteEtudiantDTO) {
        return ResponseEntity.ok(carteEtudiantService.saveCarteEtudiant(carteEtudiantDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarteEtudiantDTO> getCarteEtudiantById(@PathVariable Long id) {
        return ResponseEntity.ok(carteEtudiantService.getCarteEtudiantById(id));
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<Void> deleteCarteEtudiant(@PathVariable Long id) {
        carteEtudiantService.deleteCarteEtudiant(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recharger/{id}")
    public ResponseEntity<CarteEtudiantDTO> updateSolde(@PathVariable Long id, @RequestParam double solde) {
        return ResponseEntity.ok(carteEtudiantService.updateSolde(id, solde));
    }

    @PostMapping("/bloquer/{id}")
    public ResponseEntity<CarteEtudiantDTO> blockCarteEtudiant(@PathVariable Long id) {
        return ResponseEntity.ok(carteEtudiantService.blockCarteEtudiant(id));
    }

    @GetMapping("/historique-recharge/{id}")
    public ResponseEntity<List<HistoriqueRecharge>> getHistoriqueRecharge(@PathVariable Long id) {
        return ResponseEntity.ok(carteEtudiantService.getHistoriqueRecharge(id));
    }

    @PostMapping("/recu-paiement")
    public String genererRecuPaiement(@RequestBody RecuPaiementRequest request) {
        try {
            LocalDate parsedDate = LocalDate.parse(request.getDatePaiement());
            return carteEtudiantService.genererRecuPaiement(request.getUtilisateurId(), parsedDate);
        } catch (IllegalArgumentException ex) {
            return "Erreur : " + ex.getMessage();
        }
    }
}

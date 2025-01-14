package com.example.springdata.restaurantbackend.Controller;

import com.example.springdata.restaurantbackend.DTO.CarteEtudiantDTO;
import com.example.springdata.restaurantbackend.DTO.RepasDTO;
import com.example.springdata.restaurantbackend.DTO.UtilisateurDTO;
import com.example.springdata.restaurantbackend.Entity.Utilisateur;
import com.example.springdata.restaurantbackend.Enums.TypeRepas;
import com.example.springdata.restaurantbackend.Requests.PayerRepasRequest;
import com.example.springdata.restaurantbackend.Service.CarteEtudiantService;
import com.example.springdata.restaurantbackend.Service.MenuService;
import com.example.springdata.restaurantbackend.Service.RepasService;
import com.example.springdata.restaurantbackend.Service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/repas")
@Validated
public class RepasController {

    @Autowired
    private RepasService repasService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private CarteEtudiantService carteEtudiantService;
    @Autowired
    private UtilisateurService utilisateurService;

    // Récupérer tous les repas
    @GetMapping
    public List<RepasDTO> getAllRepas() {
        return repasService.getAllRepas();
    }

    // Récupérer un repas par ID
    @GetMapping("/{id}")
    public ResponseEntity<RepasDTO> getRepasById(@PathVariable Long id) {
        RepasDTO repasDTO = repasService.getRepasById(id);
        if (repasDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(repasDTO);
    }

    // Ajouter un nouveau repas
    @PostMapping("/ajouter")
    public ResponseEntity<RepasDTO> createRepas( @RequestBody RepasDTO repasDTO) {
        RepasDTO savedRepas = repasService.saveRepas(repasDTO);
        return ResponseEntity.ok(savedRepas);
    }

    // Mettre à jour un repas existant
    @PutMapping("/{id}")
    public ResponseEntity<RepasDTO> updateRepas(@PathVariable Long id, @RequestBody RepasDTO repasDTO) {
        RepasDTO updatedRepas = repasService.updateRepas(id, repasDTO);
        if (updatedRepas == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRepas);
    }

    // Supprimer un repas
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepas(@PathVariable Long id) {
        RepasDTO existingRepas = repasService.getRepasById(id);
        if (existingRepas == null) {
            return ResponseEntity.notFound().build();
        }
        repasService.deleteRepas(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint pour calculer le prix total
    @GetMapping("/{id}/prix-total")
    public ResponseEntity<Double> getPrixTotal(@PathVariable Long id) {
        double prixTotal = repasService.calculerPrixTotalRepas(Collections.singletonList(id));
        return ResponseEntity.ok(prixTotal);
    }

    // Effectuer le paiement des repas
    @PostMapping("/payer")
    public ResponseEntity<?> payerRepas(@RequestBody PayerRepasRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            UtilisateurDTO utilisateur = utilisateurService.getUserByEmail(username);
            Utilisateur user = utilisateurService.getUserById(utilisateur.getId());
            CarteEtudiantDTO carteEtudiantDTO = carteEtudiantService.getCarteEtudiantByUser(user);
            request.setCarteId(carteEtudiantDTO.getId());
            carteEtudiantService.payerRepas(request.getCarteId(), request.getRepasIds(), request.getTypePaiement());
            CarteEtudiantDTO carte = carteEtudiantService.getCarteEtudiantById(request.getCarteId());
            List<RepasDTO> repasPayes = request.getRepasIds().stream()
                    .map(repasService::getRepasById)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "message", "Paiement effectué avec succès.",
                    "solde_restant", carte.getSolde(),
                    "repas_payes", repasPayes
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Une erreur s'est produite lors du paiement."));
        }
    }


    //endpoint pour filtrer les repas
    @GetMapping("/type")
    public List<RepasDTO> getRepasByType(@RequestParam TypeRepas type) {
        return repasService.getRepasByType(type);
    }
}

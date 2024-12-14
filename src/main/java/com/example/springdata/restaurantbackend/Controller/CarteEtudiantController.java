package com.example.springdata.restaurantbackend.Controller;

import com.example.springdata.restaurantbackend.DTO.CarteEtudiantDTO;
import com.example.springdata.restaurantbackend.DTO.UtilisateurDTO;
import com.example.springdata.restaurantbackend.Entity.HistoriqueRecharge;
import com.example.springdata.restaurantbackend.Entity.Utilisateur;
import com.example.springdata.restaurantbackend.Service.CarteEtudiantService;
import com.example.springdata.restaurantbackend.Service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

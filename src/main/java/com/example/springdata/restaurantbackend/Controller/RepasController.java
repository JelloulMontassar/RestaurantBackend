package com.example.springdata.restaurantbackend.Controller;

import com.example.springdata.restaurantbackend.DTO.RepasDTO;
import com.example.springdata.restaurantbackend.Service.RepasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repas")
@Validated
public class RepasController {

    @Autowired
    private RepasService repasService;

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
    public ResponseEntity<RepasDTO> createRepas(@RequestBody RepasDTO repasDTO) {
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
        double prixTotal = repasService.calculerPrixTotalRepas(id);
        return ResponseEntity.ok(prixTotal);
    }

}

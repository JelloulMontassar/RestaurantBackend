package com.example.springdata.restaurantbackend.Controller;

import com.example.springdata.restaurantbackend.DTO.IngredientDTO;
import com.example.springdata.restaurantbackend.Service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    // Récupérer tous les ingrédients
    @GetMapping
    public List<IngredientDTO> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    // Récupérer un ingrédient par ID
    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getIngredientById(@PathVariable Long id) {
        IngredientDTO ingredientDTO = ingredientService.getIngredientById(id);
        if (ingredientDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ingredientDTO);
    }

    @PostMapping("/ajouter")
    public ResponseEntity<?> createIngredient(@RequestBody IngredientDTO ingredientDTO) {
        if (ingredientDTO.getPrix() <= 0) {
            return ResponseEntity.badRequest().body("Le prix doit être supérieur à 0.");
        }
        try {
            IngredientDTO savedIngredient = ingredientService.saveIngredient(ingredientDTO);
            return ResponseEntity.ok(savedIngredient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'ajout de l'ingrédient.");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<IngredientDTO> updateIngredient(
            @PathVariable Long id,
            @RequestBody IngredientDTO ingredientDTO) {
       // System.out.println("Received DTO: " + ingredientDTO);
        if (ingredientDTO.getPrix() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        IngredientDTO updatedIngredient = ingredientService.updateIngredient(id, ingredientDTO);
        if (updatedIngredient == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedIngredient);
    }

    // Supprimer un ingrédient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        IngredientDTO existingIngredient = ingredientService.getIngredientById(id);
        if (existingIngredient == null) {
            return ResponseEntity.notFound().build();
        }
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}

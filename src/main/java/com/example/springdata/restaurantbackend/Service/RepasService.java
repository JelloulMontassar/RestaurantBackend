package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.DTO.RepasDTO;
import com.example.springdata.restaurantbackend.Entity.Ingredient;
import com.example.springdata.restaurantbackend.Entity.Repas;
import com.example.springdata.restaurantbackend.Mapper.RepasMapper;
import com.example.springdata.restaurantbackend.Repository.IngredientRepository;
import com.example.springdata.restaurantbackend.Repository.RepasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RepasService {

    @Autowired
    private RepasRepository repasRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    public List<RepasDTO> getAllRepas() {
        return repasRepository.findAll()
                .stream()
                .map(RepasMapper::toDTO)
                .toList();
    }

    public RepasDTO getRepasById(Long id) {
        return RepasMapper.toDTO(repasRepository.findById(id).orElse(null));
    }

    @Transactional
    public RepasDTO saveRepas(RepasDTO repasDTO) {
        Repas repas = RepasMapper.toEntity(repasDTO);

        // Mise à jour des ingrédients et validation des quantités disponibles
        repas.setIngredients(repas.getIngredients().stream()
                .map(ingredient -> {
                    Ingredient existingIngredient = ingredientRepository.findById(ingredient.getId())
                            .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredient.getId()));

                    // Vérifier si la quantité demandée est disponible
                    if (existingIngredient.getQuantite() < ingredient.getQuantite()) {
                        throw new RuntimeException("Quantité insuffisante pour l'ingrédient : " + existingIngredient.getNom() +
                                ". Disponible : " + existingIngredient.getQuantite() + ", demandé : " + ingredient.getQuantite());
                    }

                    // Réduire la quantité disponible dans la base
                    existingIngredient.setQuantite(existingIngredient.getQuantite() - ingredient.getQuantite());

                    // Associer le prix de l'ingrédient existant
                    ingredient.setPrix(existingIngredient.getPrix());

                    // Rétablir la quantité choisie pour le calcul du prix total
                    ingredient.setQuantite(ingredient.getQuantite());

                    // Sauvegarder les modifications
                    ingredientRepository.save(existingIngredient);

                    return ingredient;
                })
                .collect(Collectors.toList()));

        // Calcul du prix total basé sur les quantités choisies
        repas.calculerPrixTotal();

        // Sauvegarder le repas et retourner le DTO
        return RepasMapper.toDTO(repasRepository.save(repas));
    }



    private Ingredient updateIngredientQuantity(Ingredient requestedIngredient) {
        Ingredient existingIngredient = ingredientRepository.findById(requestedIngredient.getId())
                .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé : " + requestedIngredient.getId()));

        if (existingIngredient.getQuantite() < requestedIngredient.getQuantite()) {
            throw new IllegalArgumentException(
                    "Quantité insuffisante pour l'ingrédient : " + existingIngredient.getNom() +
                            ". Disponible : " + existingIngredient.getQuantite() + ", demandé : " + requestedIngredient.getQuantite());
        }

        // Réduire la quantité disponible
        existingIngredient.setQuantite(existingIngredient.getQuantite() - requestedIngredient.getQuantite());

        // Retourner l'ingrédient mis à jour sans sauvegarder immédiatement
        return existingIngredient;
    }




    public void deleteRepas(Long id) {
        repasRepository.deleteById(id);
    }

    public RepasDTO updateRepas(Long id, RepasDTO repasDTO) {
        Repas existingRepas = repasRepository.findById(id).orElse(null);
        if (existingRepas != null) {
            existingRepas.setNom(repasDTO.getNom());
            existingRepas.setType(repasDTO.getType());

            List<Ingredient> updatedIngredients = repasDTO.getIngredients().stream()
                    .map(ingredientDTO -> ingredientRepository.findById(ingredientDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientDTO.getId())))
                    .collect(Collectors.toList());

            existingRepas.getIngredients().clear();
            existingRepas.getIngredients().addAll(updatedIngredients);
            existingRepas.calculerPrixTotal(); // Calcul automatique du prix total

            // Mise à jour des quantités dans la base de données après mise à jour du repas
            updateIngredientQuantities(existingRepas);

            return RepasMapper.toDTO(repasRepository.save(existingRepas));
        }
        return null;
    }

    public double calculerPrixTotalRepas(Long repasId) {
        // Récupérer le repas par ID
        Repas repas = repasRepository.findById(repasId)
                .orElseThrow(() -> new RuntimeException("Repas introuvable"));

        // Calculer le prix total (quantité choisie * prix unitaire pour chaque ingrédient)
        return repas.getIngredients().stream()
                .mapToDouble(ingredient -> ingredient.getQuantite() * ingredient.getPrix())
                .sum();
    }

    private void validateIngredientQuantities(Repas repas) {
        for (Ingredient ingredient : repas.getIngredients()) {
            // Récupérer l'ingrédient dans la base de données
            Ingredient existingIngredient = ingredientRepository.findById(ingredient.getId())
                    .orElseThrow(() -> new RuntimeException("Ingrédient introuvable : " + ingredient.getId()));

            // Vérifier si la quantité demandée dépasse celle disponible
            if (ingredient.getQuantite() > existingIngredient.getQuantite()) {
                throw new RuntimeException("Quantité insuffisante pour l'ingrédient : " + existingIngredient.getNom() +
                        ". Disponible : " + existingIngredient.getQuantite() + ", demandé : " + ingredient.getQuantite());
            }
        }
    }

    private void updateIngredientQuantities(Repas repas) {
        for (Ingredient ingredient : repas.getIngredients()) {
            // Récupérer l'ingrédient dans la base de données
            Ingredient existingIngredient = ingredientRepository.findById(ingredient.getId())
                    .orElseThrow(() -> new RuntimeException("Ingrédient introuvable : " + ingredient.getId()));

            // Réduire la quantité de l'ingrédient dans la base de données après consommation
            existingIngredient.setQuantite(existingIngredient.getQuantite() - ingredient.getQuantite());

            // Sauvegarder la mise à jour de la quantité dans la base de données
            ingredientRepository.save(existingIngredient);
        }
    }
}


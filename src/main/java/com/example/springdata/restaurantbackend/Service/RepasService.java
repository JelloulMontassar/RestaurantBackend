package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.DTO.IngredientDTO;
import com.example.springdata.restaurantbackend.DTO.RepasDTO;
import com.example.springdata.restaurantbackend.Entity.Ingredient;
import com.example.springdata.restaurantbackend.Entity.Repas;
import com.example.springdata.restaurantbackend.Enums.TypeRepas;
import com.example.springdata.restaurantbackend.Mapper.RepasMapper;
import com.example.springdata.restaurantbackend.Repository.IngredientRepository;
import com.example.springdata.restaurantbackend.Repository.RepasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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


    public RepasDTO saveRepas(RepasDTO repasDTO) {
        // Conteneur mutable pour le prix total
        final double[] prixTotal = {0.0};

        // Créer un nouvel objet Repas
        Repas repas = new Repas();
        repas.setNom(repasDTO.getNom());
        repas.setType(repasDTO.getType());

        List<Ingredient> updatedIngredients = repasDTO.getIngredients().stream().map(ingredientDTO -> {
            // Récupérer l'ingrédient existant dans la base de données
            Ingredient existingIngredient = ingredientRepository.findById(ingredientDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Ingrédient non trouvé : " + ingredientDTO.getId()));
            System.out.println(existingIngredient.getNom()+" "+existingIngredient.getPrix());
            // Vérifier la quantité disponible
            double quantiteRestante = existingIngredient.getQuantite() - ingredientDTO.getQuantite();
            if (quantiteRestante < 0) {
                throw new RuntimeException("Quantité insuffisante pour l'ingrédient : " + existingIngredient.getNom());
            }

            // Mettre à jour la quantité restante de l'ingrédient
            existingIngredient.setQuantite(quantiteRestante);
            ingredientRepository.save(existingIngredient); // Sauvegarder la mise à jour dans la base de données
            System.out.println("Saved ! 1");
            if (existingIngredient.getPrix() == 0.0) {
                throw new RuntimeException("Prix de l'ingrédient non défini pour : " + existingIngredient.getNom());
            }

            // Créer un nouvel objet Ingredient pour le repas (sans le sauvegarder directement)
            Ingredient repasIngredient = new Ingredient();
            repasIngredient.setId(existingIngredient.getId());
            repasIngredient.setNom(existingIngredient.getNom());
            repasIngredient.setSeuil(existingIngredient.getSeuil());
            repasIngredient.setPrix(existingIngredient.getPrix()); // Assurez-vous que le prix est défini
            repasIngredient.setQuantite(ingredientDTO.getQuantite()); // Quantité utilisée
            repasIngredient.setQuantiteRestante(quantiteRestante);   // Nouvelle quantité restante


            // Ajouter au prix total
            prixTotal[0] += ingredientDTO.getQuantite() * existingIngredient.getPrix();

            return repasIngredient;
        }).collect(Collectors.toList());

        // Associer les ingrédients au repas
        repas.setIngredients(updatedIngredients);
        System.out.println(updatedIngredients);
        repas.setPrixTotal(prixTotal[0]); // Mettre à jour le prix total
        System.out.println(repas.toString());
        // Sauvegarder le repas
        System.out.println(repas.getIngredients().get(0).getPrix());
        System.out.println(repas.getPrixTotal());
        Repas savedRepas = repasRepository.save(repas);
        System.out.println("Saved ! 2");
        // Mapper vers le DTO
        RepasDTO savedRepasDTO = RepasMapper.toDTO(savedRepas);

        // Ajouter les quantités restantes des ingrédients au DTO
        savedRepasDTO.getIngredients().forEach(ingredientDTO -> {
            Ingredient matchingIngredient = updatedIngredients.stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientDTO.getId()))
                    .findFirst()
                    .orElse(null);

            if (matchingIngredient != null) {
                System.out.println(matchingIngredient.getPrix());
                ingredientDTO.setQuantiteRestante(matchingIngredient.getQuantiteRestante());
            }
        });

        return savedRepasDTO;
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

    public double calculerPrixTotalRepas(List<Long> repasIds) {
        // Récupérer le repas par ID
        /*Repas repas = repasRepository.findById(repasId)
                .orElseThrow(() -> new RuntimeException("Repas introuvable"));

        // Calculer le prix total (quantité choisie * prix unitaire pour chaque ingrédient)
        return repas.getIngredients().stream()
                .mapToDouble(ingredient -> ingredient.getQuantite() * ingredient.getPrix())
                .sum();*/
        System.out.println(repasIds);
        List<Repas> repasList = repasRepository.findAllById(repasIds);
        if (repasList.size() != repasIds.size()) {
            throw new RuntimeException("Certains repas sont introuvables.");
        }
        return repasList.stream()
                .mapToDouble(repas -> repas.getIngredients().stream()
                        .mapToDouble(ingredient -> ingredient.getQuantite() * ingredient.getPrix())
                        .sum())
                .sum();
    }

    public void deleteRepas(Long id) {
        repasRepository.deleteById(id);
    }

    // Méthodes privées pour gérer la mise à jour des quantités d'ingrédients
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

    private void validateIngredientQuantities(RepasDTO repasDTO) {
        for (IngredientDTO ingredientDTO : repasDTO.getIngredients()) {
            // Convertir IngredientDTO en Ingredient pour accéder à la base de données
            Ingredient existingIngredient = ingredientRepository.findById(ingredientDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Ingrédient introuvable : " + ingredientDTO.getId()));

            // Vérifier si la quantité demandée dépasse celle disponible
            if (ingredientDTO.getQuantite() > existingIngredient.getQuantite()) {
                throw new RuntimeException("Quantité insuffisante pour l'ingrédient : " + existingIngredient.getNom() +
                        ". Disponible : " + existingIngredient.getQuantite() + ", demandé : " + ingredientDTO.getQuantite());
            }
        }
    }

    public List<RepasDTO> getRepasByType(TypeRepas type) {
        return repasRepository.findByType(type)
                .stream()
                .map(RepasMapper::toDTO)
                .collect(Collectors.toList());
    }
}

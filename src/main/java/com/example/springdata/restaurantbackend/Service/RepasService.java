package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.DTO.RepasDTO;
import com.example.springdata.restaurantbackend.Entity.Ingredient;
import com.example.springdata.restaurantbackend.Entity.Repas;
import com.example.springdata.restaurantbackend.Mapper.RepasMapper;
import com.example.springdata.restaurantbackend.Repository.IngredientRepository;
import com.example.springdata.restaurantbackend.Repository.RepasRepository;
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
        Repas repas = RepasMapper.toEntity(repasDTO);
        repas.setIngredients(repas.getIngredients().stream()
                .map(ingredient -> ingredientRepository.findById(ingredient.getId())
                        .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredient.getId())))
                .collect(Collectors.toList()));
        repas.calculerPrixTotal(); // Calcul automatique
        return RepasMapper.toDTO(repasRepository.save(repas));
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
            existingRepas.calculerPrixTotal(); // Calcul automatique
            return RepasMapper.toDTO(repasRepository.save(existingRepas));
        }
        return null;
    }

    public double calculerPrixTotalRepas(Long repasId) {
        // Récupérer le repas par ID
        Repas repas = repasRepository.findById(repasId)
                .orElseThrow(() -> new RuntimeException("Repas introuvable"));
        // Calculer le prix total
        return repas.getIngredients().stream()
                .mapToDouble(ingredient -> ingredient.getQuantite() * ingredient.getPrix())
                .sum();
    }

}

package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.DTO.IngredientDTO;
import com.example.springdata.restaurantbackend.Entity.Ingredient;
import com.example.springdata.restaurantbackend.Mapper.IngredientMapper;
import com.example.springdata.restaurantbackend.Repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    public List<IngredientDTO> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        System.out.println("Entities from DB: " + ingredients);
        return ingredients.stream()
                .map(IngredientMapper::toDTO)
                .peek(dto -> System.out.println("Mapped DTO: " + dto))
                .collect(Collectors.toList());
    }


    public IngredientDTO getIngredientById(Long id) {
        return IngredientMapper.toDTO(ingredientRepository.findById(id).orElse(null));
    }

    public IngredientDTO saveIngredient(IngredientDTO ingredientDTO) {
        Ingredient ingredient = IngredientMapper.toEntity(ingredientDTO);
        System.out.println("Entity before save: " + ingredient);
        return IngredientMapper.toDTO(ingredientRepository.save(ingredient));
    }

    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }

    public IngredientDTO updateIngredient(Long id, IngredientDTO ingredientDTO) {
        Ingredient existingIngredient = ingredientRepository.findById(id).orElse(null);
        if (existingIngredient != null) {
            existingIngredient.setNom(ingredientDTO.getNom());
            existingIngredient.setQuantite(ingredientDTO.getQuantite());
            existingIngredient.setSeuil(ingredientDTO.getSeuil());
            existingIngredient.setPrix(ingredientDTO.getPrix());
            existingIngredient.setUpdatedAt(LocalDateTime.now()); // Mise à jour du champ updatedAt
            return IngredientMapper.toDTO(ingredientRepository.save(existingIngredient));
        }
        return null;
    }

}

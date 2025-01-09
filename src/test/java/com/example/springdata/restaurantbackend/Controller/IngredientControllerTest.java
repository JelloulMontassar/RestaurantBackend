package com.example.springdata.restaurantbackend.Controller;

import com.example.springdata.restaurantbackend.DTO.IngredientDTO;
import com.example.springdata.restaurantbackend.Service.IngredientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IngredientControllerTest {

    @InjectMocks
    private IngredientController ingredientController;

    @Mock
    private IngredientService ingredientService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    void testGetAllIngredients_Success() throws Exception {
        IngredientDTO ingredient1 = new IngredientDTO(1L, "Tomate", 50.0, 10.0, 3.0, LocalDateTime.now(), 40.0);
        IngredientDTO ingredient2 = new IngredientDTO(2L, "Oignon", 30.0, 5.0, 2.0, LocalDateTime.now(), 25.0);

        List<IngredientDTO> ingredients = Arrays.asList(ingredient1, ingredient2);

        when(ingredientService.getAllIngredients()).thenReturn(ingredients);

        mockMvc.perform(get("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom", is("Tomate")))
                .andExpect(jsonPath("$[1].nom", is("Oignon")));
    }

    @Test
    void testGetIngredientById_Success() throws Exception {
        IngredientDTO ingredient = new IngredientDTO(1L, "Tomate", 50.0, 10.0, 3.0, LocalDateTime.now(), 40.0);

        when(ingredientService.getIngredientById(1L)).thenReturn(ingredient);

        mockMvc.perform(get("/api/ingredients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", is("Tomate")))
                .andExpect(jsonPath("$.quantite", is(50.0)));
    }

    @Test
    void testCreateIngredient_Success() throws Exception {
        IngredientDTO newIngredient = new IngredientDTO(null, "Carotte", 20.0, 5.0, 1.5, null, 15.0);
        IngredientDTO savedIngredient = new IngredientDTO(1L, "Carotte", 20.0, 5.0, 1.5, LocalDateTime.now(), 15.0);

        when(ingredientService.saveIngredient(any())).thenReturn(savedIngredient);

        mockMvc.perform(post("/api/ingredients/ajouter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Carotte\",\"quantite\":20,\"seuil\":5,\"prix\":1.5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Carotte")));
    }

    @Test
    void testUpdateIngredient_Success() throws Exception {
        IngredientDTO updatedIngredient = new IngredientDTO(1L, "Tomate", 40.0, 10.0, 2.5, LocalDateTime.now(), 35.0);

        when(ingredientService.updateIngredient(eq(1L), any())).thenReturn(updatedIngredient);

        mockMvc.perform(put("/api/ingredients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Tomate\",\"quantite\":40,\"seuil\":10,\"prix\":2.5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", is("Tomate")))
                .andExpect(jsonPath("$.quantite", is(40.0)));
    }

    @Test
    void testDeleteIngredient_Success() throws Exception {
        doNothing().when(ingredientService).deleteIngredient(1L);

        mockMvc.perform(delete("/api/ingredients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}

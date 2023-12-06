package com.abn.recipesmanager.boundary.controller;

import com.abn.recipesmanager.boundary.AbstractIntegrationTest;
import com.abn.recipesmanager.boundary.dto.RecipeDTO;
import com.abn.recipesmanager.domain.domainobject.Recipe;
import com.abn.recipesmanager.fixtures.RecipeTestFixtures;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RecipeControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Should create Recipe successfully and respond with a valid id")
    void shouldCreateRecipeSuccessfully() throws Exception {
        RecipeDTO dto = RecipeTestFixtures.getRecipeDTO(null);

        String responseString = mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RecipeDTO response = objectMapper.readValue(responseString, RecipeDTO.class);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo(dto.name());
        assertThat(response.servings()).isEqualTo(dto.servings());
        assertThat(response.vegetarian()).isEqualTo(dto.vegetarian());
        assertThat(response.ingredients()).isEqualTo(dto.ingredients());
        assertThat(response.instructions()).isEqualTo(dto.instructions());
    }

    @Test
    @DisplayName("Should fetch all Recipes filtering successfully")
    void shouldFetchAllRecipesFilteringSuccessfully() throws Exception {
        Recipe recipeToPersist = RecipeTestFixtures.getRecipe("name");
        repository.save(recipeToPersist);

        String[] ninIngredients = {"beef", "sugar"};

        String responseString = mockMvc.perform((get("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ninIngredients", ninIngredients[0], ninIngredients[1])
                        .param("name", recipeToPersist.name())
                        .param("servings", recipeToPersist.servings().toString())
                        .param("vegetarian", recipeToPersist.vegetarian().toString())
                        .param("ingredients", recipeToPersist.ingredients().get(0).name(), recipeToPersist.ingredients().get(1).name())
                        .param("instructions", recipeToPersist.instructions())
                ))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RecipeDTO> response = objectMapper.readValue(responseString, new TypeReference<List<RecipeDTO>>() {
        });

        assertThat(response).isNotEmpty();
        assertThat(response.get(0).name()).isEqualTo(recipeToPersist.name());
        assertThat(response.get(0).servings()).isEqualTo(recipeToPersist.servings());
        assertThat(response.get(0).vegetarian()).isEqualTo(recipeToPersist.vegetarian());
        assertThat(response.get(0).ingredients()).isEqualTo(recipeToPersist.ingredients());
        assertThat(response.get(0).instructions()).isEqualTo(recipeToPersist.instructions());
    }

    @Test
    @DisplayName("Should not fetch all Recipes filtering when parameter value is wrong")
    void shouldNotFetchAllRecipesFilteringWhenParameterValueIsWrong() throws Exception {
        Recipe recipeToPersist = RecipeTestFixtures.getRecipe("name");
        repository.save(recipeToPersist);

        String[] ninIngredients = {"beef", "sugar"};

        String responseString = mockMvc.perform((get("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ninIngredients", ninIngredients[0], ninIngredients[1])
                        .param("name", recipeToPersist.name() + "wrongName")
                        .param("servings", recipeToPersist.servings().toString())
                        .param("vegetarian", recipeToPersist.vegetarian().toString())
                        .param("ingredients", recipeToPersist.ingredients().get(0).name(), recipeToPersist.ingredients().get(1).name())
                        .param("instructions", recipeToPersist.instructions())
                ))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RecipeDTO> response = objectMapper.readValue(responseString, new TypeReference<List<RecipeDTO>>() {
        });

        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("Should not fetch all Recipes filtering when parameter value is wrong")
    void shouldReturn400WhenServingsIsNotANumber() throws Exception {
        mockMvc.perform((get("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("servings", "test")

                ))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should update Recipe successfully")
    void shouldUpdateRecipeSuccessfully() throws Exception {
        RecipeDTO dto = RecipeTestFixtures.getRecipeDTO(null);
        Recipe persistedRecipe = RecipeTestFixtures.getRecipe("testName");
        String persistedId = repository.save(persistedRecipe).id();

        String responseString = mockMvc.perform(put("/recipes/" + persistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RecipeDTO response = objectMapper.readValue(responseString, RecipeDTO.class);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(persistedId);
        assertThat(response.name()).isEqualTo(dto.name());
        assertThat(response.servings()).isEqualTo(dto.servings());
        assertThat(response.vegetarian()).isEqualTo(dto.vegetarian());
        assertThat(response.ingredients()).isEqualTo(dto.ingredients());
        assertThat(response.instructions()).isEqualTo(dto.instructions());
    }

    @Test
    @DisplayName("Should return NotFound when updating Recipe with invalid id successfully")
    void shouldReturnNotFoundWhenUpdatingRecipeWithInvalidId() throws Exception {
        RecipeDTO dto = RecipeTestFixtures.getRecipeDTO(null);

        mockMvc.perform(put("/recipes/" + "invalidId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should get Recipe by id successfully")
    void shouldGetRecipeByIdSuccessfully() throws Exception {
        Recipe recipe = RecipeTestFixtures.getRecipe("test");
        String id = repository.save(recipe).id();

        String responseString = mockMvc.perform(get("/recipes/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RecipeDTO response = objectMapper.readValue(responseString, RecipeDTO.class);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo(recipe.name());
        assertThat(response.servings()).isEqualTo(recipe.servings());
        assertThat(response.vegetarian()).isEqualTo(recipe.vegetarian());
        assertThat(response.ingredients()).isEqualTo(recipe.ingredients());
        assertThat(response.instructions()).isEqualTo(recipe.instructions());
    }

    @Test
    @DisplayName("Should return NotFound when Recipe with id does not exist")
    void shouldReturnNotFoundWhenRecipeWithIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/recipes/" + "invalidId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete Recipe by id successfully when Recipe exists")
    void shouldDeleteRecipeByIdSuccessfullyWhenRecipeExists() throws Exception {
        Recipe recipe = RecipeTestFixtures.getRecipe("test");
        String id = repository.save(recipe).id();

        mockMvc.perform(delete("/recipes/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}

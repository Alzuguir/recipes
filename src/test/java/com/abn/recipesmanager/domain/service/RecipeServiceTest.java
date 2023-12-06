package com.abn.recipesmanager.domain.service;

import com.abn.recipesmanager.adapter.RecipeRepository;
import com.abn.recipesmanager.boundary.dto.RecipeDTO;
import com.abn.recipesmanager.domain.domainobject.Recipe;
import com.abn.recipesmanager.domain.exception.InvalidNumberParameterException;
import com.abn.recipesmanager.domain.exception.RecipeNotFoundException;
import com.abn.recipesmanager.fixtures.RecipeTestFixtures;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    private final RecipeRepository recipeRepository = mock();
    private final RecipeService recipeService = new RecipeService(recipeRepository);

    @Test
    void shouldSaveRecipeSuccessfully() {
        Recipe recipe = RecipeTestFixtures.getRecipe("test");

        when(recipeRepository.save(any())).thenReturn(recipe);

        Recipe result = recipeService.saveRecipe(new RecipeDTO(recipe));

        assertThat(result).isEqualTo(recipe);
    }

    @Test
    void shouldFetchFilteringAllMatchingRecipesSuccessfully() {
        Recipe recipe = RecipeTestFixtures.getRecipe("test");
        String[] ninIngredients = new String[]{"strawberry", "cream"};

        when(recipeRepository.findByIngredientNotPresent(
                ninIngredients,
                recipe.name(),
                recipe.servings(),
                recipe.vegetarian(),
                new String[]{recipe.ingredients().get(0).name(), recipe.ingredients().get(1).name()},
                recipe.instructions()
        )).thenReturn(List.of(recipe));

        List<RecipeDTO> result = recipeService.fetchAllRecipesFiltering(
                ninIngredients,
                recipe.name(),
                recipe.servings().toString(),
                recipe.vegetarian().toString(),
                new String[]{recipe.ingredients().get(0).name(), recipe.ingredients().get(1).name()},
                recipe.instructions()
        );

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isNotNull();
    }

    @Test
    void shouldMapParamNullWhenNumberParamIsNull() {
        Recipe recipe = RecipeTestFixtures.getRecipe("test");
        String[] ninIngredients = new String[]{"strawberry", "cream"};

        when(recipeRepository.findByIngredientNotPresent(
                ninIngredients,
                recipe.name(),
                null,
                recipe.vegetarian(),
                new String[]{recipe.ingredients().get(0).name(), recipe.ingredients().get(1).name()},
                recipe.instructions()
        )).thenReturn(List.of(recipe));

        List<RecipeDTO> result = recipeService.fetchAllRecipesFiltering(
                ninIngredients,
                recipe.name(),
                null,
                recipe.vegetarian().toString(),
                new String[]{recipe.ingredients().get(0).name(), recipe.ingredients().get(1).name()},
                recipe.instructions()
        );

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isNotNull();
    }

    @Test
    void shouldThrowInvalidNumberParameterExceptionWhenServingsIsNotANumber() {
        assertThrows(InvalidNumberParameterException.class, () ->
                recipeService.fetchAllRecipesFiltering(
                        null,
                        null,
                        "NAN",
                        null,
                        null,
                        null
                )
        );
    }

    @Test
    void shouldFindRecipeByIdSuccessfully() {
        Recipe recipe = RecipeTestFixtures.getRecipe("test");

        when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));

        RecipeDTO result = recipeService.findById(recipe.id());

        assertNotNull(result);
        assertThat(result.id()).isEqualTo(recipe.id());
        assertThat(result.servings()).isEqualTo(recipe.servings());
        assertThat(result.vegetarian()).isEqualTo(recipe.vegetarian());
        assertThat(result.ingredients()).isEqualTo(recipe.ingredients());
        assertThat(result.instructions()).isEqualTo(recipe.instructions());
    }

    @Test
    void shouldReturnNotFoundWhenFindByInvalidId() {
        when(recipeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.findById("test"));
    }

    @Test
    void shouldDeleteRecipeByIdSuccessfully() {
        String id = new ObjectId().toString();
        recipeService.deleteById(id);
        verify(recipeRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldUpdateRecipeSuccessfully() {
        String id = new ObjectId().toString();
        RecipeDTO dto = RecipeTestFixtures.getRecipeDTO(null);

        when(recipeRepository.findById(id)).thenReturn(Optional.of(new Recipe(dto)));
        when(recipeRepository.save(any())).thenReturn(new Recipe(dto));

        RecipeDTO result = recipeService.updateRecipe(id, dto);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(dto.name());
        assertThat(result.servings()).isEqualTo(dto.servings());
        assertThat(result.vegetarian()).isEqualTo(dto.vegetarian());
        assertThat(result.ingredients()).isEqualTo(dto.ingredients());
        assertThat(result.instructions()).isEqualTo(dto.instructions());
    }

    @Test
    void shouldThrowRecipeNotFoundExceptionWhenUpdateRecipeWithInvalidId() {
        String id = new ObjectId().toString();
        RecipeDTO dto = RecipeTestFixtures.getRecipeDTO(null);

        when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () ->
                recipeService.updateRecipe(id, dto)
        );
    }
}
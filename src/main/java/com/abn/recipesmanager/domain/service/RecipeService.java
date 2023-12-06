package com.abn.recipesmanager.domain.service;

import com.abn.recipesmanager.adapter.RecipeRepository;
import com.abn.recipesmanager.boundary.dto.RecipeDTO;
import com.abn.recipesmanager.domain.exception.InvalidNumberParameterException;
import com.abn.recipesmanager.domain.exception.RecipeNotFoundException;
import com.abn.recipesmanager.domain.domainobject.Recipe;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe saveRecipe(RecipeDTO dto) {
        return recipeRepository.save(new Recipe(dto));
    }

    public List<RecipeDTO> fetchAllRecipesFiltering(
            String[] ninIngredients,
            String name,
            String servings,
            String vegetarian,
            String[] ingredients,
            String instructions
    ) {
        return recipeRepository.findByIngredientNotPresent(
                        ninIngredients,
                        name,
                        mapIntegerParam(servings),
                        vegetarian != null ? Boolean.valueOf(vegetarian) : null,
                        ingredients,
                        instructions
                )
                .stream()
                .map(RecipeDTO::new)
                .toList();
    }

    private Integer mapIntegerParam(String param) {
        if (param != null) {
            try {
                return Integer.valueOf(param);
            } catch (NumberFormatException e) {
                throw new InvalidNumberParameterException(param);
            }
        } else {
            return null;
        }
    }

    public RecipeDTO findById(String id) {
        return new RecipeDTO(
                recipeRepository.findById(id)
                        .orElseThrow(() -> new RecipeNotFoundException(id))
        );
    }

    public void deleteById(String id) {
        recipeRepository.deleteById(id);
    }

    public RecipeDTO updateRecipe(String id, RecipeDTO dto) {
        var recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));

        return new RecipeDTO(recipeRepository.save(getUpdatedRecipe(dto, recipe)));
    }

    private Recipe getUpdatedRecipe(RecipeDTO dto, Recipe recipe) {
        return new Recipe(
                recipe.id(),
                dto.name() != null ? dto.name() : recipe.name(),
                dto.servings() != null ? dto.servings() : recipe.servings(),
                dto.vegetarian() != null ? dto.vegetarian() : recipe.vegetarian(),
                dto.ingredients() != null ? dto.ingredients() : recipe.ingredients(),
                dto.instructions() != null ? dto.instructions() : recipe.instructions()
        );
    }
}

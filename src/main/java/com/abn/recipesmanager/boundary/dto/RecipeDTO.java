package com.abn.recipesmanager.boundary.dto;

import com.abn.recipesmanager.domain.domainobject.Ingredient;
import com.abn.recipesmanager.domain.domainobject.Recipe;

import java.util.List;

public record RecipeDTO(
        String id,
        String name,
        Integer servings,
        Boolean vegetarian,
        List<Ingredient> ingredients,
        String instructions
) {
    public RecipeDTO(Recipe recipe) {
        this(
                recipe.id(),
                recipe.name(),
                recipe.servings(),
                recipe.vegetarian(),
                recipe.ingredients(),
                recipe.instructions()
        );
    }
}

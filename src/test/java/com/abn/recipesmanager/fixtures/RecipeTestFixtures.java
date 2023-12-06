package com.abn.recipesmanager.fixtures;

import com.abn.recipesmanager.boundary.dto.RecipeDTO;
import com.abn.recipesmanager.domain.domainobject.Ingredient;
import com.abn.recipesmanager.domain.domainobject.Recipe;
import org.bson.types.ObjectId;

import java.util.List;

public class RecipeTestFixtures {

    public static RecipeDTO getRecipeDTO(String id){
        return new RecipeDTO(
                id,
                "test",
                2,
                false,
                List.of(new Ingredient("beef", "300g")),
                "BBQ for 15 minutes"
        );
    }

    public static Recipe getRecipe(String name) {
        return new Recipe(
                new ObjectId().toString(),
                name,
                2,
                false,
                List.of(new Ingredient("flour", "200 g"), new Ingredient("salt", "20 g")),
                "bake it for 20 minutes"
        );
    }
}

package com.abn.recipesmanager.domain.domainobject;

import com.abn.recipesmanager.boundary.dto.RecipeDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("recipes")
public record Recipe(
        @Id
        String id,
        String name,
        Integer servings,
        Boolean vegetarian,
        List<Ingredient> ingredients,
        String instructions
) {
        public Recipe(RecipeDTO dto){
                this(
                        null,
                        dto.name(),
                        dto.servings(),
                        dto.vegetarian(),
                        dto.ingredients(),
                        dto.instructions()
                );
        }
}

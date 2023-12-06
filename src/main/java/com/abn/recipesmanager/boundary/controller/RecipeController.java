package com.abn.recipesmanager.boundary.controller;

import com.abn.recipesmanager.boundary.dto.RecipeDTO;
import com.abn.recipesmanager.domain.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService){
        this.recipeService = recipeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDTO createRecipe(@RequestBody RecipeDTO recipe){
        return new RecipeDTO(recipeService.saveRecipe(recipe));
    }

    @GetMapping
    public List<RecipeDTO> getAllRecipesFiltering(
            @RequestParam(required = false) String[] ninIngredients,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String servings,
            @RequestParam(required = false) String vegetarian,
            @RequestParam(required = false) String[] ingredients,
            @RequestParam(required = false) String instructions
    ){
        return recipeService.fetchAllRecipesFiltering(ninIngredients, name, servings, vegetarian, ingredients, instructions);
    }

    @GetMapping("/{id}")
    public RecipeDTO getRecipeById(@PathVariable String id){
        return recipeService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipeById(@PathVariable String id){
        recipeService.deleteById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RecipeDTO putRecipe(@PathVariable String id, @RequestBody RecipeDTO dto){
        return recipeService.updateRecipe(id, dto);
    }
}

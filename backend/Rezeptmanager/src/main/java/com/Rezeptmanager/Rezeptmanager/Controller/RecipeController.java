package com.Rezeptmanager.Rezeptmanager.Controller;

import com.Rezeptmanager.Rezeptmanager.Model.Recipe;
import com.Rezeptmanager.Rezeptmanager.Service.RecipeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/save-full")
    public Recipe saveFullRecipe(@Valid @RequestBody Recipe recipe) {
        return recipeService.saveRecipe(recipe);
    }

    @GetMapping("/search")
    public List<Recipe> searchRecipes(@RequestParam(required = false) String query) {
        return recipeService.searchRecipes(query);
    }

    @GetMapping("/{apiId}")
    public Recipe getRecipeByApiId(@PathVariable @Min(1) Long apiId) {
        return recipeService.findRecipeByApiId(apiId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezept nicht gefunden"));
    }

    @DeleteMapping("/delete/{apiId}")
    public void deleteRecipe(@PathVariable @Min(1) Long apiId) {
        recipeService.deleteRecipeByApiId(apiId);
    }
}
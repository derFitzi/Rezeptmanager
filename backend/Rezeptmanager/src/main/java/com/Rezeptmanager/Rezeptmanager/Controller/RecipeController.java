package com.Rezeptmanager.Rezeptmanager.Controller;

import com.Rezeptmanager.Rezeptmanager.Model.Recipe;
import com.Rezeptmanager.Rezeptmanager.Service.RecipeService;
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
    public Recipe saveFullRecipe(@RequestBody Recipe recipe) {
        if (recipe.getTitle() == null || recipe.getImageUrl() == null || recipe.getInstructions() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fehlende Felder im Rezept");
        }
        return recipeService.saveRecipe(recipe);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam(required = false) String query) {
        List<Recipe> recipes;
        if (query == null || query.isEmpty()) {
            recipes = recipeService.getAllRecipes();
        } else {
            recipes = recipeService.searchRecipes(query);
        }
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{apiId}")
    public ResponseEntity<Recipe> getRecipeByApiId(@PathVariable Long apiId) {
        Optional<Recipe> recipe = recipeService.findRecipeByApiId(apiId);
        return recipe.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/delete/{apiId}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long apiId) {
        try {
            Optional<Recipe> recipe = recipeService.findRecipeByApiId(apiId);
            if (recipe.isPresent()) {
                recipeService.deleteRecipeByApiId(apiId);
                return ResponseEntity.ok("Rezept wurde gelöscht.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rezept wurde nicht gespeichert.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Löschen des Rezepts.");
        }
    }
}
package com.Rezeptmanager.Rezeptmanager.Service;

import com.Rezeptmanager.Rezeptmanager.Model.Recipe;
import com.Rezeptmanager.Rezeptmanager.Repository.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe saveRecipe(Recipe recipe) {
        if (recipeRepository.findByApiId(recipe.getApiId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Rezept bereits im Kochbuch");
        }
        return recipeRepository.save(recipe);
    }

    public Optional<Recipe> findRecipeByApiId(Long apiId) {
        return recipeRepository.findByApiId(apiId);
    }

    @Transactional
    public void deleteRecipeByApiId(Long apiId) {
        recipeRepository.deleteByApiId(apiId);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> searchRecipes(String query) {
        return recipeRepository.findByTitleContainingIgnoreCase(query);
    }
}
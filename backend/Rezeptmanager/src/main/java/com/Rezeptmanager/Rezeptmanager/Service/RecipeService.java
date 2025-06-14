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

    private final RecipeRepository recipeRepository; // Repository für den Zugriff auf Rezeptdaten

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository; // Initialisierung
    }

    public Recipe saveRecipe(Recipe recipe) {
        // Überprüft, ob ein Rezept mit der gleichen API-ID bereits existiert
        if (recipeRepository.findByApiId(recipe.getApiId()).isPresent()) {
            // Wirft eine Ausnahme, wenn das Rezept bereits existiert
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Rezept bereits im Kochbuch");
        }
        return recipeRepository.save(recipe); // Speichert das Rezept im Repository
    }

    public Optional<Recipe> findRecipeByApiId(Long apiId) {
        return recipeRepository.findByApiId(apiId); // Sucht ein Rezept basierend auf der API-ID
    }

    @Transactional // wird transactional ausgeführt, löscht allso alles, wenn ein Fehler auftritt macht es quasi ein rollback
    public void deleteRecipeByApiId(Long apiId) {
        recipeRepository.deleteByApiId(apiId); // Löscht ein Rezept basierend auf der API-ID
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll(); // Gibt alle Rezepte zurück
    }

    public List<Recipe> searchRecipes(String query) {
        return recipeRepository.findByTitleContainingIgnoreCase(query); // Sucht Rezepte basierend auf einem Suchbegriff
    }
}
package com.Rezeptmanager.Rezeptmanager.Repository;

import com.Rezeptmanager.Rezeptmanager.Model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByApiId(Long apiId); // Findet ein Rezept basierend auf der API-ID
    List<Recipe> findByTitleContainingIgnoreCase(String title); // Findet Rezepte, mit dem Suchbegriff
    void deleteByApiId(Long apiId); // Löscht ein Rezept basierend auf der API-ID
}
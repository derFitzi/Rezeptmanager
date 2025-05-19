package com.Rezeptmanager.Rezeptmanager.Repository;

import com.Rezeptmanager.Rezeptmanager.Model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByApiId(Long apiId);
    List<Recipe> findByTitleContainingIgnoreCase(String title);
    void deleteByApiId(Long apiId);
}
package com.Rezeptmanager.Rezeptmanager.Repository;

import com.Rezeptmanager.Rezeptmanager.Model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
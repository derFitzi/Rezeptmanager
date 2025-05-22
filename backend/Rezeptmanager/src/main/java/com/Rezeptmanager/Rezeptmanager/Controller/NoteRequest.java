package com.Rezeptmanager.Rezeptmanager.Controller;

import com.Rezeptmanager.Rezeptmanager.Model.Recipe;

public class NoteRequest {
    private String content;
    private Recipe recipeDetails;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Recipe getRecipeDetails() {
        return recipeDetails;
    }

    public void setRecipeDetails(Recipe recipeDetails) {
        this.recipeDetails = recipeDetails;
    }
}
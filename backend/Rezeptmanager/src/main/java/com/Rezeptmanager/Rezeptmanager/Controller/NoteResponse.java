package com.Rezeptmanager.Rezeptmanager.Controller;

public class NoteResponse {
    private Long id;
    private String content;
    private Long recipeId;

    public NoteResponse(Long id, String content, Long recipeId) {
        this.id = id;
        this.content = content;
        this.recipeId = recipeId;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getRecipeId() {
        return recipeId;
    }
}
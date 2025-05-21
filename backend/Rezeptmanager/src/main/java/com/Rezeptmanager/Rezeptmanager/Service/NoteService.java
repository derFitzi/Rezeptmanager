package com.Rezeptmanager.Rezeptmanager.Service;

import com.Rezeptmanager.Rezeptmanager.Model.Note;
import com.Rezeptmanager.Rezeptmanager.Model.Recipe;
import com.Rezeptmanager.Rezeptmanager.Repository.NoteRepository;
import com.Rezeptmanager.Rezeptmanager.Repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final RecipeRepository recipeRepository;

    public NoteService(NoteRepository noteRepository, RecipeRepository recipeRepository) {
        this.noteRepository = noteRepository;
        this.recipeRepository = recipeRepository;
    }

    public Note addNoteToRecipe(Long apiId, String content, Recipe recipeDetails) {
        // Check if the recipe already exists in the database
        Recipe recipe = recipeRepository.findByApiId(apiId).orElseGet(() -> {
            // Save the recipe if it does not exist
            Recipe newRecipe = new Recipe();
            newRecipe.setApiId(apiId);
            newRecipe.setTitle(recipeDetails.getTitle());
            newRecipe.setImageUrl(recipeDetails.getImageUrl());
            newRecipe.setSourceUrl(recipeDetails.getSourceUrl());
            newRecipe.setInstructions(recipeDetails.getInstructions());
            return recipeRepository.save(newRecipe);
        });

        // Create a new note and associate it with the recipe
        Note note = new Note();
        note.setContent(content);
        note.setRecipe(recipe);
        return noteRepository.save(note);
    }

    public List<Note> getNotesByRecipeId(Long recipeId) {
        return noteRepository.findByRecipeId(recipeId);
    }

    public void deleteNoteById(Long noteId) {
        noteRepository.deleteById(noteId);
    }
}
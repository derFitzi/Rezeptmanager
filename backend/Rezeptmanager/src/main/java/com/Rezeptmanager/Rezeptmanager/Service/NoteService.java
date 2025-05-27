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

    public Note addNoteToRecipe(Long apiId, Note note) {
        Recipe recipe = recipeRepository.findByApiId(apiId).orElseGet(() -> {
            Recipe newRecipe = new Recipe();
            newRecipe.setApiId(apiId);
            newRecipe.setTitle(note.getRecipe().getTitle());
            newRecipe.setImage(note.getRecipe().getImage());
            newRecipe.setInstructions(note.getRecipe().getInstructions());
            return recipeRepository.save(newRecipe);
        });

        note.setRecipe(recipe);
        return noteRepository.save(note);
    }

    public List<Note> getNotesByRecipeId(Long recipeId) {
        return noteRepository.findByRecipeId(recipeId);
    }

    public void deleteNoteById(Long noteId) {
        if (!noteRepository.existsById(noteId)) {
            throw new IllegalArgumentException("Note with ID " + noteId + " does not exist.");
        }
        noteRepository.deleteById(noteId);
    }
}
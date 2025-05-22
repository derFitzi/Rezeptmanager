package com.Rezeptmanager.Rezeptmanager.Service;

import com.Rezeptmanager.Rezeptmanager.Controller.NoteResponse;
import com.Rezeptmanager.Rezeptmanager.Model.Note;
import com.Rezeptmanager.Rezeptmanager.Model.Recipe;
import com.Rezeptmanager.Rezeptmanager.Repository.NoteRepository;
import com.Rezeptmanager.Rezeptmanager.Repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final RecipeRepository recipeRepository;

    public NoteService(NoteRepository noteRepository, RecipeRepository recipeRepository) {
        this.noteRepository = noteRepository;
        this.recipeRepository = recipeRepository;
    }

    public NoteResponse addNoteToRecipe(Long apiId, String content, Recipe recipeDetails) {
        Recipe recipe = recipeRepository.findByApiId(apiId).orElseGet(() -> {
            Recipe newRecipe = new Recipe();
            newRecipe.setApiId(apiId);
            newRecipe.setTitle(recipeDetails.getTitle());
            newRecipe.setImageUrl(recipeDetails.getImageUrl());
            newRecipe.setSourceUrl(recipeDetails.getSourceUrl());
            newRecipe.setInstructions(recipeDetails.getInstructions());
            return recipeRepository.save(newRecipe);
        });

        Note note = new Note();
        note.setContent(content);
        note.setRecipe(recipe);
        Note savedNote = noteRepository.save(note);

        return new NoteResponse(savedNote.getId(), savedNote.getContent(), recipe.getId());
    }

    public List<NoteResponse> getNotesByRecipeId(Long recipeId) {
        List<Note> notes = noteRepository.findByRecipeId(recipeId);
        return notes.stream()
                .map(note -> new NoteResponse(note.getId(), note.getContent(), note.getRecipe().getId()))
                .collect(Collectors.toList());
    }

    public void deleteNoteById(Long noteId) {
        if (!noteRepository.existsById(noteId)) {
            throw new IllegalArgumentException("Note with ID " + noteId + " does not exist.");
        }
        noteRepository.deleteById(noteId);
    }
}
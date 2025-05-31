package com.Rezeptmanager.Rezeptmanager.Service;

import com.Rezeptmanager.Rezeptmanager.Model.Note;
import com.Rezeptmanager.Rezeptmanager.Model.Recipe;
import com.Rezeptmanager.Rezeptmanager.Repository.NoteRepository;
import com.Rezeptmanager.Rezeptmanager.Repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository; // Repository für den Zugriff auf Notizdaten
    private final RecipeRepository recipeRepository; // Repository für den Zugriff auf Rezeptdaten

    public NoteService(NoteRepository noteRepository, RecipeRepository recipeRepository) {
        this.noteRepository = noteRepository;  // Initialisierung
        this.recipeRepository = recipeRepository;
    }

    public Note addNoteToRecipe(Long apiId, Note note) {
        // Sucht ein Rezept basierend auf der API-ID oder erstellt ein neues, falls es nicht existiert
        Recipe recipe = recipeRepository.findByApiId(apiId).orElseGet(() -> {
            Recipe newRecipe = new Recipe(); // Erstellt ein neues Rezept
            newRecipe.setApiId(apiId);
            newRecipe.setTitle(note.getRecipe().getTitle());
            newRecipe.setImage(note.getRecipe().getImage());
            newRecipe.setInstructions(note.getRecipe().getInstructions());
            return recipeRepository.save(newRecipe); // Speichert das neue Rezept im Repository
        });

        note.setRecipe(recipe); // Verknüpft die Notiz mit dem Rezept
        return noteRepository.save(note); // Speichert die Notiz im Repository
    }

    public List<Note> getNotesByRecipeId(Long recipeId) {
        return noteRepository.findByRecipeId(recipeId); // Sucht alle Notizen basierend auf der Rezept-ID
    }

    public void deleteNoteById(Long noteId) {
        // Überprüft, ob die Notiz mit der angegebenen ID existiert
        if (!noteRepository.existsById(noteId)) {
            throw new IllegalArgumentException("Notiz mit der ID " + noteId + " gibt es nicht");

        }
        noteRepository.deleteById(noteId);
    }
}
package com.Rezeptmanager.Rezeptmanager.Controller;

import com.Rezeptmanager.Rezeptmanager.Model.Note;
import com.Rezeptmanager.Rezeptmanager.Service.NoteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes") // Basis-URL für alle Notizen-bezogenen Endpunkte
public class NoteController {

    private final NoteService noteService; // Service für die Logik von Notizen

    public NoteController(NoteService noteService) {
        this.noteService = noteService; // Initialisiert den NoteService über Dependency Injection
    }

    @GetMapping("/{recipeId}") // Endpunkt zum Abrufen aller Notizen für ein Rezept mit einer bestimmten ID
    public List<Note> getNotesByRecipeId(@PathVariable @Min(1) Long recipeId) {
        return noteService.getNotesByRecipeId(recipeId);
    }

    @PostMapping("/{apiId}") // Endpunkt zum Hinzufügen einer neuen Notiz zu einem Rezept mit einer bestimmten API-ID
    public Note addNoteToRecipe(
            @PathVariable @Min(1) Long apiId,
            @Valid @RequestBody Note note) {
        return noteService.addNoteToRecipe(apiId, note); // Fügt die Notiz dem Rezept hinzu
    }

    @DeleteMapping("/{noteId}") // Endpunkt zum Löschen einer Notiz anhand ihrer ID
    public void deleteNoteById(@PathVariable @Min(1) Long noteId) {
        noteService.deleteNoteById(noteId); // Löscht die Notiz
    }
}
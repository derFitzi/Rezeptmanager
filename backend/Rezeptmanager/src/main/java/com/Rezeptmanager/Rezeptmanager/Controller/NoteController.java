package com.Rezeptmanager.Rezeptmanager.Controller;

import com.Rezeptmanager.Rezeptmanager.Model.Note;
import com.Rezeptmanager.Rezeptmanager.Service.NoteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/{recipeId}")
    public List<Note> getNotesByRecipeId(@PathVariable @Min(1) Long recipeId) {
        return noteService.getNotesByRecipeId(recipeId);
    }

    @PostMapping("/{apiId}")
    public Note addNoteToRecipe(
            @PathVariable @Min(1) Long apiId,
            @Valid @RequestBody Note note) {
        return noteService.addNoteToRecipe(apiId, note);
    }

    @DeleteMapping("/{noteId}")
    public void deleteNoteById(@PathVariable @Min(1) Long noteId) {
        noteService.deleteNoteById(noteId);
    }
}
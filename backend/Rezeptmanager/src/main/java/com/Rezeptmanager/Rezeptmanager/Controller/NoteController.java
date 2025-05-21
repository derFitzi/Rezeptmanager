package com.Rezeptmanager.Rezeptmanager.Controller;

import com.Rezeptmanager.Rezeptmanager.Model.Note;
import com.Rezeptmanager.Rezeptmanager.Model.Recipe;
import com.Rezeptmanager.Rezeptmanager.Service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/{apiId}")
    public ResponseEntity<Note> addNote(@PathVariable Long apiId, @RequestBody NoteRequest noteRequest) {
        Note note = noteService.addNoteToRecipe(apiId, noteRequest.getContent(), noteRequest.getRecipeDetails());
        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<List<Note>> getNotesByRecipeId(@PathVariable Long recipeId) {
        List<Note> notes = noteService.getNotesByRecipeId(recipeId);
        return ResponseEntity.ok(notes);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable Long noteId) {
        noteService.deleteNoteById(noteId);
        return ResponseEntity.noContent().build();
    }
}
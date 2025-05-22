package com.Rezeptmanager.Rezeptmanager.Controller;

import com.Rezeptmanager.Rezeptmanager.Service.NoteService;
import com.Rezeptmanager.Rezeptmanager.Controller.NoteRequest;
import com.Rezeptmanager.Rezeptmanager.Controller.NoteResponse;
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
    public ResponseEntity<NoteResponse> addNote(@PathVariable Long apiId, @RequestBody NoteRequest noteRequest) {
        NoteResponse noteResponse = noteService.addNoteToRecipe(apiId, noteRequest.getContent(), noteRequest.getRecipeDetails());
        return ResponseEntity.ok(noteResponse);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<List<NoteResponse>> getNotesByRecipeId(@PathVariable Long recipeId) {
        List<NoteResponse> notes = noteService.getNotesByRecipeId(recipeId);
        return ResponseEntity.ok(notes);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable Long noteId) {
        noteService.deleteNoteById(noteId);
        return ResponseEntity.noContent().build();
    }
}
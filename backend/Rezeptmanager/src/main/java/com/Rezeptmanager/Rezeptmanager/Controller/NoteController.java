package com.Rezeptmanager.Rezeptmanager.Controller;


import com.Rezeptmanager.Rezeptmanager.Model.Note;
import com.Rezeptmanager.Rezeptmanager.Service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes/{recipeId}/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<Note> getNotesByRecipeId(@PathVariable Long recipeId) {
        return noteService.getNotesByRecipeId(recipeId);
    }

    @PostMapping
    public Note saveNote(@RequestBody Note note) {
        return noteService.saveNote(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
package com.Rezeptmanager.Rezeptmanager.Service;


import com.Rezeptmanager.Rezeptmanager.Model.Note;
import com.Rezeptmanager.Rezeptmanager.Repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getNotesByRecipeId(Long recipeId) {
        return noteRepository.findAll(); // Anpassung für spezifische Abfragen nötig
    }

    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}
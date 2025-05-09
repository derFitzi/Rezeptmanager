package com.Rezeptmanager.Rezeptmanager.Repository;

import com.Rezeptmanager.Rezeptmanager.Model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
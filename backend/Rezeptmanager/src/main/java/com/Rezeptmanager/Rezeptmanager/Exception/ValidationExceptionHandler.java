package com.Rezeptmanager.Rezeptmanager.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // Klasse als globalen Fehlerbehandler
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // Behandelt Validierungsfehler
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>(); // Erstellt eine Map für die Fehlerdetails
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()) // Fügt Feldnamen und Fehlermeldungen zur Map hinzu
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // Gibt die Fehlerdetails mit Status 400 zurück
    }
}
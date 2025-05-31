package com.Rezeptmanager.Rezeptmanager.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity // Markiert die Klasse als JPA-Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generiert automatisch die ID-Werte
    private Long id;

    @NotNull(message = "Der Content darf nicht null sein.") // Validiert, dass der Inhalt nicht null ist
    @Size(min = 1, max = 1000, message = "Der Titel muss zwischen 1 und 1000 Zeichen lang sein.") // Validiert die Länge des Inhalts
    private String content;

    @ManyToOne //Many-to-One-Beziehung zu Recipe
    @JoinColumn(name = "recipe_id", nullable = false) // Verknüpft die Note mit einem Rezept über die Rezept-ID
    @JsonBackReference // Verhindert eine rekursive Serialisierung
    private Recipe recipe;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
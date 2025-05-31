package com.Rezeptmanager.Rezeptmanager.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generiert automatisch die ID-Werte
    private Long id;

    @NotNull(message = "API ID darf nicht null sein.")
    private Long apiId; // API-ID des Rezepts

    @NotNull(message = "Der Titel darf nicht null sein.")
    @Size(min = 3, max = 100, message = "Der Titel muss zwischen 3 und 100 Zeichen lang sein.")
    private String title;

    @NotNull(message = "Das Bild darf nicht null sein.")
    private String image;

    @Column(columnDefinition = "TEXT") // Speichert die Anweisungen als Text
    @NotNull(message = "Die Anweisungen dürfen nicht null sein.")
    private String instructions;

    @Min(value = 1, message = "Die Zubereitungszeit muss mindestens 1 Minute betragen.")
    private Integer readyInMinutes;

    @Min(value = 1, message = "Die Anzahl der Portionen muss mindestens 1 betragen.")
    private Integer servings;

    @Column(length = 5000)
    private String summary;

    @Min(value = 0, message = "Der Health-Score darf nicht negativ sein.")
    private Integer healthScore;

    @DecimalMin(value = "0.0", inclusive = true, message = "Der Preis pro Portion darf nicht negativ sein.")
    private Double pricePerServing;

    @Min(value = 0, message = "Die Anzahl der Likes darf nicht negativ sein.")
    private Integer aggregateLikes;

    @ElementCollection // Speichert eine Liste von Diäten
    @Size(max = 50, message = "Es dürfen maximal 50 Diäten angegeben werden.") // Validiert die maximale Anzahl der Diäten
    private List<String> diets;

    @ElementCollection // Speichert eine Liste von Gerichtstypen
    @Size(max = 50, message = "Es dürfen maximal 50 Gerichtstypen angegeben werden.") // Validiert die maximale Anzahl der Gerichtstypen
    private List<String> dishTypes;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true) //One-to-Many-Beziehung zu Notes
    @JsonManagedReference // Verhindert eine rekursive Serialisierung
    private List<Note> notes;

    @Lob // Speichert die Zutaten als Large Object (weil Json als Text gespeichert und der ist extem lang und komplex)
    private String ingredients; // Zutaten als String gespeichert

    // Getter und Setter für die Felder
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Integer healthScore) {
        this.healthScore = healthScore;
    }

    public Double getPricePerServing() {
        return pricePerServing;
    }

    public void setPricePerServing(Double pricePerServing) {
        this.pricePerServing = pricePerServing;
    }

    public Integer getAggregateLikes() {
        return aggregateLikes;
    }

    public void setAggregateLikes(Integer aggregateLikes) {
        this.aggregateLikes = aggregateLikes;
    }

    public List<String> getDiets() {
        return diets;
    }

    public void setDiets(List<String> diets) {
        this.diets = diets;
    }

    public List<String> getDishTypes() {
        return dishTypes;
    }

    public void setDishTypes(List<String> dishTypes) {
        this.dishTypes = dishTypes;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
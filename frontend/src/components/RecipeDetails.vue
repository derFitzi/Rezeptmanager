<template>
  <div class="recipe-details-container">
    <div v-if="recipeDetails" class="recipe-details">
      <div class="recipe-header">
        <h1 class="recipe-title">{{ recipeDetails.title }}</h1> <!-- Zeigt den Titel des Rezepts an -->
        <div class="action-buttons">
          <button @click="saveToCookbook" class="btn btn-primary" :disabled="isRecipeSaved">
            <span class="btn-icon">💾</span>
            {{ isRecipeSaved ? 'Gespeichert' : 'Speichern' }} <!-- Button zum Speichern des Rezepts -->
          </button>
          <button @click="deleteFromCookbook" class="btn btn-danger" :disabled="!isRecipeSaved">
            <span class="btn-icon">🗑️</span>
            Löschen <!-- Button zum Löschen des Rezepts -->
          </button>
        </div>
      </div>

      <!-- Rezept Bild -->
      <div class="recipe-image-container">
        <img :src="recipeDetails.image" :alt="recipeDetails.title" class="recipe-image" /> <!-- Zeigt das Rezeptbild an -->
      </div>

      <!-- Rezept Eigenschaften -->
      <div class="recipe-stats">
        <div class="stat-card">
          <span class="stat-icon">⏱️</span>
          <div class="stat-info">
            <span class="stat-value">{{ recipeDetails.readyInMinutes }}</span>
            <span class="stat-label">Minuten</span> <!-- Zeigt die Zubereitungszeit an -->
          </div>
        </div>
        <div class="stat-card">
          <span class="stat-icon">👥</span>
          <div class="stat-info">
            <span class="stat-value">{{ recipeDetails.servings }}</span>
            <span class="stat-label">Portionen</span> <!-- Zeigt die Anzahl der Portionen an -->
          </div>
        </div>
        <div class="stat-card">
          <span class="stat-icon">❤️</span>
          <div class="stat-info">
            <span class="stat-value">{{ recipeDetails.healthScore }}</span>
            <span class="stat-label">Health Score</span> <!-- Zeigt den Gesundheitswert des Rezepts an -->
          </div>
        </div>
        <div class="stat-card">
          <span class="stat-icon">💰</span>
          <div class="stat-info">
            <span class="stat-value">{{ recipeDetails.pricePerServing.toFixed(2) }}€</span>
            <span class="stat-label">pro Portion</span> <!-- Zeigt die Kosten pro Portion an -->
          </div>
        </div>
      </div>

      <!-- Zusammenfassung -->
      <div v-if="recipeDetails.summary" class="recipe-section">
        <h2 class="section-title">
          Zusammenfassung
        </h2>
        <div class="summary-content" v-html="recipeDetails.summary"></div> <!-- Zeigt die Zusammenfassung des Rezepts an -->
      </div>

      <!-- Zusätzliche Info -->
      <div class="additional-info">
        <div v-if="recipeDetails.diets && recipeDetails.diets.length > 0" class="info-group">
          <h3 class="info-title">Diäten</h3>
          <div class="tag-list">
            <span v-for="diet in recipeDetails.diets" :key="diet" class="tag tag-diet">{{ diet }}</span> <!-- Zeigt die Diät-Tags an -->
          </div>
        </div>
        <div v-if="recipeDetails.dishTypes && recipeDetails.dishTypes.length > 0" class="info-group">
          <h3 class="info-title">Gerichtstypen</h3>
          <div class="tag-list">
            <span v-for="type in recipeDetails.dishTypes" :key="type" class="tag tag-type">{{ type }}</span> <!-- Zeigt die Gerichtstyp-Tags an -->
          </div>
        </div>
      </div>

      <!-- Zutaten -->
      <div class="recipe-section">
        <h2 class="section-title">
          Zutaten
        </h2>
        <ul class="ingredient-list">
          <li v-for="ingredient in recipeDetails.ingredients" :key="ingredient.id" class="ingredient-item">
            <span class="ingredient-amount">{{ ingredient.amount }} {{ ingredient.unit }}</span>
            <span class="ingredient-name">{{ ingredient.name }}</span> <!-- Zeigt die Zutaten mit Menge und Einheit an -->
          </li>
        </ul>
      </div>

      <!-- Anleitung -->
      <div class="recipe-section">
        <h2 class="section-title">
          Zubereitung
        </h2>
        <ol class="instruction-list">
          <li v-for="(step, index) in recipeDetails.instructions" :key="index" class="instruction-item">
            <span class="step-number">{{ }}</span>
            <span class="step-text">{{ step }}</span> <!-- Zeigt die Zubereitungsschritte an -->
          </li>
        </ol>
      </div>

      <!-- Notizen -->
      <div class="recipe-section">
        <h2 class="section-title">
          <span class="section-icon">📋</span>
          Notizen
        </h2>

        <div class="notes-container">
          <div v-if="notes.length > 0" class="notes-list">
            <div v-for="note in notes" :key="note.id" class="note-item">
              <p class="note-content">{{ note.content }}</p> <!-- Zeigt die Notizen an -->
              <button @click="deleteNote(note.id)" class="note-delete-btn">
                <span>🗑️</span> <!-- Button zum Löschen einer Notiz -->
              </button>
            </div>
          </div>
          <div v-else class="empty-notes">
            <span class="empty-icon">📝</span>
            <p>Noch keine Notizen vorhanden</p> <!-- Zeigt eine Nachricht, wenn keine Notizen vorhanden sind -->
          </div>

          <div class="add-note-form">
            <div class="note-input-wrapper">
              <textarea
                  v-model="newNoteContent"
                  placeholder="Neue Notiz hinzufügen..."
                  class="note-input"
                  rows="3"
              ></textarea> <!-- Eingabefeld für neue Notizen -->
              <button @click="addNote" class="btn btn-secondary" :disabled="!newNoteContent.trim()">
                <span class="btn-icon">➕</span>
                Hinzufügen <!-- Button zum Hinzufügen einer neuen Notiz -->
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <span class="empty-icon">🍽️</span>
      <h2>Kein Rezept ausgewählt</h2>
      <p>Wählen Sie ein Rezept aus der Liste aus, um die Details anzuzeigen.</p> <!-- Nachricht, wenn kein Rezept ausgewählt ist -->
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: ["recipe"], // Übergibt die Rezeptdaten als Prop
  data() {
    return {
      recipeDetails: null, // Rezeptdetails
      notes: [], // Notizen zum Rezept
      newNoteContent: "", // Inhalt der neuen Notiz
      isRecipeSaved: false, // Gibt an, ob das Rezept gespeichert ist
      currentApiId: null, // aktuelle API-ID des Rezepts
    };
  },
  watch: {
    recipe: {
      immediate: true, // Führt den Watcher direkt beim Laden aus
      async handler(newRecipe) {
        if (newRecipe?.apiId) { // Prüft, ob das neue Rezept eine API-ID hat
          if (this.currentApiId !== newRecipe.apiId) { // Verhindert unnötige Updates
            this.resetState(); // Setzt den Zustand zurück
            this.currentApiId = newRecipe.apiId; // Aktualisiert die aktuelle API-ID

            await this.checkIfRecipeIsSaved(newRecipe.apiId); // Prüft, ob das Rezept gespeichert ist
            await this.fetchRecipeDetails(newRecipe.apiId); // Lädt die Rezeptdetails
            await this.fetchNotes(newRecipe.apiId); // Lädt die Notizen
          }
        } else {
          this.resetState(); // Setzt den Zustand zurück, wenn kein Rezept ausgewählt ist
        }
      },
    },
  },
  methods: {
    resetState() {
      this.recipeDetails = null; // Löscht die Rezeptdetails
      this.notes = []; // Löscht die Notizen
      this.isRecipeSaved = false; // Setzt den Speicherstatus zurück
      this.currentApiId = null; // Löscht die aktuelle API-ID
      this.newNoteContent = ""; // Löscht den Inhalt der neuen Notiz
    },

    async checkIfRecipeIsSaved(apiId) {
      try {
        const response = await axios.get(`/api/recipes/${apiId}`); // Fragt die API, ob das Rezept gespeichert ist
        this.isRecipeSaved = !!response.data; // Setzt den Speicherstatus basierend auf der Antwort
      } catch (error) {
        if (error.response?.status === 404) {
          this.isRecipeSaved = false; // Rezept ist nicht gespeichert
        } else {
          console.error("Error checking if recipe is saved:", error); // Loggt Fehler
          this.isRecipeSaved = false;
        }
      }
    },

    async fetchRecipeDetails(apiId) {
      try {
        if (this.isRecipeSaved) {
          const response = await axios.get(`/api/recipes/${apiId}`); // Lädt gespeicherte Rezeptdetails
          this.recipeDetails = {
            title: response.data.title,
            sourceUrl: response.data.sourceUrl,
            sourceName: response.data.sourceName,
            image: response.data.image,
            readyInMinutes: response.data.readyInMinutes,
            servings: response.data.servings,
            summary: response.data.summary,
            healthScore: response.data.healthScore,
            pricePerServing: (response.data.pricePerServing || 0),
            aggregateLikes: response.data.aggregateLikes,
            diets: response.data.diets || [],
            dishTypes: response.data.dishTypes || [],
            ingredients: response.data.ingredients ?
                JSON.parse(response.data.ingredients).map(
                    (ingredient) => ({
                      id: ingredient.id,
                      name: ingredient.name,
                      amount: ingredient.amount,
                      unit: ingredient.unit,
                    })
                ) : [],
            instructions: response.data.instructions ?
                JSON.parse(response.data.instructions) : [],
          };
        } else {
          const response = await axios.get(
              `https://api.spoonacular.com/recipes/${apiId}/information`,
              {
                params: {
                  apiKey: "e7d045456c0f40da8b6db6fe7b794d3e",
                },
              }
          );
          this.recipeDetails = {
            title: response.data.title,
            sourceUrl: response.data.sourceUrl,
            sourceName: response.data.sourceName,
            image: response.data.image,
            readyInMinutes: response.data.readyInMinutes,
            servings: response.data.servings,
            summary: response.data.summary,
            healthScore: response.data.healthScore,
            pricePerServing: response.data.pricePerServing / 100,
            aggregateLikes: response.data.aggregateLikes,
            diets: response.data.diets || [],
            dishTypes: response.data.dishTypes || [],
            ingredients: response.data.extendedIngredients.map((ingredient) => ({
              id: ingredient.id,
              name: ingredient.name,
              amount: ingredient.amount,
              unit: ingredient.unit,
            })),
            instructions:
                response.data.analyzedInstructions[0]?.steps.map(
                    (step) => step.step
                ) || [],
          };
        }
      } catch (error) {
        console.error("Error fetching recipe details:", error); // Loggt Fehler
        this.recipeDetails = null;
      }
    },

    async fetchNotes(recipeId) {
      this.notes = []; // Löscht die Notizen
      if (!this.isRecipeSaved) return; // Keine Notizen laden, wenn Rezept nicht gespeichert ist

      try {
        const responseRecipe = await axios.get(`/api/recipes/${recipeId}`);
        const dbRecipeId = responseRecipe.data.id; // Extrahiert die interne Rezept-ID
        const response = await axios.get(`/api/notes/${dbRecipeId}`); // Lädt die Notizen aus der Datenbank
        this.notes = response.data; // Speichert die Notizen
      } catch (error) {
        if (error.response?.status !== 404) {
          console.error("Error fetching notes:", error); // Loggt Fehler
        }
        this.notes = [];
      }
    },

    async addNote() {
      if (!this.isRecipeSaved) {
        alert("Rezept muss zuerst gespeichert werden, um Notizen hinzuzufügen."); // Warnung, wenn Rezept nicht gespeichert ist
        return;
      }
      if (!this.newNoteContent.trim()) return; // Verhindert das Hinzufügen leerer Notizen

      try {
        const response = await axios.post(`/api/notes/${this.recipe.apiId}`, {
          content: this.newNoteContent, // Inhalt der neuen Notiz
        });

        const newNote = {
          id: response.data.id, // ID der neuen Notiz
          content: response.data.content, // Inhalt der neuen Notiz
        };

        this.notes = [...this.notes, newNote]; // Fügt die neue Notiz zur Liste hinzu
        this.newNoteContent = ""; // Löscht den Eingabebereich
      } catch (error) {
        console.error("Error adding note:", error.response?.data || error.message); // Loggt Fehler
      }
    },

    async deleteNote(noteId) {
      try {
        await axios.delete(`/api/notes/${noteId}`); // Löscht die Notiz aus der Datenbank
        this.notes = this.notes.filter((note) => note.id !== noteId); // Entfernt die Notiz aus der Liste
      } catch (error) {
        console.error("Error deleting note:", error.response?.data || error.message); // Loggt Fehler
      }
    },

    async saveToCookbook() {
      if (this.isRecipeSaved) {
        alert("Rezept ist bereits im Kochbuch gespeichert."); // Warnung, wenn Rezept bereits gespeichert ist
        return;
      }

      try {
        const response = await axios.post("/api/recipes/save-full", {
          apiId: this.recipe.apiId,
          title: this.recipeDetails.title,
          image: this.recipeDetails.image,
          readyInMinutes: this.recipeDetails.readyInMinutes,
          servings: this.recipeDetails.servings,
          summary: this.recipeDetails.summary,
          healthScore: this.recipeDetails.healthScore,
          pricePerServing: this.recipeDetails.pricePerServing,
          aggregateLikes: this.recipeDetails.aggregateLikes,
          diets: this.recipeDetails.diets,
          dishTypes: this.recipeDetails.dishTypes,
          ingredients: JSON.stringify(
              this.recipeDetails.ingredients.map((ingredient) => ({
                id: ingredient.id,
                name: ingredient.name,
                amount: ingredient.amount,
                unit: ingredient.unit,
              }))
          ),
          instructions: JSON.stringify(this.recipeDetails.instructions), // Zubereitungsschritte
        });

        this.isRecipeSaved = true; // Setzt den Speicherstatus auf "gespeichert"
        alert("Rezept wurde erfolgreich gespeichert!"); // Erfolgsmeldung
        this.$emit("update-db-recipes", this.$parent.dbSearchQuery); // Aktualisiert die Rezeptliste
      } catch (error) {
        if (error.response?.status === 409) {
          alert("Rezept ist bereits gespeichert."); // Warnung
          this.isRecipeSaved = true; // Setzt den Speicherstatus auf "gespeichert"
        } else {
          console.error("Error saving recipe:", error); // Loggt Fehler
          alert("Fehler beim Speichern des Rezepts.");
        }
      }
    },

    async deleteFromCookbook() {
      if (!this.isRecipeSaved) {
        alert("Rezept ist nicht im Kochbuch gespeichert."); // Warnung, wenn Rezept nicht gespeichert ist
        return;
      }

      try {
        await axios.delete(`/api/recipes/delete/${this.recipe.apiId}`); // Löscht das Rezept aus der Datenbank
        this.isRecipeSaved = false; // Setzt den Speicherstatus zurück
        this.notes = []; // Löscht die Notizen
        alert("Rezept wurde erfolgreich gelöscht.");
        this.$emit("update-db-recipes", this.$parent.dbSearchQuery); // Aktualisiert die Rezeptliste
      } catch (error) {
        console.error("Error deleting recipe:", error); // Loggt Fehler
        alert("Fehler beim Löschen des Rezepts.");
      }
    },
  },
};
</script>

<style scoped>
.recipe-details-container {
  height: 100%;
  overflow-y: auto;
}

.recipe-details {
  max-width: 100%;
}

.recipe-header {
  margin-bottom: 1.5rem;
}

.recipe-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 1rem 0;
  line-height: 1.3;
}

.action-buttons {
  display: flex;
  gap: 0.75rem;
  flex-wrap: wrap;
}

.btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.25rem;
  border: none;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none !important;
}

.btn-primary {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
}

.btn-danger {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  color: white;
}

.btn-danger:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.3);
}

.btn-secondary {
  background: linear-gradient(135deg, #0f2aa8 0%, #8498e7 100%);
  color: white;
}

.btn-secondary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.btn-icon {
  font-size: 0.9rem;
}

.recipe-image-container {
  margin-bottom: 1.5rem;
  text-align: center;
}

.recipe-image {
  max-width: 100%;
  max-height: 300px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  object-fit: cover;
}

.recipe-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 1rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  padding: 1rem;
  border-radius: 12px;
  text-align: center;
  border: 1px solid #e2e8f0;
  transition: transform 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  font-size: 1.5rem;
  display: block;
  margin-bottom: 0.5rem;
}

.stat-value {
  display: block;
  font-size: 1.1rem;
  font-weight: 700;
  color: #1f2937;
}

.stat-label {
  font-size: 0.8rem;
  color: #6b7280;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.recipe-section {
  margin-bottom: 2rem;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #e5e7eb;
}

.section-icon {
  font-size: 1.1rem;
}

.summary-content {
  background: #f8fafc;
  padding: 1.25rem;
  border-radius: 12px;
  border-left: 4px solid #667eea;
  line-height: 1.6;
  color: #374151;
}

.additional-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.info-group {
  background: #f8fafc;
  padding: 1.25rem;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.info-title {
  font-size: 1rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 0.75rem;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tag {
  display: inline-block;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
}

.tag-diet {
  background: linear-gradient(135deg, #dcfce7 0%, #bbf7d0 100%);
  color: #166534;
  border: 1px solid #a7f3d0;
}

.tag-type {
  background: linear-gradient(135deg, #ddd6fe 0%, #c4b5fd 100%);
  color: #5b21b6;
  border: 1px solid #c4b5fd;
}

.ingredient-list {
  list-style: none;
  padding: 0;
  display: grid; /* Grid-Layout verwenden */
  grid-template-columns: repeat(2, 1fr); /* Zwei Spalten */
  gap: 0.75rem; /* Abstand zwischen den Elementen */
}

.ingredient-item {
  display: flex;
  align-items: center;
  padding: 0.75rem;
  background: #f8fafc;
  border-radius: 8px;
  border-left: 4px solid #667eea;
}

.ingredient-amount {
  font-weight: 600;
  color: #667eea;
  min-width: 80px;
  margin-right: 1rem;
}

.ingredient-name {
  color: #374151;
  text-transform: capitalize;
}

.instruction-list {
  list-style: none;
  padding: 0;
  counter-reset: step-counter;
}

.instruction-item {
  display: flex;
  align-items: flex-start;
  padding: 1rem;
  margin-bottom: 1rem;
  background: #f8fafc;
  border-radius: 12px;
  border-left: 4px solid #f59e0b;
  counter-increment: step-counter;
}

.step-number {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  color: white;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 0.9rem;
  margin-right: 1rem;
  flex-shrink: 0;
}

.step-number::before {
  content: counter(step-counter);
}

.step-text {
  line-height: 1.6;
  color: #374151;
}

.notes-container {
  background: #f8fafc;
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid #e2e8f0;
}

.notes-list {
  margin-bottom: 1.5rem;
}

.note-item {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding: 1rem;
  background: white;
  border-radius: 8px;
  margin-bottom: 0.75rem;
  border: 1px solid #e5e7eb;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.note-content {
  flex: 1;
  margin: 0;
  color: #374151;
  line-height: 1.5;
}

.note-delete-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 4px;
  transition: background-color 0.2s ease;
  margin-left: 1rem;
}

.note-delete-btn:hover {
  background: #fee2e2;
}

.empty-notes {
  text-align: center;
  padding: 2rem;
  color: #6b7280;
}

.empty-notes .empty-icon {
  font-size: 2rem;
  display: block;
  margin-bottom: 0.5rem;
}

.add-note-form {
  border-top: 1px solid #e5e7eb;
  padding-top: 1.5rem;
}

.note-input-wrapper {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.note-input {
  width: 100%;
  padding: 0.75rem;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 0.9rem;
  font-family: inherit;
  resize: vertical;
  min-height: 80px;
  transition: border-color 0.3s ease;
  outline: none;
}

.note-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.note-input::placeholder {
  color: #9ca3af;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #6b7280;
}

.empty-state .empty-icon {
  font-size: 4rem;
  display: block;
  margin-bottom: 1rem;
}

.empty-state h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 0.5rem;
}

.empty-state p {
  font-size: 1rem;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .recipe-title {
    font-size: 1.5rem;
  }

  .action-buttons {
    flex-direction: column;
  }

  .recipe-stats {
    grid-template-columns: repeat(2, 1fr);
  }

  .additional-info {
    grid-template-columns: 1fr;
  }

  .note-input-wrapper {
    align-items: stretch;
  }

  .instruction-item {
    padding: 0.75rem;
  }

  .step-number {
    width: 24px;
    height: 24px;
    font-size: 0.8rem;
  }
}

@media (max-width: 480px) {
  .recipe-stats {
    grid-template-columns: 1fr;
  }

  .btn {
    padding: 0.5rem 1rem;
    font-size: 0.85rem;
  }
}
</style>
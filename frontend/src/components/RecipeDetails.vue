<template>
  <div v-if="recipeDetails" class="recipe-details">
    <h1>{{ recipeDetails.title }}</h1>
    <button @click="saveToCookbook">Im Kochbuch Speichern</button>
    <button @click="deleteFromCookbook">Rezept löschen</button>
    <p><strong>Quelle:</strong> <a :href="recipeDetails.sourceUrl" target="_blank">{{ recipeDetails.sourceName }}</a></p>
    <img :src="recipeDetails.image" :alt="recipeDetails.title" class="recipe-image" />
    <h2>Zutaten</h2>
    <ul>
      <li v-for="ingredient in recipeDetails.ingredients" :key="ingredient.id">
        {{ ingredient.name }} - {{ ingredient.amount }} {{ ingredient.unit }}
      </li>
    </ul>
    <h2>Zubereitung</h2>
    <ol>
      <li v-for="(step, index) in recipeDetails.instructions" :key="index">
        {{ step }}
      </li>
    </ol>

    <h2>Notizen</h2>
    <ul>
      <li v-for="note in notes" :key="note.id">
        {{ note.content }}
        <button @click="deleteNote(note.id)">Löschen</button>
      </li>
    </ul>
    <input v-model="newNoteContent" placeholder="Neue Notiz hinzufügen" />
    <button @click="addNote">Hinzufügen</button>
  </div>
  <div v-else>
    <p>Kein Rezept ausgewählt.</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: ["recipe"],
  data() {
    return {
      recipeDetails: null,
      notes: [],
      newNoteContent: "",
      isRecipeSaved: false,
    };
  },
  watch: {
    recipe: {
      immediate: true,
      handler(newRecipe) {
        if (newRecipe?.apiId) {
          this.fetchRecipeDetails(newRecipe.apiId);
          this.checkIfRecipeIsSaved(newRecipe.apiId);
          this.fetchNotes(newRecipe.apiId);
        } else {
          this.recipeDetails = null;
          this.notes = [];
          this.isRecipeSaved = false;
        }
      },
    },
  },
  methods: {
    async fetchRecipeDetails(apiId) {
      try {
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
      } catch (error) {
        console.error("Fehler beim Abrufen des Rezepts:", error);
      }
    },
    async checkIfRecipeIsSaved(apiId) {
      try {
        const response = await axios.get(`/api/recipes/${apiId}`);
        this.isRecipeSaved = !!response.data; // Recipe exists in DB
      } catch (error) {
        this.isRecipeSaved = false;
      }
    },
    async fetchNotes(recipeId) {
      try {
        const response = await axios.get(`/api/notes/${recipeId}`);
        this.notes = response.data;
      } catch (error) {
        console.error("Fehler beim Abrufen der Notizen:", error);
      }
    },
    async addNote() {
      if (!this.isRecipeSaved) {
        alert("Rezept muss zuerst gespeichert werden, um Notizen hinzuzufügen.");
        return;
      }
      if (!this.newNoteContent.trim()) return;
      try {
        const response = await axios.post(`/api/notes/${this.recipe.apiId}`, {
          content: this.newNoteContent, // Sende die Notiz als JSON-Objekt
        });
        this.notes.push(response.data); // Füge die neue Notiz zur Liste hinzu
        this.newNoteContent = ""; // Leere das Eingabefeld
      } catch (error) {
        console.error("Fehler beim Hinzufügen der Notiz:", error.response?.data || error.message);
      }
    },
    async deleteNote(noteId) {
      try {
        await axios.delete(`/api/notes/${noteId}`);
        this.notes = this.notes.filter((note) => note.id !== noteId);
      } catch (error) {
        console.error("Fehler beim Löschen der Notiz:", error);
      }
    },
    async saveToCookbook() {
      try {
        const response = await axios.post("/api/recipes/save", {
          apiId: this.recipe.apiId,
          title: this.recipe.title,
          imageUrl: this.recipe.image,
          sourceUrl: this.recipe.sourceUrl,
          instructions: this.recipe.instructions.slice(0, 1000).join("\n"),
        });
        this.isRecipeSaved = true;
        alert("Rezept wurde erfolgreich ins Kochbuch gespeichert!");
      } catch (error) {
        if (error.response?.status === 409) {
          alert("Rezept bereits im Kochbuch");
        } else {
          console.error("Fehler beim Speichern des Rezepts:", error);
          alert("Fehler beim Speichern des Rezepts.");
        }
      }
    },
    async deleteFromCookbook() {
      try {
        await axios.delete(`/api/recipes/delete/${this.recipe.apiId}`);
        this.isRecipeSaved = false;
        alert("Rezept wurde erfolgreich gelöscht.");
      } catch (error) {
        console.error("Fehler beim Löschen des Rezepts:", error);
        alert("Fehler beim Löschen des Rezepts.");
      }
    },
  },
};
</script>

<style>
.recipe-details {
  margin-top: 20px;
  text-align: center;
}

.recipe-image {
  max-width: 100%;
  height: auto;
  margin: 10px 0;
}
</style>
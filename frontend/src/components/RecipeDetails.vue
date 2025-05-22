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
                apiKey: "b36c358f1dde45269e535eff1824af00",
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
        this.isRecipeSaved = !!response.data; // Rezept existiert in der DB
      } catch (error) {
        console.error("Fehler beim Überprüfen, ob das Rezept gespeichert ist:", error.response?.data || error.message);
        if (error.response?.status === 404) {
          this.isRecipeSaved = false; // Rezept existiert nicht
        } else {
          // Bei anderen Fehlern den Status nicht ändern
          console.warn("Unbekannter Fehler, Status von isRecipeSaved bleibt unverändert.");
        }
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
          content: this.newNoteContent,
        });

        console.log("API-Antwort für hinzugefügte Note:", response.data);

        // Extract only the necessary fields
        const newNote = {
          id: response.data.id,
          content: response.data.content,
        };

        // Add the cleaned-up note to the notes array
        this.notes = [...this.notes, newNote];

        // Clear the input field
        this.newNoteContent = "";
      } catch (error) {
        console.error("Fehler beim Hinzufügen der Notiz:", error.response?.data || error.message);
      }
    },
    async deleteNote(noteId) {
      try {
        console.log("Lösche Notiz mit ID:", noteId); // Debugging
        await axios.delete(`/api/notes/${noteId}`);
        this.notes = this.notes.filter((note) => note.id !== noteId);
      } catch (error) {
        console.error("Fehler beim Löschen der Notiz:", error.response?.data || error.message);
      }
    },
    async saveToCookbook() {
      if (!this.isRecipeSaved) {
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

          // Aktualisiere die Liste der Rezepte aus der Datenbank
          this.$emit("update-db-recipes");
        } catch (error) {
          if (error.response?.status === 409) {
            alert("Rezept bereits im Kochbuch");
          } else {
            console.error("Fehler beim Speichern des Rezepts:", error);
            alert("Fehler beim Speichern des Rezepts.");
          }
        }
      } else {
        alert("Rezept ist bereits im Kochbuch gespeichert.");
      }
    },
    async deleteFromCookbook() {
      if (this.isRecipeSaved) {
      try {
        await axios.delete(`/api/recipes/delete/${this.recipe.apiId}`);
        this.isRecipeSaved = false;
        alert("Rezept wurde erfolgreich gelöscht.");

        // Aktualisiere die Liste der Rezepte aus der Datenbank
        this.$emit("update-db-recipes");
      } catch (error) {
        console.error("Fehler beim Löschen des Rezepts:", error);
        alert("Fehler beim Löschen des Rezepts.");
      }}
      else {
        alert("Rezept ist nicht im Kochbuch gespeichert.");
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
<template>
  <div v-if="recipeDetails" class="recipe-details">
    <h1>{{ recipeDetails.title }}</h1>
    <button @click="saveToCookbook">Im Kochbuch Speichern</button>
    <button @click="deleteFromCookbook">Rezept löschen</button>
    <img :src="recipeDetails.image" :alt="recipeDetails.title" class="recipe-image" />
    <p><strong>Zubereitungszeit:</strong> {{ recipeDetails.readyInMinutes }} Minuten</p>
    <p><strong>Portionen:</strong> {{ recipeDetails.servings }}</p>
    <p><strong>Zusammenfassung:</strong> <span v-html="recipeDetails.summary"></span></p>
    <p><strong>Gesundheitsbewertung:</strong> {{ recipeDetails.healthScore }}/100</p>
    <p><strong>Preis pro Portion:</strong> {{ recipeDetails.pricePerServing.toFixed(2) }} €</p>
    <p><strong>Beliebtheit:</strong> {{ recipeDetails.aggregateLikes }} Likes</p>
    <p><strong>Diäten:</strong> {{ recipeDetails.diets.join(", ") }}</p>
    <p><strong>Gerichtstypen:</strong> {{ recipeDetails.dishTypes.join(", ") }}</p>
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
      currentApiId: null, // Track current recipe to detect changes
    };
  },
  watch: {
    recipe: {
      immediate: true,
      async handler(newRecipe) {
        if (newRecipe?.apiId) {
          // Reset state when switching to a different recipe
          if (this.currentApiId !== newRecipe.apiId) {
            this.resetState();
            this.currentApiId = newRecipe.apiId;

            // First check if recipe is saved, then fetch details
            await this.checkIfRecipeIsSaved(newRecipe.apiId);
            await this.fetchRecipeDetails(newRecipe.apiId);
            await this.fetchNotes(newRecipe.apiId);
          }
        } else {
          this.resetState();
        }
      },
    },
  },
  methods: {
    resetState() {
      this.recipeDetails = null;
      this.notes = [];
      this.isRecipeSaved = false;
      this.currentApiId = null;
      this.newNoteContent = "";
    },

    async checkIfRecipeIsSaved(apiId) {
      try {
        const response = await axios.get(`/api/recipes/${apiId}`);
        this.isRecipeSaved = !!response.data;
        console.log(`Recipe ${apiId} is saved:`, this.isRecipeSaved);
      } catch (error) {
        if (error.response?.status === 404) {
          // Recipe not in database - this is normal for new API recipes
          this.isRecipeSaved = false;
        } else {
          console.error("Error checking if recipe is saved:", error);
          this.isRecipeSaved = false;
        }
      }
    },

    async fetchRecipeDetails(apiId) {
      try {
        if (this.isRecipeSaved) {
          // Load from database
          console.log("Loading recipe from database:", apiId);
          const response = await axios.get(`/api/recipes/${apiId}`);
          console.log("DB Response data:", response.data); // Debug log

          this.recipeDetails = {
            title: response.data.title,
            sourceUrl: response.data.sourceUrl,
            sourceName: response.data.sourceName,
            image: response.data.image,
            readyInMinutes: response.data.readyInMinutes,
            servings: response.data.servings,
            summary: response.data.summary,
            healthScore: response.data.healthScore,
            pricePerServing: (response.data.pricePerServing || 0) / 100,
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
            instructions: response.data.analyzedInstructions ?
                (JSON.parse(response.data.analyzedInstructions)[0]?.steps.map(
                    (step) => step.step
                ) || []) : [],
          };
        } else {
          // Load from API
          console.log("Loading recipe from API:", apiId);
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
        console.error("Error fetching recipe details:", error);
        this.recipeDetails = null;
      }
    },

    async fetchNotes(recipeId) {
      console.log("Fetching notes for recipe ID:", recipeId);
      this.notes = [];

      if (!this.isRecipeSaved) {
        console.log("Recipe not saved, no notes to fetch");
        return;
      }

      try {
        const responseRecipe = await axios.get(`/api/recipes/${recipeId}`);
        const dbRecipeId = responseRecipe.data.id;
        console.log("Fetching notes for DB recipe ID:", dbRecipeId);

        const response = await axios.get(`/api/notes/${dbRecipeId}`);
        console.log("Received notes:", response.data);
        this.notes = response.data;
      } catch (error) {
        if (error.response?.status === 404) {
          console.log("No notes found for recipe");
        } else {
          console.error("Error fetching notes:", error);
        }
        this.notes = [];
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

        const newNote = {
          id: response.data.id,
          content: response.data.content,
        };

        this.notes = [...this.notes, newNote];
        this.newNoteContent = "";
      } catch (error) {
        console.error("Error adding note:", error.response?.data || error.message);
      }
    },

    async deleteNote(noteId) {
      try {
        await axios.delete(`/api/notes/${noteId}`);
        this.notes = this.notes.filter((note) => note.id !== noteId);
      } catch (error) {
        console.error("Error deleting note:", error.response?.data || error.message);
      }
    },

    async saveToCookbook() {
      if (this.isRecipeSaved) {
        alert("Rezept ist bereits im Kochbuch gespeichert.");
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
          instructions: JSON.stringify(this.recipeDetails.instructions),
        });

        this.isRecipeSaved = true;
        alert("Rezept wurde erfolgreich gespeichert!");
        this.$emit("update-db-recipes", this.$parent.dbSearchQuery);
      } catch (error) {
        if (error.response?.status === 409) {
          alert("Rezept ist bereits gespeichert.");
          this.isRecipeSaved = true; // Update local state
        } else {
          console.error("Error saving recipe:", error);
          alert("Fehler beim Speichern des Rezepts.");
        }
      }
    },

    async deleteFromCookbook() {
      if (!this.isRecipeSaved) {
        alert("Rezept ist nicht im Kochbuch gespeichert.");
        return;
      }

      try {
        await axios.delete(`/api/recipes/delete/${this.recipe.apiId}`);
        this.isRecipeSaved = false;
        this.notes = []; // Clear notes when recipe is deleted
        alert("Rezept wurde erfolgreich gelöscht.");
        this.$emit("update-db-recipes", this.$parent.dbSearchQuery);
      } catch (error) {
        console.error("Error deleting recipe:", error);
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
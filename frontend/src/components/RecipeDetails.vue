<template>
  <div v-if="recipe" class="recipe-details">
    <h1>{{ recipe.title }}</h1>
    <button @click="saveToCookbook">Im Kochbuch Speichern</button>
    <button @click="deleteFromCookbook">Rezept löschen</button>
    <p><strong>Quelle:</strong> <a :href="recipe.sourceUrl" target="_blank">{{ recipe.sourceName }}</a></p>
    <img :src="recipe.image" :alt="recipe.title" class="recipe-image" />
    <h2>Zutaten</h2>
    <ul>
      <li v-for="ingredient in recipe.ingredients" :key="ingredient.id">
        {{ ingredient.name }} - {{ ingredient.amount }} {{ ingredient.unit }}
      </li>
    </ul>
    <h2>Zubereitung</h2>
    <ol>
      <li v-for="(step, index) in recipe.instructions" :key="index">
        {{ step }}
      </li>
    </ol>
  </div>
  <div v-else>
    <p>Kein Rezept ausgewählt.</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: ["recipe"],
  methods: {
    async saveToCookbook() {
      try {
        console.log("API ID:", this.recipe.apiId); // Debugging
        const response = await axios.post("/api/recipes/save", {
          apiId: this.recipe.apiId,
          title: this.recipe.title,
          imageUrl: this.recipe.image,
          sourceUrl: this.recipe.sourceUrl,
          instructions: this.recipe.instructions.slice(0, 1000).join("\n"),
        });
        console.log("Rezept gespeichert:", response.data);
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
        console.log("Deleting recipe with API ID:", this.recipe.apiId); // Debugging
        const response = await axios.delete(`/api/recipes/delete/${this.recipe.apiId}`);
        console.log("Response from server:", response.data); // Debugging
        alert(response.data); // Display the response message
      } catch (error) {
        console.error("Fehler beim Löschen des Rezepts:", error.response?.data || error.message);
        alert("Fehler beim Löschen des Rezepts.");
      }
    }
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
<template>
  <div class="container">
    <div class="left">
      <SearchBar v-model:searchQuery="searchQuery" @search="searchRecipes" />
      <RecipeList :recipes="recipes" @select-recipe="selectRecipe" />
    </div>
    <div class="middle">
      <RecipeDetails :recipe="selectedRecipe" />
    </div>
    <div class="right">
      <SearchBar v-model:searchQuery="dbSearchQuery" @search="searchDbRecipes" />
      <RecipeList :recipes="dbRecipes" @select-recipe="selectRecipe" />
    </div>
  </div>
</template>

<script>
import SearchBar from "./components/SearchBar.vue";
import RecipeList from "./components/RecipeList.vue";
import RecipeDetails from "./components/RecipeDetails.vue";
import axios from "axios";

export default {
  components: {
    SearchBar,
    RecipeList,
    RecipeDetails,
  },
  data() {
    return {
      searchQuery: "",
      dbSearchQuery: "",
      recipes: [],
      dbRecipes: [],
      selectedRecipe: null,
    };
  },
  methods: {
    async searchRecipes(query) {
      if (!query || query.trim() === "") {
        console.error("Suchanfrage ist leer.");
        alert("Bitte geben Sie einen Suchbegriff ein.");
        return;
      }

      try {
        console.log("API-Request-Parameter:", { query, apiKey: "e7d045456c0f40da8b6db6fe7b794d3e" });

        const response = await axios.get(
            `https://api.spoonacular.com/recipes/complexSearch`,
            {
              params: {
                query,
                apiKey: "e7d045456c0f40da8b6db6fe7b794d3e",
              },
            }
        );

        if (response.data && response.data.results) {
          this.recipes = response.data.results;
          console.log("API-Antwort:", response.data);
        } else {
          console.error("Unerwartete API-Antwort:", response.data);
          alert("Keine Ergebnisse gefunden.");
        }
      } catch (error) {
        console.error("Fehler bei der API-Anfrage:", error.response?.data || error.message);
        alert("Fehler bei der Suche. Bitte versuchen Sie es später erneut.");
      }
    },
    async searchDbRecipes(query) {
      try {
        const response = await axios.get("/api/recipes/search", {
          params: { query },
        });
        this.dbRecipes = response.data;
      } catch (error) {
        console.error("Fehler beim Abrufen der Rezepte aus der DB:", error);
      }
    },
    async selectRecipe(recipe) {
      if (recipe.apiId) {
        // Rezept aus der Datenbank
        this.selectedRecipe = { apiId: recipe.apiId };
      } else {
        // Rezept aus der API
        try {
          const response = await axios.get(
              `https://api.spoonacular.com/recipes/${recipe.id}/information`,
              {
                params: {
                  apiKey: "e7d045456c0f40da8b6db6fe7b794d3e",
                },
              }
          );
          this.selectedRecipe = {
            apiId: response.data.id,
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
      }
    },
  },
  mounted() {
    this.searchDbRecipes(""); // Load all recipes from the DB on mount
  },
};
</script>

<style>
.container {
  display: flex;
  justify-content: space-between;
}

.left,
.middle,
.right {
  flex: 1;
  margin: 10px;
}
</style>
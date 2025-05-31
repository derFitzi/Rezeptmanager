<template>
  <div class="app">
    <header class="app-header">
      <h1 class="app-title">
        Persönlicher Rezeptmanager
      </h1>
    </header>

    <div class="container">
      <div class="panel left-panel">
        <div class="panel-header">
          <h2>Rezepte Suchen</h2>
        </div>
        <div class="panel-content">
          <SearchBar v-model:searchQuery="searchQuery" @search="searchRecipes" /> <!-- Suchleiste für Rezepte -->
          <RecipeList :recipes="recipes" @select-recipe="selectRecipe" /> <!-- Liste der gefundenen Rezepte -->
        </div>
      </div>

      <div class="panelcenter middle-panel">
        <div class="panel-header">
          <h2>Rezept Details</h2>
        </div>
        <div class="panel-content">
          <RecipeDetails :recipe="selectedRecipe" @update-db-recipes="searchDbRecipes" /> <!-- Anzeige der Details des ausgewählten Rezepts -->
        </div>
      </div>

      <div class="panel right-panel">
        <div class="panel-header">
          <h2>Meine Rezepte</h2>
        </div>
        <div class="panel-content">
          <SearchBar v-model:searchQuery="dbSearchQuery" @search="searchDbRecipes" /> <!-- Suchleiste für gespeicherte Rezepte -->
          <RecipeList :recipes="dbRecipes" @select-recipe="selectRecipe" /> <!-- Liste der gespeicherten Rezepte -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SearchBar from "./components/SearchBar.vue"; // Importiert die Suchleiste-Komponente
import RecipeList from "./components/RecipeList.vue"; // Importiert die Rezeptliste-Komponente
import RecipeDetails from "./components/RecipeDetails.vue"; // Importiert die Rezeptdetails-Komponente
import axios from "axios"; // Importiert Axios für HTTP-Anfragen

export default {
  components: {
    SearchBar,
    RecipeList,
    RecipeDetails,
  },
  data() {
    return {
      searchQuery: "", // Suchanfrage für Rezepte
      dbSearchQuery: "", // Suchanfrage für gespeicherte Rezepte
      recipes: [], // Liste der gefundenen Rezepte
      dbRecipes: [], //Liste der gespeicherten Rezepte
      selectedRecipe: null, //aktuell ausgewählte Rezept
    };
  },
  methods: {
    async searchRecipes(query) {
      if (!query || query.trim() === "") {
        console.error("Suchanfrage ist leer."); // Gibt eine Fehlermeldung aus, wenn die Suchanfrage leer ist
        alert("Bitte geben Sie einen Suchbegriff ein."); // Zeigt ein Alert an
        return;
      }

      try {
        const response = await axios.get(
            `https://api.spoonacular.com/recipes/complexSearch`,
            {
              params: {
                query,
                apiKey: "e7d045456c0f40da8b6db6fe7b794d3e",
                number: 20, // Anzahl der Ergebnisse
              },
            }
        );

        if (response.data && response.data.results) {
          this.recipes = response.data.results; // Speichert die gefundenen Rezepte
        } else {
          alert("Keine Ergebnisse gefunden."); // Zeigt eine Warnung an, wenn keine Ergebnisse gefunden wurden
        }
      } catch (error) {
        console.error("Fehler bei der API-Anfrage:", error.response?.data || error.message); // Gibt eine Fehlermeldung aus
        alert("Fehler bei der Suche. Bitte versuchen Sie es später erneut."); // Zeigt ein Alert an
      }
    },
    async searchDbRecipes(query) {
      try {
        const response = await axios.get("/api/recipes/search", {
          params: { query: query || this.dbSearchQuery }, // Übergibt die Suchanfrage als Parameter
        });
        this.dbRecipes = response.data; // Speichert die gefundenen Rezepte aus der Datenbank
      } catch (error) {
        console.error("Fehler beim Abrufen der Rezepte aus der Datenbank:", error);
      }
    },
    async selectRecipe(recipe) {
      if (recipe.apiId) {
        this.selectedRecipe = { apiId: recipe.apiId }; // Speichert die API-ID des ausgewählten Rezepts
      } else {
        try {
          const response = await axios.get(
              `https://api.spoonacular.com/recipes/${recipe.id}/information`, // API-Endpunkt für Rezeptdetails
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
                    (step) => step.step // Speichert die Schritte der Anleitung
                ) || [],
          };
        } catch (error) {
          console.error("Fehler beim Abrufen des Rezepts:", error);
        }
      }
    },
  },
  mounted() {
    this.searchDbRecipes(""); // Lädt alle Rezepte aus der Datenbank beim Start
  },
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #ececec;
  color: #333;
  line-height: 1.6;
}

.app {
  min-height: 100vh;
}

.app-header {
  background: linear-gradient(135deg, #0f2aa8 0%, #8498e7 100%);
  color: white;
  padding: 1.5rem 0;
  text-align: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.app-title {
  font-size: 2.5rem;
  font-weight: 300;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.title-icon {
  font-size: 2rem;
}

.container {
  display: flex;
  margin: 0 auto;
  padding: 2rem 1rem;
  gap: 1.5rem;
  min-height: calc(100vh - 120px);
}

.panel {
  flex: 1;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  max-width: 400px;
}

.panelcenter {
  flex: 1;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.panel:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.panel-header {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
  padding: 1rem 1.5rem;
  text-align: center;
}

.panel-header h2 {
  font-size: 1.3rem;
  font-weight: 500;
}

.panel-content {
  padding: 1.5rem;
  height: calc(100% - 60px);
  overflow-y: auto;
}

.left-panel .panel-header {
  background: linear-gradient(135deg, #a3b1ec 0%, #3d5eea 100%);
}

.middle-panel .panel-header {
  background: linear-gradient(135deg, #3d5eea 0%, #3d5eea 100%);
}

.right-panel .panel-header {
  background: linear-gradient(135deg, #3d5eea 0%, #a3b1ec 100%);
}

/* Scrollbar Styling */
.panel-content::-webkit-scrollbar {
  width: 6px;
}

.panel-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.panel-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.panel-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
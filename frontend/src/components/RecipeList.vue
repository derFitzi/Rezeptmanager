<template>
  <div class="recipe-list-container">
    <div v-if="recipes.length === 0" class="empty-state">
      <span class="empty-icon">🍽️</span>
      <p>Keine Rezepte gefunden</p> <!-- Zeigt eine Nachricht an, wenn keine Rezepte vorhanden sind -->
    </div>
    <ul v-else class="recipe-list">
      <li
          v-for="recipe in recipes"
          :key="recipe.id"
      @click="onSelectRecipe(recipe)"
      class="recipe-item"
      >
      <div class="recipe-image-wrapper">
        <img
        :src="recipe.image"
        :alt="recipe.title"
        class="recipe-image"
        />
      </div>
      <div class="recipe-info">
        <h3 class="recipe-title">{{ recipe.title }}</h3>
        <div v-if="recipe.readyInMinutes" class="recipe-meta"> <!-- wenn eine Zubereitungszeit vorhanden ist wird sie angezeigt -->
            <span class="meta-item">
              <span class="meta-icon">⏱️</span>
              {{ recipe.readyInMinutes }} Min
            </span>
        </div>
      </div>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: ["recipes"], // Übergibt die Liste der Rezepte als Eigenschaft
  methods: {
    onSelectRecipe(recipe) {
      this.$emit("select-recipe", recipe); // Sendet das ausgewählte Rezept als Ereignis an App.vue
    },
  },
};
</script>

<style scoped>
.recipe-list-container {
  height: 100%;
}

.empty-state {
  text-align: center;
  padding: 3rem 1rem;
  color: #6b7280;
}

.empty-icon {
  font-size: 3rem;
  display: block;
  margin-bottom: 1rem;
}

.recipe-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.recipe-item {
  display: flex;
  align-items: center;
  padding: 1rem;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  gap: 1rem;
}

.recipe-item:hover {
  background: #fff;
  border-color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

.recipe-image-wrapper {
  flex-shrink: 0;
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  background: #e2e8f0;
}

.recipe-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.recipe-item:hover .recipe-image {
  transform: scale(1.1);
}

.recipe-info {
  flex: 1;
  min-width: 0;
}

.recipe-title {
  font-size: 1rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 0.25rem 0;
  line-height: 1.3;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.recipe-meta {
  display: flex;
  gap: 1rem;
  margin-top: 0.5rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.875rem;
  color: #6b7280;
}

.meta-icon {
  font-size: 0.75rem;
}

@media (max-width: 480px) {
  .recipe-item {
    padding: 0.75rem;
    gap: 0.75rem;
  }

  .recipe-image-wrapper {
    width: 50px;
    height: 50px;
  }

  .recipe-title {
    font-size: 0.9rem;
  }

  .meta-item {
    font-size: 0.8rem;
  }
}
</style>
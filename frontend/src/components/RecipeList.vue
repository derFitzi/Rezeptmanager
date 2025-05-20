<template>
  <ul class="recipe-list">
    <li
        v-for="recipe in recipes"
        :key="recipe.id"
        @click="onSelectRecipe(recipe)"
        class="recipe-item"
    >
      <img :src="recipe.image" :alt="recipe.title" class="recipe-image"/>
      {{ recipe.title }}
    </li>
  </ul>
</template>

<script>
export default {
  props: ["recipes"],
  methods: {
    onSelectRecipe(recipe) {
      this.$emit("select-recipe", recipe); // Übergibt das gesamte Rezept-Objekt
    },
  },
};
</script>

<style>
.recipe-list {
  list-style: none;
  padding: 0;
}

.recipe-item {
  cursor: pointer;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.recipe-item:hover {
  background-color: #f0f0f0;
}
</style>
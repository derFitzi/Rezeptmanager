<template>
  <div class="search-bar">
    <div class="search-input-wrapper">
      <input
          type="text"
          :value="searchQuery"
      @input="$emit('update:searchQuery', $event.target.value)"
      @keyup.enter="onSearch"
      placeholder="Rezept suchen..."
      class="search-input"
      />
      <button @click="onSearch" class="search-button"> <!-- Führt die Suchfunktion aus, wenn der Button geklickt wird -->
        Suchen
      </button>
    </div>
  </div>
</template>

<script>
export default {
  props: ["searchQuery"], // Übergibt die Suchanfrage als Eigenschaft
  methods: {
    onSearch() {
      this.$emit("search", this.searchQuery); // Sendet die Suchanfrage als Ereignis an den Elternkomponenten
    },
  },
};
</script>

<style scoped>
.search-bar {
  margin-bottom: 1.5rem;
}

.search-input-wrapper {
  display: flex;
  gap: 0.5rem;
  align-items: stretch;
}

.search-input {
  flex: 1;
  padding: 0.75rem 1rem;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: #fff;
  outline: none;
}

.search-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  transform: translateY(-1px);
}

.search-input::placeholder {
  color: #9ca3af;
}

.search-button {
  padding: 0.75rem 1.5rem;
  background: linear-gradient(135deg, #3d5eea 0%, #8498e7 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  white-space: nowrap;
}

.search-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.search-button:active {
  transform: translateY(0);
}

.search-icon {
  font-size: 0.9rem;
}

@media (max-width: 480px) {
  .search-input-wrapper {
    flex-direction: column;
  }

  .search-button {
    justify-content: center;
  }
}
</style>
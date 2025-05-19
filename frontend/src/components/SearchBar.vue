<template>
  <div class="search-bar">
    <input
        type="text"
        :value="searchQuery"
        @input="$emit('update:searchQuery', $event.target.value)"
    />

    <button @click="onSearch">Suchen</button>
  </div>
</template>

<script>
export default {
  props: ["searchQuery"],
  methods: {
    onSearch() {
      this.$emit("search", this.searchQuery);
    },
  },
};
</script>

<style>
.search-bar {
  margin-bottom: 20px;
  text-align: center;
}
.search-bar input {
  width: 70%;
  padding: 10px;
  font-size: 16px;
}
.search-bar button {
  padding: 10px 20px;
  font-size: 16px;
  margin-left: 10px;
}
</style>
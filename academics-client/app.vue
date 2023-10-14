<template>
  <div v-if="error">
    Error: {{ error.message }}
  </div>
  <div v-else>
    <h2>Students</h2>
    <div>{{ students }}</div>
    <button @click.prevent="refresh">Refresh Data</button>
  </div>
</template>
<script setup>
  const config = useRuntimeConfig();
  const api = config.public.API_URL;
  const { data: students, error, refresh } = await useFetch(`${api}/students`)
</script>
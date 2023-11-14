<template>
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="/">Home</nuxt-link> | <nuxt-link to="/create">Create a New Teacher</nuxt-link>
        <h2>Teachers</h2>
        <table>
            <tr>
                <th>Username</th>
                <th>Name</th>
                <th>E-mail</th>
                <th>Office</th>
                <th>actions</th>
            </tr>
            <tr v-for="teacher in teachers">
                <td>{{ teacher.username }}</td>
                <td>{{ teacher.name }}</td>
                <td>{{ teacher.email }}</td>
                <td>{{ teacher.office }}</td>
                <nuxt-link :to="`/students/${teacher.username}`">Details</nuxt-link>
            </tr>
        </table>
    </div>
    <button @click.prevent="refresh">Refresh Data</button>
</template>
<script setup>
    const config = useRuntimeConfig()
    const api = config.public.API_URL
    const { data: teachers, error, refresh } = await useFetch(`${api}/teachers`)
</script>
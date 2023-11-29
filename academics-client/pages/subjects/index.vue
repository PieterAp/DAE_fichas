<template>
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="/">Home</nuxt-link> | <nuxt-link to="/create">Create a New Subject</nuxt-link>
        <h2>Subjects</h2>
        <table>
            <tr>
                <th>Username</th>
                <th>Name</th>
                <th>E-mail</th>
                <th>Office</th>
                <th>actions</th>
            </tr>
            <tr v-for="subject in subjects">
                <td>{{ subject.username }}</td>
                <td>{{ subject.name }}</td>
                <td>{{ subject.email }}</td>
                <td>{{ subject.office }}</td>
                <nuxt-link :to="`/students/${subject.username}`">Details</nuxt-link>
            </tr>
        </table>
    </div>
    <button @click.prevent="refresh">Refresh Data</button>
</template>
<script setup>
    const config = useRuntimeConfig()
    const api = config.public.API_URL
    const { data: subjects, error, refresh } = await useFetch(`${api}/subjects`)
</script>
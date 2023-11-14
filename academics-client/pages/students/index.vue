<template>
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="/">Home</nuxt-link> | <nuxt-link to="/students/create">Create a New Student</nuxt-link>
        <h2>Students</h2>
        <table>
            <tr>
                <th>Username</th>
                <th>Name</th>
                <th>E-mail</th>
                <th>Curso</th>
                <th>actions</th>
            </tr>
            <tr v-for="student in students">
                <td>{{ student.username }}</td>
                <td>{{ student.name }}</td>
                <td>{{ student.email }}</td>
                <td>{{ student.courseName }}</td>
                <nuxt-link :to="`/students/${student.username}`">Details</nuxt-link>
            </tr>
        </table>
    </div>
    <button @click.prevent="refresh">Refresh Data</button>
</template>
<script setup>
    const config = useRuntimeConfig()
    const api = config.public.API_URL
    const { data: students, error, refresh } = await useFetch(`${api}/students`)
</script>
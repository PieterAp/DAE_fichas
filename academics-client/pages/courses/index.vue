<template>
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="/">Home</nuxt-link> | <nuxt-link to="/create">Create a New Course</nuxt-link>
        <h2>Courses</h2>
        <table>
            <tr>
                <th>Code</th>
                <th>Name</th>
                <th>actions</th>
            </tr>
            <tr v-for="course in courses">
                <td>{{ course.code }}</td>
                <td>{{ course.name }}</td>
                <nuxt-link :to="`/students/${course.code}`">Details</nuxt-link>
            </tr>
        </table>
    </div>
    <button @click.prevent="refresh">Refresh Data</button>
</template>
<script setup>
    const config = useRuntimeConfig()
    const api = config.public.API_URL
    const { data: courses, error, refresh } = await useFetch(`${api}/courses`)
</script>
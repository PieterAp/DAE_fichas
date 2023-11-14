<template>
    <div v-if="student">
        <h2>Details of {{ username }}</h2>
        {{ student }}
    </div>
    <div v-if="subjects">
        <h2>Enrolled in:</h2>
        {{ subjects }}
    </div>
    <h2>Error messages:</h2>
    {{ messages }}
</template>
<script setup>
    const route = useRoute()
    const username = route.params.username

    const config = useRuntimeConfig()
    const api = config.public.API_URL
    const { data: student, error: studentErr } = await
    useFetch(`${api}/students/${username}`)
    const { data: subjects, error: subjectsErr } = await
    useFetch(`${api}/students/${username}/subjects`)
    
    const messages = ref([])
    if (studentErr.value) messages.value.push(studentErr.value)
    if (subjectsErr.value) messages.value.push(subjectsErr.value)
</script>
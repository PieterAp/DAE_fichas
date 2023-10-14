<template>
    <form @submit.prevent="create">
        <div>Username:
            <input v-model="studentForm.username" type="text">
        </div>
        <div>Password:
            <input v-model="studentForm.password" type="password">
        </div>
        <div>Name:
            <input v-model="studentForm.name" type="text">
        </div>
        <div>E-mail:
            <input v-model="studentForm.email" type="text">
        </div>
        <div>Course:
            <select v-model="studentForm.courseCode">
                <option v-for="course in courses" :value="course.code">
                    {{ course.name }}
                </option>
            </select>
        </div>
        <button type="reset">RESET</button>
        <button type="submit">CREATE</button>
        <nuxt-link to="/">Return</nuxt-link>
    </form>
    {{ message }}
</template>
<script setup>
const studentForm = reactive({
    username: '',
    password: '',
    email: '',
    name: '',
    courseCode: ''
})
const message = ref('')
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: courses } = await useFetch(`${api}/courses`)
async function create() {
    const requestOptions = {
        method: 'POST',
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(studentForm)
    }
    const { error } = await useFetch(`${api}/students`, requestOptions)
    if (!error.value) navigateTo('/')
    message.value = error.value
}
</script>
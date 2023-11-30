<template>
    <form @submit.prevent="update">
        <div>Username:
            <input disabled="true" v-model="studentForm.username" type="text">
        </div>
        <div>Name:
            <input v-model="studentForm.name" type="text">
        </div>
        <div>Password:
            <input v-model="studentForm.password" type="text">
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
        <button type="submit">Update</button>
        <nuxt-link :to="`/students/${student.username}`">Return</nuxt-link>
    </form>
    {{ message }}
</template>
<script setup>
const route = useRoute()
const username = route.params.username
const studentForm = reactive({
    username: '',
    email: '',
    name: '',
    courseCode: ''
})
const message = ref('')
const config = useRuntimeConfig()
const api = config.public.API_URL

const { data: student, error: studentErr } = await
useFetch(`${api}/students/${username}`)
if (studentErr.value) messages.value.push(studentErr.value)
console.log(student);
console.log(student.value.username);

studentForm.username = student.value.username;
studentForm.email = student.value.email;
studentForm.name = student.value.name;
studentForm.password = null;
studentForm.courseCode = student.value.courseCode;

const { data: courses } = await useFetch(`${api}/courses`)
async function update() {
    const requestOptions = {
        method: 'PUT',
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(studentForm)
    }
    const { error } = await useFetch(`${api}/students/${username}`, requestOptions)
    if (!error.value) navigateTo('/')
    message.value = error.value
}
</script>
<template>
    <form @submit.prevent="create">
        <div>Username:
            <input v-model.trim="studentForm.username" type="text">
            <span v-if="studentForm.username !== null && !isUsernameValid" class="error">
                ERROR: {{ formFeedback.username }}
            </span>
        </div>
        <div>Password:
            <input v-model.trim="studentForm.password" type="password">
            <span v-if="studentForm.password !== null && !isPasswordValid" class="error">
                ERROR: {{ formFeedback.password }}
            </span>
        </div>
        <div>Name:
            <input v-model.trim="studentForm.name" type="text">
            <span v-if="studentForm.name !== null && !isNameValid" class="error">
                ERROR: {{ formFeedback.name }}
            </span>
        </div>
        <div>E-mail:
            <input v-model.trim="studentForm.email" type="email">
            <span v-if="studentForm.email !== null && !isEmailValid" class="error">
                ERROR: {{ formFeedback.email }}
            </span>
        </div>
        <div>Course:
            <select v-model="studentForm.courseCode">
                <option value="">--- Please select Course ---</option>
                <option v-for="course in courses" :value="course.code">
                    {{ course.name }}</option>
            </select>
            <span v-if="studentForm.courseCode !== null && !isCourseValid" class="error">
                ERROR: {{ formFeedback.courseCode }}
            </span>
        </div>
        <button type="reset">RESET</button>
        <button type="submit" :disabled="!isFormValid">CREATE</button>
        <nuxt-link to="/">Return</nuxt-link>
    </form>
    {{ message }}
</template>
<style scoped>
.error {
    color: red
}
</style>
<script setup>
const studentForm = reactive({
    username: null,
    password: null,
    email: null,
    name: null,
    courseCode: null
})
const formFeedback = reactive({
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
// Field validation rules...
const isUsernameValid = computed(() => {
    if (!studentForm.username) {
        formFeedback.username = 'username is required'
        return false
    }
    if (studentForm.username.length < 3) {
        formFeedback.username = 'username must be at least 3 characters'
        return false
    }
    if (studentForm.username.length > 15) {
        formFeedback.username = 'username must be at most 15 characters'
        return false
    }
    return true
})
const isPasswordValid = computed(() => {
    if (!studentForm.password) {
        formFeedback.password = 'password is required'
        return false
    }
    if (!(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/.test(studentForm.password))) {
        formFeedback.password = 'password is too weak'
        return false
    }
    return true
})
const isNameValid = computed(() => {
    if (!studentForm.name) {
        formFeedback.name = 'name is required'
        return false
    }
    if (studentForm.name.length < 3) {
        formFeedback.name = 'name must be at least 3 characters'
        return false
    }
    if (studentForm.name.length > 15) {
        formFeedback.name = 'name must be at most 15 characters'
        return false
    }
    return true
})
const isEmailValid = computed(() => {
    if (!studentForm.email) {
        formFeedback.email = 'email is required'
        return false
    }
    if (!(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(studentForm.email))) {
        formFeedback.email = 'email must be the correct format'
        return false
    }
    return true
})
const isCourseValid = computed(() => {
    if (!studentForm.courseCode) {
        formFeedback.courseCode = 'course is required'
        return false
    }
    // TODO
    return true
})
// Form validation rule...
const isFormValid = computed(() => {
    return isUsernameValid.value
        && isPasswordValid.value
        && isNameValid.value
        && isEmailValid.value
        && isCourseValid.value
})
async function create() {
    const requestOptions = {
        method: 'POST',
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(studentForm)
    }
    const { error } = await useFetch(`${api}/students`, requestOptions)
    if (!error.value)
        navigateTo('/students')
    message.value = error.value
}
</script>   
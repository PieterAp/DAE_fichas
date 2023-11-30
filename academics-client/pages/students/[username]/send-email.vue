<template>
    <div>
        <nuxt-link :to="`/students`">
            <button>Go to Students</button></nuxt-link>
        <nuxt-link :to="`/students/${username}`">
            <button>Go to Student Details</button></nuxt-link>
        <div v-if="student">
            <h1>Send an E-mail to Student {{ student.name }}</h1>
            <form @submit.prevent="send">
                <div>Subject:
                    <input v-model.trim="formData.subject">
                    <span v-if="formData.subject !== null && !isSubjectValid" class="error">
                        ERROR: {{ formFeedback.subject }}</span>
                </div>
                <div>Message:
                    <input v-model="formData.message">
                    <span v-if="formData.message !== null && !isMessageValid" class="error">
                        ERROR: {{ formFeedback.message }}</span>
                </div>
                <button type="submit" :disabled="!isFormValid">SEND</button>
            </form>
        </div>
    </div>
    <div v-if="messages.length > 0">
        <h2>Error message:</h2>
        {{ messages }}
    </div>
</template>
<style>
.error {
    color: red
}
</style>
<script setup>
const formData = reactive({
    subject: null,
    body: null
})
const formFeedback = reactive({})
const messages = ref([])
const route = useRoute()
const username = route.params.username
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: student, error: studentError } = await useFetch(
    `${api}/students/${username}`)
if (studentError.value)
    messages.value.push(studentError.value)
const isSubjectValid = computed(() => {
    if (!formData.subject) {
        formFeedback.subject = 'message subject is required'
        return false
    }
    return true
})
const isMessageValid = computed(() => {
    if (!formData.message) {
        formFeedback.message = 'message body is required'
        return false
    }
    return true
})
const isFormValid = computed(() => {
    return isSubjectValid.value
        && isMessageValid.value
})
async function send() {
    const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData)
    }
    const { error: sendError } = await useFetch(
        `${api}/students/${username}/email/send`, requestOptions)
    if (sendError.value) {
        messages.value.push(sendError.value)
        return
    }
    navigateTo("/students")
}
</script>
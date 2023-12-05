<template>
    <div>
        <h1>Login Form</h1>
        <div>Username:
            <input v-model="loginFormData.username">
        </div>
        <div>Password:
            <input v-model="loginFormData.password">
        </div>
        <button @click="login">LOGIN</button>
        <button @click="reset">RESET</button>
    </div>
    <div v-if="token">
        <h2>API Request Form</h2>
        <div>Path:
            <input v-model="apiFormData.path">
        </div>
        <div>Token: {{ token }}</div>
        <button @click="sendRequest">SEND REQUEST</button>
    </div>
    <div v-if="messages.length > 0">
        <h2>Messages</h2>
        <div v-for="message in messages">
            <pre>{{ message }}</pre>
        </div>
    </div>
</template>
<script setup>
const config = useRuntimeConfig()
const api = config.public.API_URL
const loginFormData = reactive({
    username: null,
    password: null
})
const apiFormData = reactive({
    path: "students"
})
const token = ref(null)
const messages = ref([])
async function login() {
    const { data, error } = await useFetch(`${api}/auth/login`, {
        method: "post",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(loginFormData)
    })
    if (error.value) {
        messages.value.push({ error: error.value.message })
    }
    if (data.value) {
        token.value = data.value
        messages.value.push({ token: token.value })
    }
}
function reset() {
    token.value = null
    messages.value = []
}
async function sendRequest() {
    const { data, error } = await useFetch(`${api}/${apiFormData.path}`, {
        method: 'get',
        headers: {
            'Accept': 'application/json',
            'Authorization': 'Bearer ' + token.value
        }
    })
    if (error.value) {
        messages.value.push({ error: error.value.message })
    }

    if (data.value) {
        messages.value.push({ payload: data.value })
    }
}
</script>
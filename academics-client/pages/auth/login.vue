<template>
  <h1>Login Form</h1>
  <div>Username:
    <input v-model="loginFormData.username">
  </div>
  <div>Password:
    <input v-model="loginFormData.password">
  </div>
  <button @click="login">LOGIN</button>
  <div v-if="token">
    <div>Token: {{ token }}</div>
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
const loginFormData = ref({
  username: "",
  password: ""
})
import {useAuthStore} from "~/store/auth-store.js"
//const token = ref(null)
//const user = ref(null)
const authStore = useAuthStore()
const {token, user} = storeToRefs(authStore)

const messages = ref([])
async function login() {
  const { data, error } = await useFetch(`${api}/auth/login`, {
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    },
    body: loginFormData.value
  })
  if (error.value) {
    messages.value.push({ tokenError: error.value.message })
    return
  }
  token.value = data.value
  const { data: userData, error: userError } = await useFetch(`${api}/auth/user`, {
    method: 'get',
    headers: {
      'Accept': 'application/json',
      'Authorization': 'Bearer ' + token.value
    }
  })
  if (userError.value) {
    messages.value.push({ userDataError: userError.value.message })
    return
  }
  user.value = userData.value
}
</script>
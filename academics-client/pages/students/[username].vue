<template>
    <nuxt-link to="/students">Back</nuxt-link> | <nuxt-link to="/">Home</nuxt-link> | <nuxt-link to="/students/create">Create a New Student</nuxt-link>
    <div v-if="student">
        <h2>Details of {{ username }}</h2>
        <div style="padding-left: 14px;">
            <p><b>Username:</b> {{ student.username }}</p>
            <p><b>Name:</b> {{ student.name }}</p>
            <p><b>E-mail:</b> {{ student.email }}</p>
        </div>
    </div>
    <div v-if="subjects">
        <h2>Enrolled in course:</h2>
        <div style="padding-left: 14px;">
            {{ student.courseName }} ({{ student.courseCode }})
        </div>
    </div>
    <div v-if="subjects">
        <h2>Enrolled in subjects:</h2>
        <div v-for="subject in subjects" style="display:inline-block; float: left;">
            <div style="margin: 7px; padding: 0px; border-color: black; border: 1px solid;">
                <div style="padding-left: 14px; padding-right: 14px;">
                    <p><b>Code:</b> {{ subject.code }}</p>
                    <p><b>Name:</b> {{ subject.name }}</p>
                    <p><b>Course year:</b> {{ subject.courseYear }}</p>
                    <p><b>Scholar year:</b> {{ subject.scholarYear }}</p>
                    <p><b>Course name:</b> {{ subject.courseName }}</p>
                    <div style="text-align: center;">
                        <button @click.prevent="unroll(subject.code)" style="margin:5px;">Unroll Student from this subject</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div style="display: table; clear: both;">
        <button @click.prevent="enroll(selectedSubjectCode)" style="margin:5px;">Enroll student into this subject: </button>
        <select v-model="selectedSubjectCode">
            <option v-for="subject in availableToEnroll" :value="subject.code">
                {{ subject.name }}
            </option>
        </select>
    </div>
    <div style="display: table; clear: both;">
        <h2>Error messages:</h2>
        {{ messages }}
    </div>
</template>
<script setup>
    const selectedSubjectCode = null;
    const route = useRoute()
    const username = route.params.username

    const config = useRuntimeConfig()
    const api = config.public.API_URL
    const { data: student, error: studentErr } = await
    useFetch(`${api}/students/${username}`)
    const { data: subjects, error: subjectsErr } = await
    useFetch(`${api}/students/${username}/subjects`)
    const { data: availableToEnroll, error: availableToEnrollERR } = await
    useFetch(`${api}/students/${username}/subjectsAvailable`)
    
    const messages = ref([])
    if (studentErr.value) messages.value.push(studentErr.value)
    if (subjectsErr.value) messages.value.push(subjectsErr.value)
    if (availableToEnroll.value) messages.value.push(availableToEnrollERR.value)

    async function unroll(subjectCode) {
        const requestOptions = {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
        }
        const { error } = await useFetch(`${api}/students/${this.student.username}/unroll/${subjectCode}`, requestOptions)
        
        if (!error.value) {
            const index = this.subjects.findIndex(x => x.code === subjectCode);
            this.subjects.splice(index,1);
        }else{
            console.log(error.value);
        }
    }

    async function enroll(subjectCode) {
        const requestOptions = {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
        }
        const { error } = await useFetch(`${api}/students/${this.student.username}/enroll/${this.selectedSubjectCode}`, requestOptions)
        
        if (!error.value) {
            const { data: tempSubjects, error: subjectsErr } = await
            useFetch(`${api}/students/${username}/subjects`)

            console.log("here is some special candy");
            console.log(this.subjects);
            console.log("some moooore");
            console.log(tempSubjects.value);
            this.subjects = tempSubjects.value;
        }else{
            console.log(error.value);
        }
    }
</script>
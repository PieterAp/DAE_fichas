<template>
    <nuxt-link to="/students">Back</nuxt-link> | <nuxt-link to="/">Home</nuxt-link>
    <div v-if="student">
        
        <h2>Details of {{ username }} (<nuxt-link :to="`/students/${student.username}/edit`">Update</nuxt-link> | <a href="" @click.prevent="deleteStudent"> Delete</a>) </h2> 
        <div style="padding-left: 14px;">
            <p><b>Username:</b> {{ student.username }}</p>
            <p><b>Name:</b> {{ student.name }}</p>
            <p><b>E-mail:</b> {{ student.email }}</p>
        </div>
    </div>
    <div v-if="student">
        <h2>Enrolled in course:</h2>
        <div style="padding-left: 14px;">
            {{ student.courseName }} ({{ student.courseCode }})
        </div>
    </div>
    <div v-if="student.subjects">
        <h2>Enrolled in subjects:</h2>
        <div v-for="subject in student.subjects" style="display:inline-block; float: left;">
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

    const { data: availableToEnroll, error: availableToEnrollERR } = await
    useFetch(`${api}/students/${username}/subjectsAvailable`)
    
    const messages = ref([])
    if (studentErr.value) messages.value.push(studentErr.value)
    if (availableToEnroll.value) messages.value.push(availableToEnrollERR.value)

    async function unroll(subjectCode) {
        const requestOptions = {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
        }
        const { error } = await useFetch(`${api}/students/${username}/unroll/${subjectCode}`, requestOptions)
        
        if (!error.value) {
            const index = this.student.subjects.findIndex(x => x.code === subjectCode);
            const tempSubject = this.student.subjects[index];
            this.availableToEnroll.push({code: tempSubject.code,
                                         courseCode: tempSubject.courseCode,
                                         courseName: tempSubject.courseName,
                                         courseYear: tempSubject.courseYear,
                                         name: tempSubject.name,
                                         scholarYear: tempSubject.scholarYear})
            this.student.subjects.splice(index,1);
        }else{
            alert("An error occurred while processing your request.");
            messages.value.push(error.value);
            console.log(error.value);
        }
    };

    async function enroll(subjectCode) {
        const requestOptions = {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
        }
        const { error } = await useFetch(`${api}/students/${username}/enroll/${this.selectedSubjectCode}`, requestOptions)
        
        if (!error.value) {
            // add to the list of enrolled subjects
            const indexDropDown = this.availableToEnroll.findIndex(x => x.code === subjectCode);

            const tempSubject = this.availableToEnroll[indexDropDown];
            this.student.subjects.push({code: tempSubject.code,
                                courseCode: tempSubject.courseCode,
                                courseName: tempSubject.courseName,
                                courseYear: tempSubject.courseYear,
                                name: tempSubject.name,
                                scholarYear: tempSubject.scholarYear})

            this.availableToEnroll.splice(indexDropDown,1);
        }else{
            alert("An error occurred while processing your request.");
            messages.value.push(error.value);
            console.log(error.value);
        }
    };
    
    async function deleteStudent() {
        const requestOptions = {
            method: 'DELETE',
            headers: { "Content-Type": "application/json" },
        }
        const { error } = await useFetch(`${api}/students/${username}`, requestOptions)
        
        if (!error.value) {
            navigateTo('/students');
        }else{
            alert("An error occurred while processing your request.");
            messages.value.push(error.value);
            console.log(error.value);
        }
    }

</script>
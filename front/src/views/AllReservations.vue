<template>
  
</template>

<script>
import axios from 'axios';
export default {
    name: "AllReservations",
    components: {

    },
    data(){
        return {
            logged: "COTTAGE_OWNER",
            data: {}
        }
    },
    methods: {

    },
    mounted(){
        //posto je univerzalna komponenta sada ce na osnovu uloge ulogovanog da se poziva razliciti ajax poziv
        //verovatno ce se uloga dobavljati iz LocalStorage-a
        if (this.logged === "COTTAGE_OWNER") {
            axios.get('api/cottage-owner/all-reservations/' + "srdjan@gmail.com").then((response) => {
                this.data = response.data
            }).catch((error) => {
                console.log('Error happened: ' + error.name)
            });
        }

    }
}
</script>

<style>

</style>
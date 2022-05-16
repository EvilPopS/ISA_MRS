<template>
    <ReservationsGallery :reservations="this.reservations"/>
</template>

<script>
    import axios from 'axios';
    import ReservationsGallery from '@/components/ReservationsGallery.vue';
     
    export default {
        name: "ClientUpcomingReservations",
        components: {
            ReservationsGallery
        },
        data() {
            return {
                reservations: [] 
            };
        },
        created() {
            axios.get("api/client/upcoming-reservations", {headers: {'authorization': window.localStorage.getItem("token") }})
                .then((response) => {
                    this.reservations = response.data;
                }
            );
        },
    }
</script>

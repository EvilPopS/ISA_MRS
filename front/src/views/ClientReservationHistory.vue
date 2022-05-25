<template>
    <ReservationsGallery 
        :reservations="this.reservations"
        :isHistoryRes="true"
    />
</template>

<script>
    import axios from 'axios';
    import ReservationsGallery from '@/components/ReservationsGallery.vue';
     
    export default {
        name: "ClientReservationHistory",
        components: {
            ReservationsGallery
        },
        data() {
            return {
                reservations: [] 
            };
        },
        created() {
            axios.get("api/client/reservation-history", {headers: {'authorization': window.localStorage.getItem("token") }})
                .then((response) => {
                    this.reservations = response.data;
                }
            );
        },
    }
</script>

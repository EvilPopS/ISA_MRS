<template>
    <ReservationsGallery 
        :reservations="this.reservations"
        :isHistoryRes="false"
        @cancel-reservation="cancelReservation"
    />

    <SuccessPopUp v-show="succPopUpVisible"
        @close = closeSuccPopUp
        :mess = succMessage
    />
</template>

<script>
    import axios from 'axios';
    import ReservationsGallery from '@/components/ReservationsGallery.vue';
    import SuccessPopUp from '@/components/SuccessPopUp.vue';

    export default {
        name: "ClientUpcomingReservations",
        components: {
            ReservationsGallery,
            SuccessPopUp
        },
        data() {
            return {
                reservations: [],

                succPopUpVisible: false,
                succMessage: "The reservation was successfully canceled."
            };
        },
        methods: {
            cancelReservation(resId) {
                axios.put("api/client/cancel-reservation/" + resId, {}, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then(() => {
                        this.succPopUpVisible = true;
                    });
            },
            closeSuccPopUp() {
                this.succPopUpVisible = false;
                this.$router.go(); 
            }
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

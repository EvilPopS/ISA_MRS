<template>
    <div>
        <div class="row justify-content-center actions-cont">
            <div class="action-card-style" @click="makeReservation(reserv.id)" v-for="reserv in this.reservations" :key="reserv.id">
                <label>Reservation starts on:</label>
                <p>{{reserv.startDate}}</p>
                <label>Reservation ends on:</label>
                <p>{{this.reserv.endDate}}</p>
                <label>Reservation price per day:</label>
                <p>{{this.reserv.newPrice}}€/day <s>{{this.oldPrice}}</s></p>
                <label>Additional services:</label>
                <p>{{this.reserv.actionServices}}</p>
            </div> 
        </div>
        <div id="btn-bar" class="row form-style justify-content-center">
            <div class="col-sm-1">
                <button id="back-btn" class="btns-style" @click="$emit('close')">Back</button>
            </div>
        </div>

        <SuccessPopUp v-show="successPopUpVisible"
            @close = closeSuccPopUp
            :mess = succMessage
        />
    </div>
</template>

<script>
    import axios from 'axios';
    import SuccessPopUp from "@/components/SuccessPopUp.vue";

    export default {
        name: "RentalActionReservations",
        components: {
            SuccessPopUp
        },
        props: {
            oldPrice: Number,
            reservations: Array
        },
        data() {
            return {
                succMessage: "The reservation was successful!",
                successPopUpVisible: false
            }
        },
        methods: {
            makeReservation(resId) {
                axios.put("api/client/make-action-reservation/" + resId, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then(() => {
                        this.successPopUpVisible = true;
                    }).catch(() => {});
            },
            closePopUp() {
                this.errorPopUpVisible = false;
            },
            closeSuccPopUp() {
                this.successPopUpVisible = false;
                this.$emit('close');
            }
        }
    }

</script>

<style scoped>
    .actions-cont {
        height: 83vh;
        overflow-y: auto;
        padding: 30px 0;
    }

    #btn-bar {
        height: 7vh;
        background: linear-gradient(rgb(236, 233, 233), white, rgb(236, 233, 233));
        border-radius: 0 0 20px 20px;
    }

    .form-style label {
        margin-left: 12px;
        margin-top: 15px;
        font-size: 14px;
    }

    #make-res-btn {
        background-color: rgb(22, 102, 6);
    }

    #back-btn {
        background-color: rgb(58, 63, 57);
    }

    .btns-style {
        margin-top: 9px;
        padding: 4px 15px;
        border-radius: 20px;
        color: white;
    }

    .action-card-style {
        border: 1px solid black;
        border-radius: 20px;
        width: 300px;
        height: 290px;
        padding-top: 20px;
        margin: 20px;
    }

    .action-card-style:hover {
        margin: 12px 12px;
        width: 316px;
        height: 306px;
        background-color: rgba(245, 238, 238, 0.8);
    }

    .action-card-style label {
        font-size: 12px;
        color: rgb(16, 92, 9);
        border-top: 1px solid black;
        margin: 0;
    }

    .action-card-style p {
        font-weight: bold;
    }
</style>
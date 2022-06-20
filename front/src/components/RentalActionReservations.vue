<template>
    <div>
        <div class="row justify-content-center modal-info">
            Click on the action reservation card which you want to reserve for yourself.
        </div>
        <div class="row justify-content-center actions-cont">
            <p v-show="this.reservations.length === 0" id="no-reses-label">There are no action reservations to be displayed!</p>
            <div class="action-card-style" @click="confirmationForRes(reserv.id, reserv.price, reserv.startDate, reserv.endDate)" v-for="reserv in this.reservations" :key="reserv.id">
                <label>Reservation starts on:</label>
                <p>{{formatDateStr(reserv.startDate)}}</p>
                <label>Reservation ends on:</label>
                <p>{{formatDateStr(reserv.endDate)}}</p>
                <label>Reservation price per day:</label>
                <p>{{reserv.price}} €/day <s>{{this.oldPrice}}</s></p>
                <label>Additional services:</label>
                <p>{{reserv.additionalServices === null ? "None" : reserv.additionalServices}}</p>
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
        
        <ErrorPopUp v-show="errorPopUpVisible" 
            @close = closePopUp
            :mess = errMessage
        /> 

        <ConfirmationPopUp v-show="confirmPopUpVisible"
            :title="'Confirmation'"
            :message="confMessage"
            @modal-closed="closeConfirmationPopUp"
            @confirmed-event="makeReservation"
        />
    </div>
</template>

<script>
    import axios from 'axios';
    import SuccessPopUp from "@/components/SuccessPopUp.vue";
    import ConfirmationPopUp from "@/components/ConfirmationPopUp.vue";
    import ErrorPopUp from "@/components/ErrorPopUp.vue";

    export default {
        name: "RentalActionReservations",
        components: {
            SuccessPopUp,
            ConfirmationPopUp,
            ErrorPopUp
        },
        props: {
            oldPrice: String,
            reservations: Array
        },
        data() {
            return {
                succMessage: "The reservation was successful!",
                successPopUpVisible: false,

                confirmPopUpVisible: false,
                confMessage: "",

                selectedResId: -1,

                errorPopUpVisible: false,
                errMessage: ""
            }
        },
        methods: {
            confirmationForRes(resId, reservPrice, startDate, endDate) {
                this.selectedResId = resId;

                axios.get("api/loyalty-program/get-client-discount", {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then((response) => {
                        let discount = response.data;
                        
                        let numOfDays = Math.ceil((new Date(endDate) - new Date(startDate))/86400000);
                        let money = numOfDays*reservPrice;

                        let totalPrice = money - money*discount/100;
                        this.confMessage = "You are about to submit payment of " + 
                                            totalPrice + 
                                            "€ in total. Are you sure you want to continue?";
                        this.confirmPopUpVisible = true;
                    }
                );
            },
            makeReservation() {
                axios.put("api/client/make-action-reservation/" + this.selectedResId, {}, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then(() => {
                        this.successPopUpVisible = true;
                        this.$emit('update-action-reservations', this.selectedResId);
                    }).catch(() => {
                        if (err.response.status === 406) {
                            this.errMessage = "You have more than 2 penalties at this moment and therefore you cannot make reservations!";
                            this.errorPopUpVisible = true;
                        } else if (err.response.status == 409){
                            this.errMessage = "Conflict situation. Please try again later..";
                            this.errorPopUpVisible = true;
                        } else {
                             this.errMessage = "Something went wrong... Please try again later!";
                            this.errorPopUpVisible = true;
                        }

                    });
            },
            closePopUp() {
                this.errorPopUpVisible = false;
            },
            closeSuccPopUp() {
                this.successPopUpVisible = false;
                this.$emit('close');
            },
            formatDateStr(dateStr) {
                //yyyy-MM-dd'T'HH:mm
                let dateTimeSplit = dateStr.split('T');
                let date = dateTimeSplit[0].split("-");
                return dateTimeSplit[1] + " " + date.reverse().join("/");
            },
            closeConfirmationPopUp() {
                this.confirmPopUpVisible = false;
            }
        }
    }
</script>

<style scoped>
    .modal-info {
        font-family: "Lucida Sans Unicode", "Lucida Grande", sans-serif;
        font-size: 22px;
        letter-spacing: 2px;
        word-spacing: 2px;
        font-weight: 700;
        text-decoration: none;
        font-style: normal;
        font-variant: small-caps;
        text-transform: none;
        color: rgb(16, 92, 9);

        height: 5vh;
        background: linear-gradient(rgb(236, 233, 233), white, rgb(236, 233, 233));
        border-radius: 20px 20px 0 0;
    }

    .actions-cont {
        height: 78vh;
        overflow-y: auto;
        padding: 30px 0;
    }

    #no-reses-label {
        font-family: Georgia, serif;
        font-size: 25px;
        letter-spacing: 2px;
        word-spacing: 2px;
        font-weight: 700;
        text-decoration: none;
        font-style: normal;
        font-variant: small-caps;
        text-transform: none;
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
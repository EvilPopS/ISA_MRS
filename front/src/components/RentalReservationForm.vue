<template>
    <div>
        <div class="row">
            <div id="calendar-cont" class="col" style="background: rgba(136,180,156,0.3)">
                <CalendarComponent id="calendar" 
                    :reservations="rentalReservations" 
                    :key="rentalReservations"
                />
            </div>  
        </div>
        <div class="row form-style">
            <div class="col-sm-1">
                <label>Start:</label>
            </div>
            <div class="col-sm-2">
                <input type="datetime-local" v-model="startDate">
            </div>
            <div class="col-sm-1">
                <label>End:</label>
            </div>
            <div class="col-sm-2">
                <input type="datetime-local" v-model="endDate">
            </div>
            <div class="col-sm-5">
                <button id="make-res-btn" class="btns-style" @click="openConfPopUp()">Make reservation</button>
            </div>
            <div class="col-sm-1">
                <button id="back-btn" class="btns-style" @click="$emit('close')">Back</button>
            </div>
        </div>

        <ErrorPopUp v-show="errorPopUpVisible" 
            @close = closePopUp
            :mess = errMessage
        /> 

        <SuccessPopUp v-show="successPopUpVisible"
            @close = closeSuccPopUp
            :mess = succMessage
        />

        <ConfirmationPopUp v-show="showConfPopUp"
            :title="confTitle"
            :message="confMessage"
            @modal-closed="closeConfirmationPopUp"
            @confirmed-event="makeReservation"
        />        
    </div>

</template>

<script>
    import axios from 'axios';
    import ErrorPopUp from "@/components/ErrorPopUp.vue";
    import SuccessPopUp from "@/components/SuccessPopUp.vue";
    import ConfirmationPopUp from "@/components/ConfirmationPopUp.vue";
    import CalendarComponent from "@/components/CalendarComponent.vue";

    export default {
        name: "RentalResevationForm",
        components: {
            ErrorPopUp,
            SuccessPopUp,
            ConfirmationPopUp,
            CalendarComponent
        },
        props: {
            rentalId: Number,
            rentalType: String,
            rentalPrice: String,
            rentalReservations: Array
        },
        data() {
            return {
                startDate: "",
                endDate: "",

                errMessage: "",
                succMessage: "The rental hass been successfully reserved for the selected period!",

                errorPopUpVisible: false,
                successPopUpVisible: false,
                showConfPopUp: false,

                confTitle: "Confirmation",
                confMessage: ""
            }
        },
        methods: {
            openConfPopUp() {
                try { validateForm(this); } 
                catch (errMess) { 
                    this.errMessage = errMess;
                    this.errorPopUpVisible = true;
                    return;
                }

                axios.get("api/loyalty-program/get-client-discount", {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then((response) => {
                        let discount = response.data;

                        let numOfDays = Math.ceil((new Date(this.endDate) - new Date(this.startDate))/86400000);
                        let money = (numOfDays*this.rentalPrice.split(" ")[0]);

                        let totalPrice = money - money*discount/100;
                        this.confMessage = "You are about to submit payment of " + 
                                            totalPrice + 
                                            "€ in total. Are you sure you want to continue?";
                        this.showConfPopUp = true;
                    }
                );
            },
            closeConfirmationPopUp() {
                this.showConfPopUp = false;
            },
            makeReservation() {
                this.showConfPopUp = false;
                
                let requestBody = {
                    startDate: formatDateStr(this.startDate),
                    endDate: formatDateStr(this.endDate),
                    rentalId: this.rentalId,
                    rentalType: this.rentalType
                }

                axios.post("api/client/make-reservation", requestBody, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then(() => {
                        this.successPopUpVisible = true;
                    }).catch(err => {
                        if (err.response.status === 406)
                            this.errMessage = "You have more than 2 penalties at this moment and therefore you cannot make reservations!";
                        else if (err.response.status == 409) {
                                this.errMessage = "Conflict situation. Please try again later..";
                                this.errorPopUpVisible = true;
                        }
                        else 
                            this.errMessage = "The reservation period you entered is already taken or you are trying to make a reservation for the period you previously canceled!";
                        this.errorPopUpVisible = true;
                    });
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

    function validateForm(formData) {
        if (formData.startDate === '' || formData.endDate === '') 
            throw "Please make sure you entered both start and end date for your prefered reservation period."
        
        if (new Date(formData.startDate) <= new Date() || new Date(formData.endDate) <= new Date())
            throw "You can change past or what? Select some future date!";

        if (new Date(formData.startDate) >= new Date(formData.endDate))
            throw "How comes future is before past? Check those start and ending dates...";
    }

    function formatDateStr(dateStr) {
        //yyyy-MM-dd'T'HH:mm
        let dateTimeSplit = dateStr.split('T');
        let date = dateTimeSplit[0].split("-");
        return date.reverse().join("/") + " " + dateTimeSplit[1];
    }
</script>

<style scoped>
    #calendar {
        height: 100%;
    }

    #calendar-cont {
        height: 83vh;
        padding: 20px;
    }

    .form-style div {
        height: 7vh;

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
</style>
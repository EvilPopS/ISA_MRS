<template>
    <div>
        <div class="row">
            <div id="calendar-cont" class="col" style="background: rgba(136,180,156,0.3)">
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
                <button id="make-res-btn" class="btns-style" @click="makeReservation()">Make reservation</button>
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
    </div>

</template>

<script>
    import axios from 'axios';
    import ErrorPopUp from "@/components/ErrorPopUp.vue";
    import SuccessPopUp from "@/components/SuccessPopUp.vue";

    export default {
        name: "RentalResevationForm",
        components: {
            ErrorPopUp,
            SuccessPopUp
        },
        props: {
            rentalId: Number,
            rentalType: String
        },
        data() {
            return {
                startDate: "",
                endDate: "",

                errMessage: "",
                succMessage: "The rental hass been successfully reserved for the selected period!",

                errorPopUpVisible: false,
                successPopUpVisible: false
            }
        },
        methods: {
            makeReservation() {
                try { validateForm(this); } 
                catch (errMess) { 
                    this.errMessage = errMess;
                    this.errorPopUpVisible = true;
                    return;
                }

                let requestBody = {
                    startDate: formatDateStr(this.startDate),
                    endDate: formatDateStr(this.endDate),
                    rentalId: this.rentalId,
                    rentalType: this.rentalType
                }

                axios.post("api/client/make-reservation", requestBody, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then(response => {
                        this.successPopUpVisible = true;
                    }).catch(err => {
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
    #calendar-cont {
        height: 83vh;
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
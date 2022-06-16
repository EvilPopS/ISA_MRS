<template>
    <div>
        <label for="fdx-time">Choose a start time for period:</label>
        <input type="datetime-local" id="fdx-time"
            name="res-time" max="2024-01-01T00:00" v-model="regularData.startTime">
        <label for="fdx-time">Choose a end time for period:</label>
        <input type="datetime-local" id="fdx-time"
            name="res-time" max="2024-01-01T00:00" v-model="regularData.endTime">
        <div class="vstack gap-2 col-md-5 mx-auto" id="options-btns">
            <button type="button" class="btn btn-success" @click="addRegRes()">Add</button>
            <button type="button" class="btn btn-outline-success check-label" @click="closeWindow()">Cancel</button>
        </div>
    </div>
    <ErrorPopUp v-show="errorPopUpVisible" 
            @close = closePopUp
            :mess = errMessage
        /> 
    
    <SuccessPopUp v-show="localSuccPopUpVisible"
        @close = closeSuccPopUp
        :mess = succMessage
    />
</template>

<script>
import axios from 'axios'
import ErrorPopUp from "./ErrorPopUp.vue"
import SuccessPopUp from "./SuccessPopUp.vue"


export default {
    name: "AddUnvailablePeriod",
    components: {
        SuccessPopUp, ErrorPopUp
    },
    props: {
        rental: Object
    },
    data(){
        return {
            regularData: {
                startTime: '',
                endTime: '',
                rentalId: this.rental.id,
                clientEmail: '',
                price: 0.0
            },

            todaysDate: '',
            searchRole: '',
            roleURL: '',

            localSuccPopUpVisible: false,
            errorPopUpVisible: false,
            succMessage: 'Unvailable period is successfully added!',
            errMessage: ''
        }
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
        },
        closePopUp() {
            this.errorPopUpVisible = false;
        },
        closeSuccPopUp() {
                this.localSuccPopUpVisible = false
                this.$emit('modal-closed'); 
        },
        addRegRes() {
            try { this.checkInputs(); } 
            catch(error) {        
                this.errMessage = error;
                this.errorPopUpVisible = true; 
                return;
            }

            axios.post("api/" + this.roleURL + "/add-unvailable-period", this.regularData, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then((response) => {
                        this.localSuccPopUpVisible = true;
                    })
                    .catch(err => {
                            if (err.response.status === 404){
                                this.errMessage = "Owner doesn't exist!";
                                this.errorPopUpVisible = true;
                            } 
                            else if (err.response.status === 401) {
                                this.errMessage = "You are not authorized!";
                                this.errorPopUpVisible = true;
                            }
                            else if (err.response.status === 422) {
                                this.errMessage = "Unvailable period cannot overlap with other reservations! Check all input data.";
                                this.errorPopUpVisible = true;
                            } else {
                                this.errMessage = "Uups! Something went wrong...";
                                this.errorPopUpVisible = true;
                            }
                        });
        },
        checkInputs : function(){
            if (this.regularData.startTime <= this.todaysDate)
                throw "Wrong date! Date cannot be in past!"
            if (this.regularData.startTime >= this.regularData.endTime)
                throw "Wrond date! End time must be after start time!"

        }
    },
    mounted() {
        this.todaysDate = new Date().toISOString()

        this.searchRole = window.localStorage.getItem("userRole")
        if (this.searchRole === "COTTAGE_OWNER"){
            this.roleURL = "cottage-owner"
        } else if (this.searchRole === "INSTRUCTOR"){
            this.roleURL = "fishingInstructor"
        } else if (this.searchRole === "BOAT_OWNER") {
            this.roleURL = "boat-owner"
        }
    }
}
</script>

<style scoped>

    #options-btns {
        margin: 10% 0 5% 0;
    }


</style>
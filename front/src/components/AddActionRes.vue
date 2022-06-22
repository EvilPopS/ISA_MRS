<template>
    <div>
        <label for="fdx-time">Choose a start time for action:</label>
        <input type="datetime-local" id="fdx-time"
            name="res-time" max="2024-01-01T00:00" v-model="actionData.startTime">
        <label for="fdx-time">Choose a end time for action:</label>
        <input type="datetime-local" id="fdx-time"
            name="res-time" max="2024-01-01T00:00" v-model="actionData.endTime">
        <label >Price in euros:</label>
        <input  type="number" min="0" v-model="actionData.price" title="Minimum price/day for a rental to be. (euros)">
        <div class="btn-group-checkbox row d-flex justify-content-center">
            <label class="label-desc label-action-services">Choose some special action services: </label>
            <div class="d-flex justify-content-center">
                <input type="checkbox" class="btn-check" id="btncheck1" autocomplete="off" @click="addedActionService(servicesList[0])">
                <label class="btn btn-outline-success check-label" for="btncheck1">{{servicesList[0]}}</label>
                <input type="checkbox" class="btn-check" id="btncheck2" autocomplete="off" @click="addedActionService(servicesList[1])">
                <label class="btn btn-outline-success check-label" for="btncheck2">{{servicesList[1]}}</label>
                <input type="checkbox" class="btn-check" id="btncheck3" autocomplete="off" @click="addedActionService(servicesList[2])">
                <label class="btn btn-outline-success check-label" for="btncheck3">{{servicesList[2]}}</label>
            </div>
            <div class="d-flex justify-content-center">
                <input type="checkbox" class="btn-check" id="btncheck4" autocomplete="off" @click="addedActionService(servicesList[3])">
                <label class="btn btn-outline-success check-label" for="btncheck4">{{servicesList[3]}}</label>
                <input type="checkbox" class="btn-check" id="btncheck5" autocomplete="off" @click="addedActionService(servicesList[4])">
                <label class="btn btn-outline-success check-label" for="btncheck5">{{servicesList[4]}}</label>
                <input type="checkbox" class="btn-check" id="btncheck6" autocomplete="off" @click="addedActionService(servicesList[5])">
                <label class="btn btn-outline-success check-label" for="btncheck6">{{servicesList[5]}}</label>
            </div>
        </div>     
        <div class="vstack gap-2 col-md-5 mx-auto" id="options-btns">
            <button type="button" class="btn btn-success" @click="addAction()">Add</button>
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
    name: "AddActionRes",
    components: {
        SuccessPopUp, ErrorPopUp
    },
    props: {
        rental: Object
    },
    data(){
        return {
            actionData: {
                price: 0,
                startTime: '',
                endTime: '',
                actionServices: '',
                rentalId: this.rental.id
            },

            todaysDate: '',
            searchRole: '',
            roleURL: '',
            servicesList: [],
            choosenList: [],

            localSuccPopUpVisible: false,
            errorPopUpVisible: false,
            succMessage: 'Action Reservation is successfully added!',
            errMessage: ''
        }
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
        },
        setSelected(tab) {
            this.selected = tab;
        },
        addedActionService(service){
            if (!this.choosenList.includes(service)){
                    this.choosenList.push(service)    //zbog duplikata
            } else {
                //posto postoji a opet je kliknuto na njega brisemo ga
                this.choosenList = this.choosenList.filter((item) => {
                    return service !== item       //ako vratimo true isti su
                //kad se uslov ispuni filtrira sta je stisnuto iz liste
                 })
            }
        },
        closePopUp() {
            this.errorPopUpVisible = false;
        },
        closeSuccPopUp() {
                this.localSuccPopUpVisible = false
                this.$emit('modal-closed');
        },
        addAction() {
            try { this.checkInputs(); } 
            catch(error) {        
                this.errMessage = error;
                this.errorPopUpVisible = true; 
                return;
            }

            let counter = 0
            this.actionData.actionServices = ''
            for (let s in this.choosenList){
                counter++
                this.actionData.actionServices += this.choosenList[s]
                if (counter < this.choosenList.length) this.actionData.actionServices += ','
            }
            this.choosenList = []
            counter = 0 //nesto je bilo problema pa reset za svaki slucaj
            
            axios.post("api/" + this.roleURL + "/add-action-reservation", this.actionData, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then((response) => {
                        this.localSuccPopUpVisible = true;
                    })
                    .catch(err => {
                            if (err.response.status === 404){
                                this.errMessage = "Owner with that email doesn't!";
                                this.errorPopUpVisible = true;
                            } 
                            else if (err.response.status === 401) {
                                this.errMessage = "You are not authorized!";
                                this.errorPopUpVisible = true;
                            }
                            else if (err.response.status === 422) {
                                this.errMessage = "Action reservation cannot overlap with other reservations! Check all input data.";
                                this.errorPopUpVisible = true;
                            }
                            else if (err.response.status == 409){
                                this.errMessage = "Conflict situation. Please try again later..";
                                this.errorPopUpVisible = true;
                            } else {
                                this.errMessage = "Uups! Something went wrong...";
                                this.errorPopUpVisible = true;
                            }
                        });
        },
        checkInputs : function(){
            if (this.actionData.startTime <= this.todaysDate)
                throw "Wrong date! Date cannot be in past!"
            if (this.actionData.startTime >= this.actionData.endTime)
                throw "Wrond date! End time must be after start time!"
            if (this.actionData.price <= 0)
                throw "Wrong price! Price must be greater than 0!"
            if (this.choosenList.length == 0)
                throw "You must add at least one special action!"

        }
    },
    mounted() {
        this.todaysDate = new Date().toISOString()

        this.choosenList = []
        this.searchRole = window.localStorage.getItem("userRole")
        if (this.searchRole === "COTTAGE_OWNER"){
            this.roleURL = "cottage-owner"
            this.servicesList = ["MASSAGE", "BREAKFAST", "MINI-BAR", "SPA", "TABLE TENNIS", "KARAOKE"]
        } else if (this.searchRole === "INSTRUCTOR"){
            this.roleURL = "fishingInstructor"
            //staviti nesto za pecanje od opreme
            this.servicesList = ["MASSAGE", "BREAKFAST", "MINI-BAR", "SPA", "TABLE TENNIS", "KARAOKE"]
        } else if (this.searchRole === "BOAT_OWNER") {
            this.roleURL = "boat-owner"
            this.servicesList = ["RADAR", "AUTOPILOT", "VDR", "GPS", "LURES", "FLY REELS"]
        }
    }
}
</script>

<style scoped>

    #options-btns {
        margin: 10% 0 5% 0;
    }

    div input.btn-check{
        visibility: hidden;
    }

    div label.check-label {
        font-size: 10px !important;
    }

    label.label-action-services {
        margin-top: 6%;
    }

</style>
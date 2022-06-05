<template>
    <div class="container" style="margin-top: 5%">
        <div class="row">
            <div class="col-12 col-md-5 col-lg-4" v-for="adventure in adventures" :key="adventure.name">
                <div class="card" style="width: 18rem; margin-top: 5%" id="card-body-id">
                    <img :src="setPicture(adventure)" id="adventure-img" class="card-img-top" alt="This is adventure picture">
                    <div class="card-body">
                        <h5 class="card-title" id="heading-adventure">{{adventure.name}}</h5>
                        <p class="card-text"><b>Location:</b> {{adventure.city}}, {{adventure.street}}</p>
                        <p class="card-text"><b>Description:</b>{{adventure.description}}</p>
                        <p class="card-text"><b>Rate:</b> {{adventure.rating === 0 ? 'Not yet rated.' : adventure.rating}}</p>
                        <span>
                            <button class="btn btn-success card-btns" @click="showAdventureDetail(adventure)">Details</button>
                            <button class="btn btn-success card-btns" @click="addNewReservation(adventure)">Calendar</button>
                            <button class="btn btn-danger card-btns"  @click="showConfirmDeletionDialog(adventure)" >Delete</button>
                        </span>    
                    </div>
                </div>
            </div>
        </div>
        <div v-if="this.searchMode === false">
            <button class="btn btn-success" id="btn-add" @click="showAddAdventure()">Add adventure</button>
        </div>
    </div>
    <div v-if="showDetails">
        <AdventureDetail
        :adventure = "sendadventure"
        :showDetails = "showDetails"
        @modal-closed = "showDetails = false"
        />
    </div>
    <div v-else-if="showAddNewАdventure">
        <AddAdventure
        :showAddNewAdventure = "showAddNewАdventure"
        :succPopUpVisible = "succPopUpVisible"
        @modal-closed = "showAddNewАdventure = false"
        @succ-popup-close = "succPopUpClose"
        />
    </div>    
    <div v-else-if="confirmationPopUpVisible">
        <ConfirmationPopUp
        :title="deletionTitle"
        :message="deletionMessage"
        @modal-closed = "confirmationPopUpVisible = false"
        @confirmed-event = "deleteAdventure"
        />
    </div>
    <div v-else-if="showAddNewRes">
        <NewReservationsComponent
            @modal-closed = "showAddNewRes = false"
            :calendarForRental="calendarForCottage"
        />
    </div>
    <ErrorPopUp v-show="errorPopUpVisible" 
    @close = "errorPopUpVisible = false"
    :mess = "errorMessage"
    /> 
    <SuccessPopUp v-show="succPopUpVisible"
        @close = "succPopUpVisible = false"
        :mess = succMessage
    />

    
</template>

<script>
 import axios from 'axios';
 import AdventureDetail from '../components/AdventureDetail.vue'
 import AddAdventure from '../components/AddAdventure.vue'
 import ConfirmationPopUp from '../components/ConfirmationPopUp.vue'
 import ErrorPopUp from '../components/ErrorPopUp.vue'
 import SuccessPopUp from '../components/SuccessPopUp.vue'
 import NewReservationsComponent from '../components/NewReservationsComponent.vue'


export default {
   name: "AdventuresView",
   components: {
     AdventureDetail, AddAdventure,
     ConfirmationPopUp, ErrorPopUp, 
     SuccessPopUp, NewReservationsComponent
   },
   data (){
       return {
           adventures: [],
           sendadventure: {},
           adventureToDelete : Object,


           showDetails: false,
           showAddNewАdventure: false,
           showDeleteadventure: false,
           succPopUpVisible: false,
           confirmationPopUpVisible : false,
           errorPopUpVisible : false,
           showAddNewRes : false,

           
           errorMessage : '',
           succMessage : '',
           deletionTitle : '',
           deletionMessage : '',

           calendarForAdventure : {},

       }
   }, 
   
   props : {
       searchMode : Boolean,
       searchedAdventures : Array 
   },
   
   methods: {
        setPicture(adventure) {
                try{
                    return require('../assets/' + adventure.photos[0]);
                } catch(e) {}
        },
        showAdventureDetail(adventure) {
            this.showDetails = true
            this.sendadventure = adventure
        },
        showAddAdventure() {
            this.showAddNewАdventure = true
        },
        showConfirmDeletionDialog(adventure) {
            this.adventureToDelete = adventure;
            this.deletionTitle = 'Are you sure?';
            this.deletionMessage = 'Adventure: ' + adventure.name + 'is going to be deleted.';
            
            this.confirmationPopUpVisible = true;
        },
        succPopUpClose() {
            this.succPopUpVisible = false;
        },
        addNewReservation(cottage) {
            this.showAddNewRes = true
            this.calendarForCottage = cottage
        },
        deleteAdventure(){
            this.confirmationPopUpVisible = false;


            axios.delete('api/fishingInstructor/delete-adventure/' + this.adventureToDelete.id, {headers: {'authorization': window.localStorage.getItem("token") }}).then((response) => {
                this.succPopUpVisible = true;
                this.adventures = this.adventures.filter(item => item != this.adventureToDelete);
                this.succMessage = "Adventure is successfully deleted!";

            }).catch((e) => {
                this.errorMessage = "Adventure cannot be deleted due to upcoming reservations";
                this.errorPopUpVisible = true;
                });
            


        }
   },
   created(){
       if (!this.searchMode) {
            axios.get('api/fishingInstructor/adventures', {headers: {'authorization': window.localStorage.getItem("token") }}).then((response) => {
            this.adventures = response.data
        }).catch((error) => {
            console.log('Error happened: ' + error.name);
    })
       } else {
           this.adventures = this.searchedAdventures
       }


   }
}


</script>

<style scoped>
    
    #adventure-img {
        margin-top: 10px !important;
        height: 150px;
        width: auto;
        display: block;
        margin: 0 auto;
        border-radius: 10%;
    }

    #adventure-img:hover{
        width: 230px;
        height: 170px;
        border-radius: 20px;
        background: linear-gradient(rgb(255, 253, 253), rgb(241, 239, 239));
    }

    b{
        color: rgba(51, 92, 80, 0.8);
    }

    span button {
        margin: 10px 10px;
    }

    div h5#heading-adventure {
        color: rgba(51, 92, 80, 0.8);
        font-weight: bold;
    }

    div div#card-body-id {
        background-color: rgba(214, 218, 216, 0.7);
    }

    #btn-add{
        margin: 30px;
        float: left;
    }

    #btn-add:hover{
        border-radius: 10px;
        background: rgb(25, 117, 7);
    }
    span button.card-btns {
        margin: 10px 10px;
    }
</style>
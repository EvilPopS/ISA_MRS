<template>
    <div v-if="showSearch">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <div class="container" style="margin-top: 5%">
            <div v-if="cottages.length > 0">
                <span id="fa-search-id">
                    <input type="text" placeholder="Search..." id="search-input" v-model="searched"/>
                    <i class="fa fa-search" id="search-icon-color" aria-hidden="true"></i>
                </span>
                <span>
                    <button class="btn btn-success add-btn" id="btn-add" @click="showAddCottageModal()">Add cottage</button>
                </span>
                <div class="row">
                    <div class="col-12 col-md-5 col-lg-4" v-for="cottage in filtered" :key="cottage.name">
                        <div class="card" style="width: 18rem; margin-top: 5%" id="card-body-id">
                            <img :src="setPicture(cottage)" id="cottage-img" class="card-img-top" alt="This is cottage picture">
                            <div class="card-body">
                                <h5 class="card-title" id="heading-cottage">{{cottage.name}}</h5>
                                <p class="card-text"><b>Location:</b> {{cottage.city}}, {{cottage.street}}</p>
                                <p class="card-text"><b>Description:</b>{{cottage.description}}</p>
                                <p class="card-text"><b>Rate:</b> {{cottage.averageRating}}★</p>
                                <span>
                                    <button class="btn btn-success card-btns" @click="addNewReservation(cottage)">Calendar</button>
                                    <button class="btn btn-success card-btns" @click="showDetailCottageModal(cottage)">Details</button>
                                    <button class="btn btn-danger card-btns" @click="showConfirmDeletionDialog(cottage)">Delete</button>
                                </span>    
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div v-else>
                <h3>There is nothing to show here..</h3>
            </div>
        </div>
        <div v-if="showDetails">
            <DetailCottageModal
            :cottage = "sendCottage"
            :showDetails = "showDetails"
            @modal-closed = "showDetails = false"
            @succ-popup-close = "succPopUpClose"
            />
        </div>
        <div v-else-if="showAddNewCottage">
            <AddCottageModal
            :showAddNewCottage = "showAddNewCottage"
            :succPopUpVisible = "succPopUpVisible"
            @modal-closed = "showAddNewCottage = false"
            @succ-popup-close = "succPopUpClose"
            />
        </div>
        <div v-else-if="confirmationPopUpVisible">
            <ConfirmationPopUp
            :title="deletionTitle"
            :message="deletionMessage"
            @modal-closed = "confirmationPopUpVisible = false"
            @confirmed-event = "confirmDeletion"
            />
        </div>
        <div v-else-if="showAddNewRes">
            <NewReservationsComponent
                @modal-closed = "showAddNewRes = false"
                :calendarForCottage="calendarForCottage"
            />
        </div>
        <ErrorPopUp v-show="errorPoup" 
            @close = "errorPoup = false"
            :mess = "errMsg"
            /> 
        <SuccessPopUp v-show="succPoupUp"
            @close = "succPoupUp = false"
            :mess = succMessage
        />
    </div>
    <div v-else>
        <h3>You don't have permission to visit this page!</h3>
    </div>
</template>

<script>
 import axios from 'axios';
 import DetailCottageModal from '../components/DetailCottageModal.vue'
 import AddCottageModal from '../components/AddCottageModal.vue'
 import ConfirmationPopUp from '../components/ConfirmationPopUp.vue'
 import ErrorPopUp from '../components/ErrorPopUp.vue'
 import SuccessPopUp from '../components/SuccessPopUp.vue'
 import NewReservationsComponent from '../components/NewReservationsComponent.vue'

export default {
   name: "AllCottagesView",
   components: {
       DetailCottageModal, AddCottageModal, ConfirmationPopUp, ErrorPopUp, SuccessPopUp, NewReservationsComponent
   },
   data (){
       return {
           cottages: [],
           sendCottage: {},
           cottageToDelete: {},
           searched: '',
           errorPoup: false,
           errMsg: "Cottage cannot be deleted due to upcoming reservations",
           succPoupUp: false,
           succMessage: '',

           showDetails: false,
           showAddNewCottage: false,
           showDeleteCottage: false,
           confirmationPopUpVisible: false,

           deletionMessage: "Are you sure about deleting this cottage?",
           deletionTitle: "Cottage deleting",

           showSearch: window.localStorage.getItem("userRole") === "COTTAGE_OWNER",
           
           showAddNewRes: false,
           calendarForCottage: {}
       }
   }, 
   methods: {
        setPicture(cottage) {
                try{
                    return require('../assets/' + cottage.photos[0]);
                } catch(e) {}
        },
        showDetailCottageModal(cottage) {
            this.sendCottage = cottage
            this.showDetails = true
        },
        showAddCottageModal() {
            this.showAddNewCottage = true
        },
        succPopUpClose() {
            this.succPopUpVisible = false;
        },
        showConfirmDeletionDialog(cottage) {
            this.cottageToDelete = cottage;
            this.confirmationPopUpVisible = true;
        },
        closeSuccPopUp() {
            this.succPoupUp = false 
        },
        addNewReservation(cottage) {
            this.showAddNewRes = true
            this.calendarForCottage = cottage
        },
        confirmDeletion(){
            this.confirmationPopUpVisible = false
            axios.delete('api/cottage-owner/delete-cottage/' + this.cottageToDelete.id, {headers: {'authorization': window.localStorage.getItem("token") }})
            .then((response) => {
                this.succMessage = "Cottage is successfully deleted!"
                this.succPoupUp = true

                this.cottages = this.cottages.filter((item) => {
                    return this.cottageToDelete !== item       //ako vratimo true isti su
                //kad se uslov ispuni filtrira sta je stisnuto iz liste
                })
                this.cottageToDelete = {}   //reset vreednosti
            }).catch((error) => {
                this.errMsg = "Cottage cannot be deleted due to upcoming reservations"
                this.errorPoup = true
            })
            
        }
   },
   mounted(){
       axios.get('api/cottage-owner/all-cottages', {headers: {'authorization': window.localStorage.getItem("token") }})
       .then((response) => {
           this.cottages = response.data
        }).catch((error) => {
            this.errMsg = "Error happened: " + error.name
            this.errorPoup = true
        })

   },
   computed: {
        filtered: function(){
            return this.cottages.filter((res) => {
                return ((res.name.toLowerCase()).match(this.searched.toLowerCase()) || (res.description.toLowerCase()).match(this.searched.toLowerCase())
                || (res.averageRating.toString()).match(this.searched) || (res.city.toLowerCase()).match(this.searched.toLowerCase()))
            });
        }
    }

}
</script>

<style scoped>
    
    #cottage-img {
        margin-top: 10px !important;
        height: 150px;
        width: auto;
        display: block;
        margin: 0 auto;
        border-radius: 10%;
    }

    #cottage-img:hover{
        width: 230px;
        height: 170px;
        border-radius: 20px;
        background: linear-gradient(rgb(255, 253, 253), rgb(241, 239, 239));
    }

    b{
        color: rgba(51, 92, 80, 0.8);
    }

    span button.card-btns {
        margin: 10px 10px;
    }

    div h5#heading-cottage {
        color: rgba(51, 92, 80, 0.8);
        font-weight: bold;
    }

    div div#card-body-id {
        background-color: rgba(214, 218, 216, 0.7);
        margin-bottom: 5%;
    }

    span#fa-search-id{
        display:inline;
        margin-left: auto; 
        margin-right: 68%;
    }

    #search-input {
        margin-top: 4%;
        width: 30%;
        display: inline;
        margin-bottom: 2%;
        border: 0;
        border-bottom: 2px solid rgba(51, 92, 80, 0.8);
    }

    #search-input:focus{
        outline: none;
    }

    i#search-icon-color{
        color: rgba(51, 92, 80, 0.8);
    }

    button.add-btn{
        margin-left: auto; 
        margin-right: 90%;
    }

</style>
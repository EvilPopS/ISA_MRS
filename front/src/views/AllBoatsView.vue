<template>
    <div v-if="showSearch">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <div class="container" style="margin-top: 5%">
            <div v-if="boats.length > 0">
                <span id="fa-search-id">
                    <input type="text" placeholder="Search..." id="search-input" v-model="searched"/>
                    <i class="fa fa-search" id="search-icon-color" aria-hidden="true"></i>
                    <div class="d-flex justify-content-center" id="sort-bar">
                        <button class=" btn btn-success" v-bind:class="{ btn_clicked: nameSortBtnClicked }" @click="sortByName();">&#8645; Rental Name</button>
                        <button class=" btn btn-success" v-bind:class="{ btn_clicked: rateSortBtnClicked }" @click="sortByRate();">&#8645; Rate</button>
                        <button class=" btn btn-success" v-bind:class="{ btn_clicked: priceSortBtnClicked }" @click="sortByPrice();">&#8645; Price</button>
                    </div>
                </span>
                <span>
                    <button class="btn btn-success add-btn" id="btn-add" @click="showAddBoatModal()">Add boat</button>
                </span>
                <div class="row">
                    <div class="col-12 col-md-5 col-lg-4" v-for="boat in filtered" :key="boat.name">
                        <div class="card" style="width: 18rem; margin-top: 5%" id="card-body-id">
                            <img :src="setPicture(boat)" id="cottage-img" class="card-img-top" alt="This is boat picture" @click="showDetailModal(boat)">
                            <div class="card-body">
                                <h5 class="card-title" id="heading-cottage">{{boat.name}}</h5>
                                <p class="card-text"><b>Location:</b> {{boat.city}}, {{boat.street}}</p>
                                <p class="card-text"><b>Description:</b>{{boat.description.length < 27 ? boat.description + "&#8205;&#8205;&#8205;&#8205;&#8205;." : boat.description}}</p>
                                <p class="card-text"><b>Engine power:</b>{{boat.enginePower}}</p>
                                <p class="card-text"><b>Max speed:</b>{{boat.maxSpeed}}</p>
                                <p class="card-text"><b>Price:</b>{{boat.price}} &euro;</p>
                                <p class="card-text"><b>Rate:</b> {{boat.averageRating}}★</p>
                                <span>
                                    <button class="btn btn-success card-btns" @click="showEditBoatModal(boat)">Change</button>
                                    <button class="btn btn-danger card-btns" @click="showConfirmDeletionDialog(boat)">Delete</button>
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
        <div v-if="showEdit">
            <EditCottageModal
            :cottage = "sendCottage"
            @modal-closed = "showEdit = false"
            @succ-popup-close = "succPopUpClose"
            />
        </div>
        <div v-else-if="showDetails">
            <DetailCottageModal
            :cottage = "sendCottage"
            @modal-closed = "showDetails = false"
            @succ-popup-close = "succPopUpClose"
            />
        </div>
        <div v-else-if="showAddNewBoat">
            <AddCottageModal
            :showAddNewBoat = "showAddNewBoat"
            :succPopUpVisible = "succPopUpVisible"
            @modal-closed = "showAddNewBoat = false"
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
                :choosenCottage="calendarForCottage"
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
 import EditCottageModal from '../components/EditCottageModal.vue'
 import AddCottageModal from '../components/AddCottageModal.vue'
 import ConfirmationPopUp from '../components/ConfirmationPopUp.vue'
 import ErrorPopUp from '../components/ErrorPopUp.vue'
 import SuccessPopUp from '../components/SuccessPopUp.vue'
 import DetailCottageModal from '../components/DetailCottageModal.vue'

export default {
   name: "AllBoatsView",
   components: {
       EditCottageModal, AddCottageModal, ConfirmationPopUp, ErrorPopUp, SuccessPopUp, DetailCottageModal
   },
   data (){
       return {
            boats: [],
            sendCottage: {},
            boatToDelete: {},
            searched: '',
            errorPoup: false,
            errMsg: "Boat cannot be deleted due to upcoming reservations",
            succPoupUp: false,
            succMessage: '',
            
            showDetails: false,
            showEdit: false,
            showAddNewBoat: false,
            showDeleteCottage: false,
            confirmationPopUpVisible: false,

            deletionMessage: "Are you sure about deleting this boat?",
            deletionTitle: "Boat deleting",

            showSearch: window.localStorage.getItem("userRole") === "BOAT_OWNER",
            
            calendarForCottage: {},

            nameSortBtnClicked: false,
            rateSortBtnClicked: false,
            priceSortBtnClicked: false
       }
   }, 
   methods: {
        setPicture(boat) {
                try{
                    return require('../assets/' + boat.photos[0]);
                } catch(e) {}
        },
        showEditBoatModal(boat) {
            this.sendCottage = boat
            this.showEdit = true
        },
        showDetailModal(boat) {
            this.sendCottage = boat
            this.showDetails = true
        },
        showAddBoatModal() {
            this.showAddNewBoat = true
        },
        succPopUpClose() {
            this.succPopUpVisible = false;
        },
        showConfirmDeletionDialog(boat) {
            this.boatToDelete = boat;
            this.confirmationPopUpVisible = true;
        },
        closeSuccPopUp() {
            this.succPoupUp = false 
        },
        confirmDeletion(){
            this.confirmationPopUpVisible = false
            axios.delete('api/boat-owner/delete-boat/' + this.boatToDelete.id, {headers: {'authorization': window.localStorage.getItem("token") }})
            .then((response) => {
                this.succMessage = "Boat is successfully deleted!"
                this.succPoupUp = true

                this.boats = this.boats.filter((item) => {
                    return this.boatToDelete !== item       //ako vratimo true isti su
                //kad se uslov ispuni filtrira sta je stisnuto iz liste
                })
                this.boatToDelete = {}   //reset vreednosti
            }).catch(err => {
                if (err.response.status === 404){
                    this.errMsg = "Client or owner with given email address is not found!";
                    this.errorPoup = true;
                } 
                else if (err.response.status === 401) {
                    this.errMsg = "You are not authorized!";
                    this.errorPoup = true;
                }
                else if (err.response.status === 422) {
                    this.errMsg = "Boat cannot be deleted due to upcoming reservations";
                    this.errorPoup = true;
                } else {
                    this.errMsg = "Boat cannot be deleted due to upcoming reservations";
                    this.errorPoup = true;
                }
            });
            
        },
        sortByName() {
            if (this.nameSortBtnClicked)
                this.boats.reverse();
            else {
                this.uncheckSortButtons();
                this.nameSortBtnClicked = true;

                this.boats.sort(function(left, right) { 
                    let lName = left.name.toUpperCase();
                    let rName = right.name.toUpperCase();
                    if (lName < rName) 
                        return -1;
                    else if (lName > rName)
                        return 1;
                    
                    return 0;
                });
            }
        },
        sortByPrice() {
            if (this.priceSortBtnClicked)
                this.boats.reverse();
            else {
                this.uncheckSortButtons();
                this.priceSortBtnClicked = true;

                this.boats.sort(function(left, right) { 
                    let lPrice = left.price;
                    let rPrice = right.price;
                    if (lPrice < rPrice)
                        return -1;
                    else if (lPrice > rPrice)
                        return 1;
                    return 0;
                });
            }
        },
        sortByRate() {
            if (this.rateSortBtnClicked)
                this.boats.reverse();
            else {
                this.uncheckSortButtons();
                this.rateSortBtnClicked = true;

                this.boats.sort(function(left, right) { 
                    let lRate = left.averageRating;
                    let rRate = right.averageRating;
                    if (lRate < rRate)
                        return -1;
                    else if (lRate > rRate)
                        return 1;
                    return 0;
                });
            }
        },
        uncheckSortButtons() {
            this.nameSortBtnClicked = false;
            this.rateSortBtnClicked = false;
            this.priceSortBtnClicked = false;
        }
   },
   mounted(){
       axios.get('api/boat-owner/all-boats', {headers: {'authorization': window.localStorage.getItem("token") }})
       .then((response) => {
           this.boats = response.data
        }).catch(err => {
            if (err.response.status === 404){
                this.errMsg = "Client or owner with given email address is not found!";
                this.errorPoup = true;
            } 
            else if (err.response.status === 401) {
                this.errMsg = "You are not authorized!";
                this.errorPoup = true;
            }
            else if (err.response.status === 422) {
                this.errMsg = "Input data is wrong!";
                this.errorPoup = true;
            } else {
                this.errMsg = "Uups! Something went wrong...";
                this.errorPoup = true;
            }
        });

   },
   computed: {
        filtered: function(){
            return this.boats.filter((res) => {
                return ((res.name.toLowerCase()).match(this.searched.toLowerCase()) || (res.description.toLowerCase()).match(this.searched.toLowerCase())
                || (res.averageRating.toString()).match(this.searched) || (res.price.toString()).match(this.searched) || (res.city.toLowerCase()).match(this.searched.toLowerCase()) 
                || ((res.enginePower.toLowerCase()).match(this.searched.toLowerCase())) || ((res.maxSpeed.toLowerCase()).match(this.searched.toLowerCase())))
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
        max-width: 250px;
        display: block;
        margin: 0 auto;
        border-radius: 10%;
    }

    #cottage-img:hover{
        width: 270px;
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

    #sort-bar button {
        margin: 0 5px;
        border-radius: 10px;
        font-weight: bold;
        padding: 4px 15px;
        border: 1px rgb(71, 69, 69) solid;
        color: white;
    }

    #sort-bar button:hover {
        background-color: rgb(6, 94, 40);
    }

</style>
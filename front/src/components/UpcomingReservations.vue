<template>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <div class="container" style="margin-top: 5%">
        <div v-if="data.length > 0">
            <span id="fa-search-id">
                <input type="text" placeholder="Search..." id="search-input" v-model="searchedReservation"/>
                <i class="fa fa-search" id="search-icon-color" aria-hidden="true"></i>
                <div class="d-flex justify-content-center" id="sort-bar">
                        <button class=" btn btn-success" v-bind:class="{ btn_clicked: rentalNameSortBtnClicked }" @click="sortByRentalName();">&#8645; Rental Name</button>
                        <button class=" btn btn-success" v-bind:class="{ btn_clicked: clientNameSortBtnClicked }" @click="sortByClientName();">&#8645; Client Name</button>
                        <button class=" btn btn-success" v-bind:class="{ btn_clicked: startDateSortBtnClicked }" @click="sortByStartDate();">&#8645; Start date</button>
                        <button class=" btn btn-success" v-bind:class="{ btn_clicked: endDateSortBtnClicked }" @click="sortByEndDate();">&#8645; End date</button>
                </div>
            </span>
            <div class="row">
                <div class="col-12 col-md-5 col-lg-4" v-for="reservation in filteredReservations" :key="reservation.reservationId">
                    <div class="card" style="width: 18rem; margin-top: 5%" id="card-body-id">
                        <img :src="setPicture(reservation.clientProfilePhoto)" id="cottage-img" class="card-img-top" alt="This is a reservation picture." @click="showClient(reservation.clientEmail)">
                        <div class="card-body">
                            <h5 class="card-title" id="heading-cottage">Reservation #{{reservation.reservationId}}</h5>
                            <p class="card-text"><b>Rental:</b> {{reservation.rentalName}}</p>
                            <p class="card-text"><b>Client:</b> {{reservation.clientFullName}}</p>
                            <p class="card-text"><b>Start:</b> {{reservation.startTime.split('T')[0]}} <b>at</b> {{(reservation.startTime.split('T')[1]).split(':')[0]}}:{{(reservation.startTime.split('T')[1]).split(':')[1]}}</p>
                            <p class="card-text"><b>End:</b> {{reservation.endTime.split('T')[0]}} <b>at</b> {{(reservation.endTime.split('T')[1]).split(':')[0]}}:{{(reservation.endTime.split('T')[1]).split(':')[1]}}</p>
                            <p class="card-text"><b>Action:</b> {{reservation.Action ? 'Yes' : 'No'}}</p>   
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div v-else>
            <h3>There is nothing to show here..</h3>
        </div>
    </div>
    <div v-if="basicClientProfileShow">
        <BasicClientProfile
            :clientEmail = "selectedClient"
            @modal-closed = "basicClientProfileShow = false"
        />
    </div>

</template>

<script>
import BasicClientProfile from '../components/BasicClientProfile'

export default {
    name: "UpcomingReservations",
    components: {
        BasicClientProfile
    },
    props: {
        data: Array
    },
    data(){
        return {
            searchedReservation: '',
            basicClientProfileShow: false,
            selectedClient: '',

            rentalNameSortBtnClicked: false,
            clientNameSortBtnClicked: false,
            startDateSortBtnClicked: false,
            endDateSortBtnClicked: false
        }
    },
    methods: {
        setPicture(picture) {
                try{
                    return require('../assets/' + picture);
                } catch(e) {console.log(e)}
        },
        showClient(email) {
            this.basicClientProfileShow = true
            this.selectedClient = email
        },
        sortByRentalName() {
            if (this.rentalNameSortBtnClicked)
                this.data.reverse();
            else {
                this.uncheckSortButtons();
                this.rentalNameSortBtnClicked = true;

                this.data.sort(function(left, right) { 
                    let lName = left.rentalName.toUpperCase();
                    let rName = right.rentalName.toUpperCase();
                    if (lName < rName) 
                        return -1;
                    else if (lName > rName)
                        return 1;
                    
                    return 0;
                });
            }
        },
        sortByClientName() {
            if (this.clientNameSortBtnClicked)
                this.data.reverse();
            else {
                this.uncheckSortButtons();
                this.clientNameSortBtnClicked = true;

                this.data.sort(function(left, right) { 
                    let lName = left.clientFullName.toUpperCase();
                    let rName = right.clientFullName.toUpperCase();
                    if (lName < rName) 
                        return -1;
                    else if (lName > rName)
                        return 1;
                    
                    return 0;
                });
            }
        },
        sortByStartDate() {
            if (this.startDateSortBtnClicked)
                this.data.reverse();
            else {
                this.uncheckSortButtons();
                this.startDateSortBtnClicked = true;

                this.data.sort(function(left, right) {
                    let lDate = new Date(left.startTime).toISOString();
                    let rDate = new Date(right.startTime).toISOString();
                    if (lDate < rDate) 
                        return -1;
                    else if (lDate > rDate)
                        return 1;

                    return 0;
                });
            }
        },
        sortByEndDate() {
            if (this.endDateSortBtnClicked)
                this.data.reverse();
            else {
                this.uncheckSortButtons();
                this.endDateSortBtnClicked = true;

                this.data.sort(function(left, right) { 
                    let lDate = new Date(left.endTime).toISOString();
                    let rDate = new Date(right.endTime).toISOString();
                    if (lDate < rDate) 
                        return -1;
                    else if (lDate > rDate)
                        return 1;

                    return 0;
                });
            }
        },
        uncheckSortButtons() {
            this.rentalNameSortBtnClicked = false
            this.clientNameSortBtnClicked = false
            this.startDateSortBtnClicked = false
            this.endDateSortBtnClicked = false
        }
    },
    computed: {
        filteredReservations: function(){
            return this.data.filter((res) => {
                return ((res.clientFullName.toLowerCase()).match(this.searchedReservation.toLowerCase()) || (res.rentalName.toLowerCase()).match(this.searchedReservation.toLowerCase())
                || res.startTime.match(this.searchedReservation) || res.endTime.match(this.searchedReservation) || (res.reservationId.toString()).match(this.searchedReservation))
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

    span button {
        margin: 10px 10px;
    }

    div h5#heading-cottage {
        color: rgba(51, 92, 80, 0.8);
        font-weight: bold;
    }

    div div#card-body-id {
        background-color: rgba(214, 218, 216, 0.7);
    }

    span#fa-search-id{
        display:inline;
        margin-left: auto; 
        margin-right: 68%;
    }

    #search-input {
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

    button.btn-success{
        margin-left: auto; 
        margin-right: 85%;
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
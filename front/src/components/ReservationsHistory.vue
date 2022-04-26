<template>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <div class="container" style="margin-top: 5%">
        <div v-if="data.length > 0">
            <span id="fa-search-id">
                <input type="text" placeholder="Search..." id="search-input" v-model="searchedReservation"/>
                <i class="fa fa-search" id="search-icon-color" aria-hidden="true"></i>
            </span>
            <div class="row">
                <div class="col-12 col-md-5 col-lg-4" v-for="reservation in filteredReservations" :key="reservation.reservationId">
                    <div class="card" style="width: 18rem; margin-top: 5%" id="card-body-id">
                        <img :src="setPicture(reservation.clientProfilePhoto)" id="cottage-img" class="card-img-top" alt="This is a reservation picture.">
                        <div class="card-body">
                            <h5 class="card-title" id="heading-cottage">Reservation #{{reservation.reservationId}}</h5>
                            <p class="card-text"><b>Rental:</b> {{reservation.rentalName}}</p>
                            <p class="card-text"><b>Client:</b> {{reservation.clientFullName}}</p>
                            <p class="card-text"><b>Start date:</b> {{reservation.startTime.split('T')[0]}}</p>
                            <p class="card-text"><b>End date:</b> {{reservation.endTime.split('T')[0]}}</p>
                            <p class="card-text"><b>Action:</b> {{reservation.Action ? 'Yes' : 'No'}}</p>
                            <span>
                                <button class="btn btn-success" @click="">Make report</button>
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
</template>

<script>
export default {
    name: "ReservationsHistory",
    components: {

    },
    props: {
        data: Array
    },
    data(){
        return {
            searchedReservation: ''
        }
    },
    methods: {
        setPicture(picture) {
                try{
                    return require('../assets/' + picture);
                } catch(e) {console.log(e)}
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

</style>
<template>
    <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow()" class="close">X</button>
            <div class="container">
                <h1>{{cottage.name}}</h1>
                <div class="row">
                    <div class="col-6">
                        <div class="start-data">
                            <h4>Basic data</h4>
                            <p class="d-flex justify-content-left"><b>Cottage name: </b> {{cottage.name}}</p>
                            <p class="d-flex justify-content-left"><b>Description: </b> {{cottage.description}}</p>
                            <p class="d-flex justify-content-left"><b>Rules: </b> {{cottage.rules}}</p>
                        </div>
                        <div>
                            <div class="start-data">
                                <h4>Additional services:</h4>
                                <div v-for="service in additServ" :key="service" class="pill">
                                    <span >{{service}}</span>
                                </div>
                             </div>
                        </div>
                        <div id="photo-gallery">
                            <carousel :items-to-show="1" autoplay="5000" wrapAround="true">
                                <slide v-for="photo in cottage.photos" :key="photo">
                                    <img :src="setPicture(photo)">
                                </slide>
                                <template #addons>
                                    <navigation />
                                    <pagination />
                                </template>
                            </carousel>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="start-data">
                            <h4>Address</h4>
                            <p class="d-flex justify-content-left"><b>Country: </b> {{cottage.country}}</p>
                            <p class="d-flex justify-content-left"><b>City: </b> {{cottage.city}}</p>
                            <p class="d-flex justify-content-left"><b>Street: </b> {{cottage.street}}</p>
                        </div>
                        <div>
                            <div class="badge bg-success text-wrap rounded-pill status" style="width: 10rem;">
                                Price: {{cottage.price}}€
                            </div>
                            <div class="badge bg-success text-wrap rounded-pill status" style="width: 10rem;">
                                No rooms: {{cottage.noRooms}}
                            </div>
                        </div>
                        <div>
                            <div class="badge bg-success text-wrap rounded-pill status" style="width: 10rem;">
                                Rate: {{cottage.averageRating}}★
                            </div>
                            <div class="badge bg-success text-wrap rounded-pill status" style="width: 10rem;">
                                No ratings: {{cottage.noRatings}}
                            </div>
                        </div>
                        <div id="map-container">
                            <MapContainer
                                :coordinates = "[cottage.lon, cottage.lat]"
                                :map-height = "200"
                                :mapEditable="false"
                                @changed-location = "changedLocationFunc"
                            >
                            </MapContainer>
                        </div>
                    </div>
                </div>
                <div class="vstack gap-2 col-md-5 mx-auto" id="options-btns">
                    <button type="button" class="btn btn-success" @click="addNewReservation()">Calendar <img id="btn-calendar" src="../assets/icons8-calendar.png"></button>
                    <button type="button" class="btn btn-success" @click="addNewReservation()">Reviews <img id="btn-calendar" src="../assets/icons8-reviews-64.png"></button>
                    <button type="button" class="btn btn-success" @click="closeWindow">Cancel</button>
                </div>
            </div>
            <div v-if="showAddNewRes">
            <NewReservationsComponent
                @modal-closed = "showAddNewRes = false"
                :choosenRental="cottage"
            />
            </div>
        </div>
    </div>
</template>

<script>
import 'vue3-carousel/dist/carousel.css';
import { Carousel, Slide, Pagination, Navigation } from 'vue3-carousel';
import MapContainer from "./MapContainer.vue"
import NewReservationsComponent from '../components/NewReservationsComponent.vue'

export default {
    name: "DetailCottageModal",
    components: {
        Carousel, Slide, Pagination, Navigation, MapContainer, NewReservationsComponent
    },
    props: {
        cottage: Object
    },
    data(){
        return {
            additServ: [],
            showAddNewRes: false,

        }
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
        },
        setPicture(photo) {
                try{
                    return require('../assets/' + photo);
                } catch(e) {}
        },
        changedLocationFunc(){
            //do nothing
        },
        addNewReservation() {
            this.showAddNewRes = true
        }
    },
    mounted() {
        try {
            this.additServ = this.cottage.additionalServices.split(',')
        } catch (err){
            this.additServ = this.cottage.additionalServices //ako ne uspe split znaci da ima samo jedna
        }
    }
}
</script>

<style scoped>
    b{
        color: black;
    }

    #close_btn{
        height: 30px;
        width: 25px;
    }

    .modal {
        display: block; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 100px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0,0,0); /* Fallback color */
        background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
    }

    /* Modal Content */
    .modal-content {
        background-color: #fefefe;
        margin: auto;
        padding: 30px;
        border: 1px solid #888;
        width: 80%;
    }

    /* The Close Button */
    .close {
        color: #aaaaaa;
        float: right;
        font-size: 16px;
        font-weight: bold;
    }

    .close:hover,
    .close:focus {
        color: #000;
        text-decoration: none;
        cursor: pointer;
    }

    img {
        width: auto;
        height: 200px;
        border-radius: 20px;
    }

    #photo-gallery {
        margin: 5% 10% 10% 10%;
    }

    h1 {
        margin-top: 5%;
        color: rgba(51, 92, 80, 0.8);
    }

    .start-data {
        margin-top: 5%;
        margin-bottom: 5%;
        border-radius: 15px;
        background-color: rgb(146, 179, 146);
        padding-top: 2%;
        padding-bottom: 2%;
    }

    p {
        margin: 5%;
        font-size: 16px;
    }

    div.status{
        align-content: center;
        text-align: center;
        margin: 2%;
        font-size: 18px;
        background-color: rgb(146, 179, 146) !important;
        color: black;
    }

    div .pill {
        display: inline-block;
        margin: 20px 10px 0 0;
        padding: 6px 12px;
        background: white;
        border-radius: 20px;
        font-size: 12px;
        letter-spacing: 1px;
        font-weight: bold;
        color: black;
        cursor: pointer;
    }

    .pillPic {
        display: inline-block;
        margin: 20px 10px 0 0;
        padding: 6px 12px;
        border-radius: 10px;
        cursor: pointer;
    }

    #map-container {
        margin:5% 10% 10% 10%;
    }

    #options-btns{
        margin-top: 5%;
    }

    #options-btns button {
        color: white;
    }

    #options-btns button:hover {
        background-color: rgb(6, 94, 40);
    }

    #btn-calendar {
        width: 30px;
        height: 30px;
    }
    
</style>
<template>
    <div class="popup-overlay" @click="emitClose()">
        <div class="container popup" @click.stop>
            <div class="row modal-style" v-show="!showReservationForm && !showRentalActions">

                <div class="col col-style">
                    <div id="info-holder">
                        <div v-if="showCottageIcon" class="d-flex justify-content-center">
                            <img class="icon-style" src="@/assets/cottage_icon.png" alt="">
                        </div>
                        <div v-else-if="showBoatIcon" class="d-flex justify-content-center">
                            <img class="icon-style" src="@/assets/boat_icon.png" alt="">
                        </div>
                        
                        <div v-else-if="showAdventureIcon" class="d-flex justify-content-center"> 
                            <img class="icon-style" src="@/assets/adventure_icon.png" alt="">
                        </div>
                        
                        <div class="row">
                            <div class="col-md-8">
                                <label>Name:</label>
                                <input type="text" v-model="name" disabled>
                            </div>
                            <div class="col-md-2">
                                <label>Price:</label>
                                <input type="text" v-model="price" disabled>
                            </div>
                            <div class="col-md-2">
                                <label>Rating:</label>
                                <input type="text" v-model="rate" disabled>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-9">
                                <label>Address:</label>
                                <input type="text" v-model="address" disabled>
                            </div>
                            <div class="col-sm-3" v-if="isClient">
                                <label>Capacity:</label>
                                <input type="text" v-model="capacity" disabled>
                            </div>
                        </div>

                        <label>Description:</label>
                        <textarea class="text-area-style" v-model="description" disabled></textarea>

                        <div v-if="showDetailedCottage">
                            <label>Rules:</label>
                            <textarea class="text-area-style" v-model="rules" disabled></textarea>
                            <div class="row">
                                <div class="col-sm-9">
                                    <label>Additional services:</label>
                                    <input type="text" v-model="additionalServices" disabled>
                                </div>
                                <div class="col-sm-3">
                                    <label>Rooms:</label>
                                    <input type="text" v-model="noRooms" disabled>
                                </div>
                            </div>

                        </div>
                        <div v-if="showDetailedAdventure">
                            <label>Rules:</label>
                            <textarea class="text-area-style" v-model="rules" disabled></textarea>
                            <label>Instructor biography:</label>
                            <textarea class="text-area-style" v-model="biography" disabled></textarea>
                            <label>Fishing equipment:</label>
                            <input type="text" v-model="fishingEquipment" disabled>
                        </div>
                        <div v-if="showDetailedBoat">

                        </div>

                        <label>Location on map:</label>
                        <MapContainer
                            :mapHeight="'200'"
                            :coordinates="[lon, lat]"
                        />
                    </div>
                </div>

                <div class="col">
                    <div class="images-cont">
                        <label>Images:</label>
                        <div class="row justify-content-center">
                            <img v-for="pic in images" :key="pic" class="entity-img-style" :src="setProfPic(pic)" alt="">
                        </div>
                    </div>
                </div>

            </div>

            <RentalReservationForm
            />
        </div>
    </div>
</template>

<script>
    import axios from 'axios';
    import MapContainer from "@/components/MapContainer.vue";
    import RentalReservationForm from "@/components/RentalReservationForm.vue";

    export default {
        name: "RentalViewModal",
        components: {
            MapContainer,
            RentalReservationForm
        },
        props: {
            id: Number,
            type: String
        },
        data() {
            return {
                name: "",
                description: "",
                price: "",
                rate: "",
                address: "",
                images: [],
                lon: "",
                lat: "",

                // svi rentali
                rules: "",
                capacity: "",

                // COTTAGE
                additionalServices: "",  // poseduje i BOAT
                noRooms: "",

                // ADVENTURE
                biography: "",
                fishingEquipment: "",  // poseduje i BOAT

                // BOAT  
                boatType: "",
                boatLength: "",
                engineNumber: "",
                enginePower: "",
                maxSpeed: "",
                navigationEquipment: "",

                showCottageIcon: false,
                showBoatIcon: false,
                showAdventureIcon: false,

                isClient: window.localStorage.getItem('token') !== null,

                showDetailedCottage: false,
                showDetailedBoat: false,
                showDetailedAdventure: false
            }
        },
        created() {
            if (this.isClient) {
                switch(this.type){
                    case "Cottage":
                        axios.get("api/rental/cottage/details/" + this.id, {headers: {'authorization': window.localStorage.getItem("token") }})
                            .then((response) => {
                                this.showDetailedCottage = true;
                                setUpDefaultRentalInfo(this, response);
                                setUpDetailedCottageInfo(this, response);
                            });
                        break;
                    case "Boat":
                        axios.get("api/rental/boat/details/" + this.id, {headers: {'authorization': window.localStorage.getItem("token") }})
                            .then((response) => {
                            });
                        break;
                    case "Adventure":
                        axios.get("api/rental/adventure/details/" + this.id, {headers: {'authorization': window.localStorage.getItem("token") }})
                            .then((response) => {
                                this.showDetailedAdventure = true;
                                setUpDefaultRentalInfo(this, response);
                                setUpDetailedAdventureInfo(this, response);
                                console.log(response.data);
                            });
                        break;
                }
            }
            else
                axios.get("api/rental/basic/" + this.id +"?type=" + this.type).then((response) => {
                    setUpDefaultRentalInfo(this, response);
                });
        },
        methods: {
            emitClose() {
                this.showCottageIcon = false;
                this.showBoatIcon = false;
                this.showAdventureIcon = false;
                this.$emit('close');
            },
            setProfPic(imagePath) {
                try{
                    return require('@/assets/' + imagePath);
                } catch(e) {}
            }
        }
    }

    function setUpDefaultRentalInfo(params, response) {
        if (params.type === "Cottage")
            params.showCottageIcon = true;
        else if (params.type === "Adventure")
            params.showAdventureIcon = true;
        else if (params.type === "Boat")
            params.showBoatIcon = true;

        let rental = response.data;
        
        params.name = rental.name;
        params.description = rental.description;
        params.price = rental.price + "€/day";
        params.rate = rental.rate + " ★";
        let adr = rental.address;
        params.address = adr.street + ", " + adr.placeName + ", " + adr.country;
        params.lon = adr.lon;
        params.lat = adr.lat;
        params.images = rental.photos;
    }

    function setUpDetailedCottageInfo(params, response) {
        let cottage = response.data;
        
        params.rules = cottage.rules;
        params.capacity = cottage.capacity + " 웃";

        params.additionalServices = cottage.additionalServices.replaceAll(",", ", ");
        params.noRooms = cottage.noRooms;
    }

    function setUpDetailedBoatInfo(params, response) {
        // let cottage = response.data;
        
        // params.rules = cottage.rules;
        // params.capacity = cottage.capacity + " 웃";

        // TODO - not implemented yet
    }

    function setUpDetailedAdventureInfo(params, response) {
        let adventure = response.data;
        
        params.rules = adventure.rules;
        params.capacity = adventure.capacity + " 웃";

        params.biography = adventure.biography;
        params.fishingEquipment = adventure.fishingEquipment;
    }
</script>

<style scoped>
    .popup-overlay {
        display: block;
        position: fixed;
        z-index: 999;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background-color: #000000da;
        overflow: auto;
        width: 100%; 
        height: 100%; 
        padding-top: 25px; 
    }

    .popup {
        background: white;
        border: 2px solid black;
        border-radius: 20px;
        margin: auto;
        width: 80%;
        min-height: 90vh;
    }

    div label {
        font-size: 15px;
    }

    .modal-style{
        padding: 20px;
    }

    .icon-style {
        width: 40px;
        height: 40px;
        margin-left: auto;
        margin-right: auto;
        margin-bottom: 10px;
    }

    #info-holder {
        text-align: left;
    }

    .text-area-style {
        display: block;
        padding: 10px 6px;
        width: 100%;
        box-sizing: border-box;
        border: none;
        border-bottom: 1px solid #ddd;
        color: #555;
        max-height: 200px;
        min-height: 50px;
    }

    .images-cont {
        margin-top: 48px;
        padding-left: 20px;
        padding-right: 20px;
        min-height: 90%;
    }

    .entity-img-style:hover {
        background-color: rgba(26, 25, 25, 0.1);
        height: 245px;
        width: 260px;
        margin-left: 5px;
        margin-right: 5px;
        margin-bottom: 5px;
    }

    .entity-img-style {
        height: 230px;
        width: 240px;
        border-radius: 25px;
        margin-left: 15px;
        margin-right: 15px;
        margin-bottom: 20px;
    }

</style>
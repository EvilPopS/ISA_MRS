<template>
    <div class="popup-overlay" @click="emitClose()">
        <div class="container popup" @click.stop>
            <div class="row modal-style" v-show="!this.toShowReservationForm && 
                                                    !this.toShowRentalActions && 
                                                    !this.toShowOwnerProfile && 
                                                    !this.toShowRatingsAndReviews">
                <div class="row btns-cont" v-if="isClient">
                    <div class="col justify-content-center">
                        <button class="btn-style" @click="showReservationForm">Reservation calendar</button>
                    </div>
                    <div class="col justify-content-center">
                        <button class="btn-style rent-ratings" @click="showRatingsAndReviews">Ratings &amp; reviews</button>
                    </div>
                    <div class="col justify-content-center">
                        <button class="btn-style rent-owner-btn" @click="showOwnerProfile">Owner Profile</button>
                    </div>
                    <div class="col justify-content-center">
                        <button class="btn-style" @click="showActionResevations">Rental actions</button>
                    </div>
                </div>

                <div class="col">
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
                            <div class="col-md-7">
                                <label>Name:</label>
                                <input type="text" v-model="name" disabled>
                            </div>
                            <div class="col-md-3">
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

                        <div v-else-if="showDetailedAdventure">
                            <label>Rules:</label>
                            <textarea class="text-area-style" v-model="rules" disabled></textarea>
                            <label>Instructor biography:</label>
                            <textarea class="text-area-style" v-model="biography" disabled></textarea>
                            <label>Fishing equipment:</label>
                            <input type="text" v-model="fishingEquipment" disabled>
                        </div>

                        <div v-else-if="showDetailedBoat">
                            <div class="row">
                                <div class="col-sm-4">
                                    <label>Engine number:</label>
                                    <input type="text" v-model="engineNumber" disabled>
                                </div>
                                <div class="col-sm-4">
                                    <label>Max speed:</label>
                                    <input type="text" v-model="maxSpeed" disabled>
                                </div>
                                <div class="col-sm-4">
                                    <label>Engine power:</label>
                                    <input type="text" v-model="enginePower" disabled>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-10">
                                    <label>Fishing equipment:</label>
                                    <input type="text" v-model="fishingEquipment" disabled>
                                </div>
                                <div class="col-sm-2">
                                    <label>Length:</label>
                                    <input type="text" v-model="boatLength" disabled>
                                </div>
                            </div>
                            <label>Navigation equipment:</label>
                            <input type="text" v-model="navigationEquipment" disabled>
                        </div>

                        <label>Location on map:</label>
                        <MapContainer
                            :key="[lon, lat]"
                            :mapHeight="'300'"
                            :coordinates="[lon, lat]"
                            :mapEditable="false"
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

            <RentalReservationForm v-if="toShowReservationForm"
                :rentalId="rentalId"
                :rentalType="rentalType"
                :rentalPrice="price"
                :rentalReservations="normalReservations.concat(actionReservations)"
                @close="reopenRentalDetails"
            />

            <RentalActionReservations v-if="toShowRentalActions"
                :oldPrice="price"
                :reservations="actionReservations"
                @close="reopenRentalDetails"
                @update-action-reservations="updateActionReservs"
            />

            <RentalOwnerProfileView v-if="toShowOwnerProfile"
                :ownerInfo="ownerInfo"
                :rentalType="rentalType"
                @close="reopenRentalDetails"
            />

            <RentalReviewingModal v-if="toShowRatingsAndReviews"
                :rentalId="rentalId"
                :rentalType="rentalType"
                @close="reopenRentalDetails"
            />
        </div>
    </div>
</template>

<script>
    import axios from 'axios';
    import MapContainer from "@/components/MapContainer.vue";
    import RentalReservationForm from "@/components/RentalReservationForm.vue";
    import RentalActionReservations from "@/components/RentalActionReservations.vue";
    import RentalOwnerProfileView from "@/components/RentalOwnerProfileView.vue";
    import RentalReviewingModal from "@/components/RentalReviewingModal.vue";

    export default {
        name: "RentalViewModal",
        components: {
            MapContainer,
            RentalReservationForm,
            RentalActionReservations,
            RentalOwnerProfileView,
            RentalReviewingModal
        },
        props: {
            id: Number,
            type: String,
        },
        data() {
            return {
                rentalId: this.id,
                rentalType: this.type,

                ownerInfo: {},

                normalReservations: [],
                actionReservations: [],
                name: "",
                description: "",
                price: "",
                rate: "",
                address: "",
                images: [],
                lon: 0,
                lat: 0,

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
                showDetailedAdventure: false,
                toShowReservationForm: false,
                toShowRentalActions: false,
                toShowOwnerProfile: false,
                toShowRatingsAndReviews: false
            }
        },
        mounted() {
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
                                this.showDetailedBoat = true;
                                setUpDefaultRentalInfo(this, response);
                                setUpDetailedBoatInfo(this, response);
                            });
                        break;
                    case "Adventure":
                        axios.get("api/rental/adventure/details/" + this.id, {headers: {'authorization': window.localStorage.getItem("token") }})
                            .then((response) => {
                                this.showDetailedAdventure = true;
                                setUpDefaultRentalInfo(this, response);
                                setUpDetailedAdventureInfo(this, response);
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
            },
            showReservationForm() {
                this.toShowReservationForm = true;
            },
            showActionResevations() {
                this.toShowRentalActions = true;
            },
            showOwnerProfile() {
                this.toShowOwnerProfile = true;
            },
            showRatingsAndReviews() {
                this.toShowRatingsAndReviews = true;
            },
            reopenRentalDetails() {
                if (this.toShowReservationForm)
                    this.$emit('reload');
                this.toShowReservationForm = false;
                this.toShowRentalActions = false;
                this.toShowOwnerProfile = false;
                this.toShowRatingsAndReviews = false;
            },
            updateActionReservs(resId) {
                this.actionReservations.splice(this.actionReservations.findIndex(res => res.id === resId), 1);
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
        
        params.ownerInfo = rental.owner;

        params.name = rental.name;
        params.description = rental.description;
        params.price = rental.price + " €/day";
        params.rate = rental.rate + " ★";
        let adr = rental.address;
        params.address = adr.street + ", " + adr.placeName + ", " + adr.country;
        params.lon = adr.lon;
        params.lat = adr.lat;
        params.images = rental.photos;
        params.actionReservations = rental.actionReservations;
        params.normalReservations = rental.normalReservations;
    }

    function setUpDetailedCottageInfo(params, response) {
        let cottage = response.data;
        
        params.rules = cottage.rules;
        params.capacity = cottage.capacity + " 웃";

        params.additionalServices = cottage.additionalServices.replaceAll(",", ", ");
        params.noRooms = cottage.noRooms;
    }

    function setUpDetailedBoatInfo(params, response) {
        let boat = response.data;

        params.rules = boat.rules;
        params.capacity = boat.capacity + " 웃";
        params.fishingEquipment = boat.fishingEquipment.replaceAll(",", ", ");
        params.boatType = boat.boatType;
        params.boatLength = boat.boatLength + " m";
        params.engineNumber = boat.engineNumber;
        params.enginePower = boat.enginePower + " kW";
        params.maxSpeed = boat.maxSpeed + " km/h";
        params.navigationEquipment = boat.navigationEquipment.replaceAll(",", ", ");
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

    .btns-cont {
        margin-bottom: 20px;
        border-bottom: 1px solid black;
        padding: 10px 0;
        border-radius: 0 0 30px 30px;
        background: linear-gradient(white, rgba(170, 165, 165, 0.3))
    }

    .btn-style {
        background: rgb(51, 160, 233);
        margin: 10px 0px;
        font-weight: bold;
        font-size: 15px;
        border-radius: 20px;
        height: 30px;
        width: 180px;
    }

    .btn-style:hover {
        margin: 5px 0px;
        height: 40px;
        width: 190px;
    }

    .rent-owner-btn {
        background: rgb(18, 161, 18);
    }

    .rent-ratings {
        background: rgb(161, 128, 18);
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
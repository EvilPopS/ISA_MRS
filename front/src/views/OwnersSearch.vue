<template>
    <div v-if="showSearh">
        <div class="split left">
            <p id="search-res-init-mess" v-show="showInitSearchResMess">
                Nothing to show here... Try that fancy search form on your right side :D
            </p>
            <p id="no-search-result-mess" v-show="toShowNoResultsMess">
                Search resulted in... no result, don't be so strict with that criterion!
            </p>
            <div v-for="res in this.searchResult" :key="res.id"> 
                <div class="card" @click="showDetailRental(res.id)"> 
                    <p class="entity-name">{{res.name}}</p>
                    <img v-if="res.type == 'Adventure'" class="card-icon" src="@/assets/adventure_icon.png">
                    <img v-else-if="res.type == 'Cottage'" class="card-icon" src="@/assets/cottage_icon.png">
                    <img v-else class="card-icon" src="@/assets/boat_icon.png">
                    <div class="entity-info-holder">
                        <span class="information-style"><span class="attr-name-style">Location: </span>{{res.city}}</span>
                        <span class="information-style"><span class="attr-name-style">Price: </span>{{res.price}} &euro;/day</span>
                        <span class="information-style"><span class="attr-name-style">Capacity: </span>{{res.capacity}}</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="split right">
            <form id="search-form" class="form-group" @submit.prevent="submitSearchForm()">
                <div class="col-md-12">
                    <label >Service name(optional):</label>
                    <input  type="text" v-model="serviceName" title="Service name">
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <label >Min Price:</label>
                        <input  type="number" min="0" v-model="minPrice" title="Minimum price/day for a rental to be. (euros)">
                    </div>
                    <div class="col-md-6">
                        <label >Max Price:</label>
                        <input  type="number" min="0" v-model="maxPrice" title="Maximum price/day for a rental to be. (euros)">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-4">
                        <label >Min Capacity: </label>
                        <input  type="number" min="1" v-model="capacity" title="Min Capacity of your service.">
                    </div> 
                    <div class="col-md-8">
                        <label >Location: </label>
                        <input  type="text" v-model="location">
                    </div>
                </div>
                <div class="submit">
                    <button id="subBtn">Search</button>
                </div>
            </form>
        </div>
    </div>
    <div v-else>
        <h3>You don't have permission to visit this page!</h3>
    </div>
    <div v-if="showDetailsCottage">
        <DetailCottageModal
        :cottage = "detailRental"
        @modal-closed = "showDetailsCottage = false"
        @succ-popup-close = "succPopUpClose"
        />
    </div>
    <ErrorPopUp v-show="errorPopUpVisible" 
        @close = closePopUp
        :mess = errMessage
    />
    <SuccessPopUp v-show="succPoupUp"
        @close = "succPoupUp = false"
        :mess = succMessage
    />
</template>

<script>
    import ErrorPopUp from "@/components/ErrorPopUp.vue"
    import DetailCottageModal from '../components/DetailCottageModal.vue'
    import SuccessPopUp from '../components/SuccessPopUp.vue'
    import axios from 'axios';

    export default {
        name: "OwnersSearch",
        components: {
            ErrorPopUp, DetailCottageModal, SuccessPopUp
        },
        data() {
            return {
                minPrice: '',
                maxPrice: '',
                location: '',
                capacity: '',
                serviceName: '',
                showSearh: false,
                roleURL: '',
                searchRole: '',
                detailRental: {},
                showDetailsCottage: false,
                errMessage : '',
                errorPopUpVisible: false,
                succPoupUp: false,
                succMessage: '',
                toShowNoResultsMess: false,
                showInitSearchResMess: true,

                searchResult: []
            };
        },
        methods: {
            closePopUp() {
                this.errorPopUpVisible = false;
            },
            succPopUpClose() {
                this.succPopUpVisible = false;
            },
            submitSearchForm() {
                let formData = {
                    minPrice: this.minPrice,
                    maxPrice: this.maxPrice,
                    location: this.location,
                    capacity: this.capacity,
                    serviceName: this.serviceName,
                    searchRole: this.searchRole
                }

                try { validateForm(formData) }
                catch(errMess) {
                    this.errMessage = errMess;
                    this.errorPopUpVisible = true;
                    return;
                }
                axios.get("api/" + this.roleURL + "/ownersSearch?"
                                                + "&minPrice=" + this.minPrice
                                                + "&maxPrice=" + this.maxPrice
                                                + "&location=" + this.location 
                                                + "&minCapacity=" + this.capacity 
                                                + "&serviceName=" + this.serviceName,
                                                {headers: {'authorization': window.localStorage.getItem("token") }} 
                   ).then((response) => {
                        this.showInitSearchResMess = false;
                        response.data.length === 0 ? this.toShowNoResultsMess = true : this.toShowNoResultsMess = false    

                        this.searchResult = response.data;
                   }).catch((error) => {
                        this.errMessage = "Error happened: " + error.data
                        this.errorPopUpVisible = true
                    });
            },
            showDetailRental(rentalId) {
                axios.get('api/' + this.roleURL + '/find-one-rental/' + rentalId, {headers: {'authorization': window.localStorage.getItem("token") }})
                .then((response) => {
                    this.detailRental = response.data
                    if (this.searchRole === "COTTAGE_OWNER")
                        this.showDetailsCottage = true

                    }).catch((error) => {
                        this.errMsg = "Error happened: " + error.name
                        this.errorPoup = true
                    })
            }
        },
        mounted() {
            this.searchRole = window.localStorage.getItem("userRole")
            if (this.searchRole === "COTTAGE_OWNER"){
                this.roleURL = "cottage-owner"
                this.showSearh = true
            } else if (this.searchRole === "INSTRUCTOR"){
                this.roleURL = "fishingInstructor"
                this.showSearh = true
            } else {
                //za boat
            }
        }
    }

    function validateForm(formData) {
        if (formData.maxPrice !== '' && formData.minPrice !== '' && formData.maxPrice < formData.minPrice)
            throw "You mixed up minimal and maximal price values. Fix it up!"
        
        if (formData.maxRate !== '' && formData.minRate !== '' && formData.maxRate < formData.minRate)
            throw "You mixed up minimal and maximal rating values. Fix it up!"

        if (formData.location !== '' && !/^[a-zA-Z -]{2,50}$/.test(formData.location))
            throw "Make sure you entered valid location name."

        if (formData.capacity === '')
            throw "You need to enter capacity!"

    }
</script>

<style scoped>
    label {
        font-size: 11px;
    }

    .split {
        height: 100%;
        position: fixed;
        z-index: 1;
        top: 0;
        overflow-x: hidden;
        padding-top: 10px;
    }

    .left {
        width: 60%;
        margin-top: 32px;
        padding-top: 40px;
        padding-left: 20px;
        padding-right: 20px;
        left: 0;
    }

    .right {
        width: 40%;
        right: 0;
    }

    .date-inp {
        width: 20px;
    }

    #search-form {
        margin: 0;
        width: 100%;
        margin: 30px auto;
        text-align: left;
        padding: 40px;
        border-radius: 10px;
    }

   .checkbox-border {
        border: solid 1px #aaa;
        border-radius: 20px;
        padding-bottom: 10px;
        margin-top: 10px;
    }

    .card {
        border: 1px solid rgb(70, 68, 68);
        border-radius: 30px;
        padding: 10px;
        margin: 20px 40px;
    }
    .card:hover {
        background: rgba(170, 167, 167, 20%);
        margin: 20px 0px;
        border: 1px solid rgb(34, 33, 33);
    }

    .entity-name {
        color: rgb(9, 104, 9);
        align-content: center;
        font-weight: bold;
        font-size: 20px;
        font-variant: small-caps;
    }

    .entity-info-holder {
        display: inline;
    }


    .card-icon {
        width: 30px;
        height: 30px;
        margin-left: auto;
        margin-right: auto;
        margin-bottom: 10px;
    }

    .attr-name-style {
        color: rgb(9, 104, 9);
        font-weight: 500;
        font-size: 20px;
        font-variant: small-caps;
    }

    .information-style {
        margin: 0 20px;
    }

    #search-res-init-mess {
        color: rgb(11, 77, 11);
        align-content: center;
        font-weight: bold;
        font-variant: small-caps;
        font-family: "Arial Black", Gadget, sans-serif;
        font-size: 20px;
        letter-spacing: -0.6px;
        word-spacing: 1px;
        width: 70%;
        align-content: center;
        margin: 0 auto;
    }

    #no-search-result-mess {
        color: rgb(202, 99, 14);
        align-content: center;
        font-weight: bold;
        font-variant: small-caps;
        font-family: "Arial Black", Gadget, sans-serif;
        font-size: 20px;
        letter-spacing: -0.6px;
        word-spacing: 1px;
        width: 70%;
        align-content: center;
        margin: 0 auto;
    }

    h3 {
        margin-top: 10%;
    }

    button {
        margin-top: 5%;
    }

</style>
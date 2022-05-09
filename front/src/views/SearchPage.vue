<template>
    <div class="split left">
        <p id="search-res-init-mess" v-show="showInitSearchResMess">
            Nothing to show here... Try that fancy search form on your right side :D
        </p>
        <p id="no-search-result-mess" v-show="toShowNoResultsMess">
            Search resulted in... no result, don't be so strict with that criterion!
        </p>
        <div v-for="res in this.searchResult" :key="res.id" @click="entityBasicView(res.id, res.type);"> 
            <div class="card"> 
                <p class="entity-name">{{res.name}}</p>
                <img v-if="res.type == 'Adventure'" class="card-icon" src="@/assets/adventure_icon.png">
                <img v-else-if="res.type == 'Cottage'" class="card-icon" src="@/assets/cottage_icon.png">
                <img v-else class="card-icon" src="@/assets/boat_icon.png">
                <div class="entity-info-holder">
                    <span class="information-style"><span class="attr-name-style">Location: </span>{{res.city}}</span>
                    <span class="information-style"><span class="attr-name-style">Price: </span>{{res.price}} &euro;/day</span>
                    <span class="information-style"><span class="attr-name-style">Rating: </span>{{res.rate}} &#9733;</span>
                </div>
            </div>
        </div>
    </div>
    <div class="split right">
        <form id="search-form" class="form-group" @submit.prevent="submitSearchForm()">
            <div class="row">
                <div class="col-md-6">
                    <label >Start Date:</label>
                    <input  type="date" v-model="startDate" title="The starting date you wish for your rental period to be.">
                </div>
                <div class="col-md-6">
                    <label >End Date:</label>
                    <input  type="date" v-model="endDate" title="The ending date you wish for your rental period to be.">
                </div>
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
                <div class="col-md-3">
                    <label >Min Rating: </label>
                    <input  type="number" min="1" max="5" v-model="minRate" title="Minimum rating for a rental to be. (stars)">
                </div> 
                <div class="col-md-3">
                    <label >Max Rating: </label>
                    <input  type="number" min="1" max="5" v-model="maxRate" title="Maximum rating for a rental to be. (stars)">
                </div> 
                <div class="col-md-6">
                    <label >Location: </label>
                    <input  type="text" v-model="location">
                </div>
            </div>
            
            <label>What to search for: </label>
            <div class="row checkbox-border d-flex justify-content-center text-center">
                <div class="col-sm">
                    <label>Adventures: </label>
                    <input type="checkbox" value="adventures" v-model="entities">
                </div>
                <div class="col-sm">
                    <label>Cottages: </label>
                    <input type="checkbox" value="cottages" v-model="entities">
                </div>
                <div class="col-sm">
                    <label>Boats: </label>
                    <input type="checkbox" value="boats" v-model="entities">
                </div>                
            </div>

            <div class="submit">
                <button id="subBtn">Search</button>
            </div>
        </form>
    </div>

    <ErrorPopUp v-show="errorPopUpVisible" 
        @close = closePopUp
        :mess = errMessage
    /> 
</template>

<script>
    import ErrorPopUp from "@/components/ErrorPopUp.vue"
    import axios from 'axios';

    export default {
        name: "SearchPage",
        components: {
            ErrorPopUp
        },
        data() {
            return {
                startDate: '',
                endDate: '',
                minPrice: '',
                maxPrice: '',
                location: '',
                minRate: '',
                maxRate: '',
                entities: ["adventures", "cottages", "boats"],

                errMessage : '',
                errorPopUpVisible: false,
                toShowNoResultsMess: false,
                showInitSearchResMess: true,

                searchResult: []
            };
        },
        methods: {
            closePopUp() {
                this.errorPopUpVisible = false;
            },
            entityBasicView(id, type) {
                console.log(id, type);
                window.sessionStorage.setItem("entityDataId", id);
                window.sessionStorage.setItem("entityDataType", type);

                this.$router.push({ name: "EntityBasicView" })
            },
            submitSearchForm() {
                let formData = {
                    startDate: this.startDate,
                    endDate: this.endDate,
                    minPrice: this.minPrice,
                    maxPrice: this.maxPrice,
                    location: this.location,
                    minRate: this.minRate,
                    maxRate: this.maxRate,
                    entities: this.entities
                }

                try { validateForm(formData) }
                catch(errMess) {
                    this.errMessage = errMess;
                    this.errorPopUpVisible = true;
                    return;
                }
                
                axios.get("api/rental/search?" + "startDate=" + formatDateStr(this.startDate) 
                                                + "&endDate=" + formatDateStr(this.endDate) 
                                                + "&minPrice=" + this.minPrice
                                                + "&maxPrice=" + this.maxPrice
                                                + "&location=" + this.location 
                                                + "&minRate=" + this.minRate 
                                                + "&maxRate=" + this.maxRate 
                                                + "&entities=" + this.entities.join(","))
                   .then((response) => {
                        this.showInitSearchResMess = false;
                        response.data.length === 0 ? this.toShowNoResultsMess = true : this.toShowNoResultsMess = false    

                        this.searchResult = response.data;
                   });
            }
        }
    }

    function validateForm(formData) {
        if (formData.startDate === '' || formData.endDate === '') 
            throw "Please make sure you entered both start and end date for your prefered reservation period."
        
        if (new Date(formData.startDate) <= new Date() || new Date(formData.endDate) <= new Date())
            throw "You can change past or what? Select some future date!";

        if (new Date(formData.startDate) >= new Date(formData.endDate))
            throw "How comes future is before past? Check those start and ending dates...";
    
        if (formData.maxPrice !== '' && formData.minPrice !== '' && formData.maxPrice < formData.minPrice)
            throw "You mixed up minimal and maximal price values. Fix it up!"
        
        if (formData.maxRate !== '' && formData.minRate !== '' && formData.maxRate < formData.minRate)
            throw "You mixed up minimal and maximal rating values. Fix it up!"

        if (formData.location !== '' && !/^[a-zA-Z -]{2,50}$/.test(formData.location))
            throw "Make sure you entered valid location name."

        if (formData.entities.length === 0)
            throw "You need to check at least one rental type out of 3."
    }

    function formatDateStr(dateStr) {
        let dateComps = dateStr.split("-");
        return dateComps[2] + "/" + dateComps[1] + "/" + dateComps[0]; 
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
</style>
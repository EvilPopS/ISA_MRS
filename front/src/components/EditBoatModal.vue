<template>
    <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow" class="close">X</button>
            <div class="container">
                <div class="row">
                    <div class="col-4">
                        <span>Boat</span>
                        <hr class="solid">
                        <label class="label" for="name">Boat name:</label>
                        <input type="text" id="name" class="form-control" v-model="data.name">
                        <label class="label" for="description">description:</label>
                        <input type="text" id="description" class="form-control" v-model="data.description">
                        <label class="label" for="rules">Rules:</label>
                        <input type="text" id="rules" class="form-control" v-model="data.rules">
                        <label class="label" for="type">Boat type:</label>
                        <input type="text" id="type" class="form-control" v-model="data.type">
                        <span>
                            <div class="inline-inputs">
                                <label class="label" for="price"><br>Price/day €:</label>
                                <input type="text" id="price" class="form-control rating" v-model="data.price">
                            </div>
                            <div class="inline-inputs">
                                <label class="label" for="lenght"><br>Lenght(m):</label>
                                <input type="text" id="lenght" class="form-control rating" v-model="data.boatLength">
                            </div>
                        </span>
                        <div>
                            <label class="label" for="additionalServices">Navigation equipment(press alt + , to add):</label>
                            <input type="text" class="form-control" v-model="tempNavEq" @keyup.alt="addNavEq">
                            <div v-for="service in this.localNavEq" :key="service" class="pill">
                                <span  @click="deleteNavEq(service)">{{service}}</span>
                            </div>
                        </div>
                        <div>
                            <label class="label">Fishing equipment(press alt + , to add):</label>
                            <input type="text" class="form-control" v-model="tempFishingEq" @keyup.alt="addFishingEq">
                            <div v-for="service in this.localFishingEq" :key="service" class="pill">
                                <span  @click="deleteFishingEq(service)">{{service}}</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <span>Address&Engine</span>
                        <hr class="solid">
                        <label class="label" for="zip-code">Country:</label>
                        <input type="text" id="zip-code" class="form-control" v-model="data.country">
                        <label class="label" for="city">City:</label>
                        <input type="text" id="city" class="form-control" v-model="data.city">
                        <label class="label" for="street">Street:</label>
                        <input type="text" id="street" class="form-control" v-model="data.street">
                        <label class="label" for="capacity">Capacity:</label>
                        <input type="text" id="capacity" class="form-control" v-model="data.capacity">
                        <label class="label" for="engine-number"><br>Engine number:</label>
                        <input type="text" id="engine-number" class="form-control" v-model="data.engineNumber">
                        <span>
                            <div class="inline-inputs">
                                <label class="label" for="rating">Rating:</label>
                                <input type="text" id="rating" class="form-control rating" v-model="data.averageRating" disabled>
                            </div>
                            <div class="inline-inputs">
                                <label class="label" for="num-ratings">No. ratings:</label>
                                <input type="text" id="num-ratings" class="form-control rating" v-model="data.noRatings" disabled>
                            </div>
                        </span>
                        <span>
                            <div class="inline-inputs">
                                <label class="label" for="engine-power"><br><br>Engine<br>power(ks):</label>
                                <input type="text" id="engine-power" class="form-control rating" v-model="data.enginePower">
                            </div>
                            <div class="inline-inputs">
                                <label class="label" for="max-speed"><br><br>Max<br>speed(km/h):</label>
                                <input type="text" id="max-speed" class="form-control rating" v-model="data.maxSpeed">
                            </div>
                        </span>
                    </div>
                    <div class="col-4">
                        <div>
                            <span>Photos</span>
                            <hr class="solid">
                            <label id="popupLabel">Choose new picture: </label>
                            <input ref="picInp" class="form-control form-control-sm" type="file" @change="newPicAdded" accept="image/*"/>
                            <img id="imgPreview" :src="require('@/assets/' + newPicture)"/>
                        </div>
                        <div v-for="pic in this.localPhotos" :key="pic" class="pillPic">
                            <span  @click="deletePic(pic)"><img class="picGallery" :src="require('@/assets/' + pic)"/></span>
                        </div>
                        <div>
                            <button type="button" class="btn btn-success" @click="AddNewPhoto">Add photo</button>
                        </div>
                        <div id="mapContainer">
                            <MapContainer
                                :coordinates = "[boat.lon, boat.lat]"
                                :map-height = "200"
                                :mapEditable="true"
                                @changed-location = "changedLocationFunc"
                            >
                            </MapContainer>
                        </div>
                    </div>
                </div>
            </div>
            <div class="vstack gap-2 col-md-5 mx-auto" id="options-btns">
                <button type="button" class="btn btn-secondary" @click="changeBoat">Save changes</button>
                <button type="button" class="btn btn-outline-secondary" @click="closeWindow">Cancel</button>
            </div>
        </div>
        <ErrorPopUp v-show="errorPopUpVisible" 
            @close = closePopUp
            :mess = errMessage
        /> 
    
        <SuccessPopUp v-show="localSuccPopUpVisible"
            @close = closeSuccPopUp
            :mess = succMessage
        />
    </div>
</template>

<script>
import ErrorPopUp from "./ErrorPopUp.vue"
import SuccessPopUp from "./SuccessPopUp.vue"
import axios from 'axios';
import MapContainer from "./MapContainer.vue"

export default {
    name: "EditBoatModal",
    components: {
        ErrorPopUp, SuccessPopUp, MapContainer
    },
    props: {
        boat: Object
    },
    data(){
        return {
            tempNavEq: '',
            tempFishingEq: '',
            newPicture: 'addPhoto.png',

            localPhotos: [],
            localNavEq: [],
            localFishingEq: [],

            changedLocation: false,

            errMessage: '',
            succMessage: 'Boat data is changed successfully!',
            errorPopUpVisible: false,
            localSuccPopUpVisible: false,

            data: Object
        }
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
        },
        changedLocationFunc(lon, lat){
            this.changedLocation = true
            this.data.lon = lon
            this.data.lat = lat
        },
        closePopUp() {
            this.errorPopUpVisible = false;
        },
        closeSuccPopUp() {
                this.$emit("succ-popup-close");
                this.$router.go(); 
        },
        addNavEq(e) {
            //uvek je ocpiono prvi parametar event
            //key up je recimo kada se svaki put klikne nesto pojedinacno na tastaturi
            if (e.key === ',' && this.tempNavEq){
                if (!this.localNavEq.includes(this.tempNavEq)){
                    this.localNavEq.push(this.tempNavEq)    //zbog duplikata
                }
                
                this.tempNavEq = ''     //resetuje se trenutni
            }
        },
        addFishingEq(e) {
            //uvek je ocpiono prvi parametar event
            //key up je recimo kada se svaki put klikne nesto pojedinacno na tastaturi
            if (e.key === ',' && this.tempFishingEq){
                if (!this.localFishingEq.includes(this.tempFishingEq)){
                    this.localFishingEq.push(this.tempFishingEq)    //zbog duplikata
                }
                
                this.tempFishingEq = ''     //resetuje se trenutni
            }
        },
        deleteNavEq(service){
            //parametar je skill koji se brise
            //uzimamo trenutni item kad filter iterira i ako je to taj brisemo
            this.localNavEq = this.localNavEq.filter((item) => {
                return service !== item       //ako vratimo true isti su
                //kad se uslov ispuni filtrira sta je stisnuto iz liste
            })
        },
        deleteFishingEq(service){
            //parametar je skill koji se brise
            //uzimamo trenutni item kad filter iterira i ako je to taj brisemo
            this.localFishingEq = this.localFishingEq.filter((item) => {
                return service !== item       //ako vratimo true isti su
                //kad se uslov ispuni filtrira sta je stisnuto iz liste
            })
        },
        newPicAdded(){
            this.newPicture = this.$refs.picInp.value.split("\\")[2]
        },
        deletePic(pic) {
            this.localPhotos = this.localPhotos.filter((item) => {
                return pic !== item       //ako vratimo true isti su
                //kad se uslov ispuni filtrira sta je stisnuto iz liste
            })
        },
        AddNewPhoto(){
            if (!this.localPhotos.includes(this.newPicture)){
                    this.localPhotos.push(this.newPicture)    //zbog duplikata
                }
        },
        changeBoat(){
            try { this.checkInputs(); } 
                catch(error) {        
                    this.errMessage = error;
                    this.errorPopUpVisible = true; 
                    return;
                }

            this.data.photos = this.localPhotos
            this.data.fishingEquipment = []
            this.data.navigationEquipment = []
            let counter1 = 0
            let counter2 = 0
            for (let s in this.localNavEq){
                counter1++
                this.data.navigationEquipment += this.localNavEq[s]
                if (counter1 < this.localNavEq.length) this.data.navigationEquipment += ','
            }
            for (let s in this.localFishingEq){
                counter2++
                this.data.fishingEquipment += this.localFishingEq[s]
                if (counter2 < this.localFishingEq.length) this.data.fishingEquipment += ','
            }
            axios.put("api/boat-owner/change-boat-data", this.data, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then((response) => {
                        this.localSuccPopUpVisible = true;
                    })
                    .catch(err => {
                        if (err.response.status === 404){
                            this.errMsg = "Client or owner with given email address is not found!";
                            this.errorPoup = true;
                        } 
                        else if (error.response.status == 409){
                            this.errMsg = "Conflict situation. Please try again later..";
                            this.errorPoup = true;
                        }
                        else if (err.response.status === 401) {
                            this.errMsg = "You are not authorized!";
                            this.errorPoup = true;
                        }
                        else if (err.response.status === 422) {
                            this.errMsg = "Error! Wrong data!";
                            this.errorPoup = true;
                        } else {
                            this.errMsg = "Error! Wrong data!";
                            this.errorPoup = true;
                        }
                    });

        },
        validate : function(toTest, regex) {
        return regex.test(toTest)
        },
        checkInputs : function(){
            let nameReg = /^[a-zA-Z ]{2,30}$/;
            let numReg = /^[0-9]+$/;
            let streetReg = /^[a-zA-Z0-9 -.,]+$/;
            let cityReg = /^[a-zA-Z -]{2,50}$/;

            if (!this.validate(this.data.name, nameReg))
                throw "Make sure you entered a valid name. Name cannot contain digit.";

            if(!this.validate(this.data.city, cityReg))
                throw "Make sure you entered a valid city name!";
            
            if (!this.validate(this.data.country, cityReg))
                throw "Make sure you entered a valid country name.";
            
            if (!this.validate(this.data.street, streetReg))
                throw "Make sure you entered a valid street name.";
            
            if (!this.validate(this.data.boatLength, numReg) || this.data.boatLength <= 0)
                throw "Make sure your entered valid lenght of boat.";

            if (!this.validate(this.data.maxSpeed, numReg) || this.data.maxSpeed <= 0)
                throw "Make sure your entered valid max speed.";

            if (!this.validate(this.data.enginePower, numReg) || this.data.enginePower <= 0)
                throw "Make sure your entered valid engine power.";

            if (!this.validate(this.data.engineNumber, numReg) || this.data.engineNumber <= 0)
                throw "Make sure your entered valid engine number.";

            if (!this.validate(this.data.type, streetReg))
                throw "Make sure you entered a valid boat type.";
            
            if (!this.validate(this.data.capacity, numReg) || this.data.capacity <= 0)
                throw "Capacity must be greater than 0.";

            if (!this.validate(this.data.price, numReg) || this.data.price <= 0)
                throw "Please enter a valid price.";

            if (this.data.description.length < 25)
                throw "Description must have at least 26 characters";
            
            if (this.data.rules.length < 7)
                throw "Rules must have at least 8 characters";

            if (this.localPhotos.length < 1)
                throw "You must upload at least 1 photo.";

            if (this.localPhotos.length > 4)
                throw "You cannot upload more than 4 photos.";

            if (this.localFishingEq.length < 1)
                throw "You need to add at least one fishing equipment.";

            if (this.localNavEq.length < 1)
                throw "You need to add at least one navigation equipment.";

            if (this.changedLocation && !(this.boat.city !== this.data.city || this.boat.street !== this.data.street)){
                throw "Location on the map is changed, but you didn't change city/street in input fields.";
            }
        }
        
    },
    mounted(){
            this.data = Object.assign({}, this.boat)
            try {
                this.localNavEq = this.boat.navigationEquipment.split(',')
                this.localFishingEq = this.boat.fishingEquipment.split(',')
                this.localPhotos = Object.assign([], this.boat.photos)
            }catch (err){
                if (this.localNavEq.length == 0) this.localNavEq = this.boat.navigationEquipment //ako ne uspe split znaci da ima samo jedna
                if (this.localFishingEq.length == 0) this.localFishingEq = this.boat.fishingEquipment
                this.localPhotos = Object.assign([], this.boat.photos)
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
    padding: 20px;
    border: 1px solid #888;
    width: 90%;
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

    #options-btns{
        margin-top: 5%;
    }

    #options-btns button {
        background-color: rgba(51, 92, 80, 0.8);
        color: white;
    }

    .rating {
        width: 60px;
    }

    .inline-ratings {
        margin: 3%;
    }

    .inline-inputs {
        display: inline-block;
        margin: 0 10%;
    }

    div .pill {
        display: inline-block;
        margin: 20px 10px 0 0;
        padding: 6px 12px;
        background: rgba(51, 92, 80, 0.8);
        border-radius: 20px;
        font-size: 12px;
        letter-spacing: 1px;
        font-weight: bold;
        color: white;
        cursor: pointer;
    }

    .pillPic {
        display: inline-block;
        margin: 20px 10px 0 0;
        padding: 6px 12px;
        border-radius: 10px;
        cursor: pointer;
    }

    .picGallery {
        height: 90px;
        width: auto;
        border-radius: 5px;
    }

    #picGallery:hover {
        border-radius: 10px;
        border: rgba(51, 92, 80, 0.8) solid 3px;
    }
    
    #imgPreview:hover {
        border-radius: 5px;
        height: 150px;
        width: auto;
    }

    #imgPreview {
        border: solid 1px black;
        border-radius: 20px;
        margin-top: 5%;
        height: 70px;
        width: auto;
        padding: 5px;
    }

    #mapContainer {
        margin-top: 30%;
    }

    label {
        font-size: 12px;
    }

</style>
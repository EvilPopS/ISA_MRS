<template>
    <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow" class="close">X</button>
            <div class="container">
                <div class="row">
                    <div class="col-4">
                        <span>Adventure</span>
                        <hr class="solid">
                        <label class="label" for="name">Adventure name:</label>
                        <input type="text" id="name" class="form-control" v-model="data.name">
                        <label class="label" for="description">description:</label>
                        <input type="text" id="description" class="form-control" v-model="data.description">
                        <label class="label" for="rules">Rules:</label>
                        <input type="text" id="rules" class="form-control" v-model="data.rules">
                        <span>
                            <div class="inline-inputs">
                                <label class="label" for="price">Price/day €:</label>
                                <input type="text" id="price" class="form-control rating" v-model="data.price">
                            </div>
                        </span>
                        <label class="label" for="additionalServices">Fishing Equipment(press alt + , to add):</label>
                        <input type="text" class="form-control" v-model="tempFe" @keyup.alt="addService">
                        <div v-for="fe in this.localFishingEqu" :key="fe" class="pill">
                            <span  @click="deleteService(fe)">{{fe}}</span>
                        </div>
                    </div>
                    <div class="col-4">
                        <span>Address</span>
                        <hr class="solid">
                        <label class="label" for="zip-code">Country:</label>
                        <input type="text" id="zip-code" class="form-control" v-model="data.country">
                        <label class="label" for="city">City:</label>
                        <input type="text" id="city" class="form-control" v-model="data.city">
                        <label class="label" for="street">Street:</label>
                        <input type="text" id="street" class="form-control" v-model="data.street">
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
                        <div>
                            <label class="label" for="capacity">Capacity:</label>
                            <input type="text" id="capacity" class="form-control" v-model="data.capacity">
                        </div>
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
                                :coordinates = "[adventure.lon, adventure.lat]"
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
                <button type="button" class="btn btn-secondary" @click="changeadventure">Save changes</button>
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
    name: "EditAdventureModal",
    components: {
        ErrorPopUp, SuccessPopUp, MapContainer
    },
    props: {
        adventure: Object
    },
    data(){
        return {
            tempFe: '',
            newPicture: 'addPhoto.png',

            localPhotos: [],
            localFishingEqu: [],

            changedLocation: false,

            errMessage: '',
            succMessage: 'adventure data is changed successfully!',
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
        addService(e) {
            //uvek je ocpiono prvi parametar event
            //key up je recimo kada se svaki put klikne nesto pojedinacno na tastaturi
            if (e.key === ',' && this.tempFe){
                if (!this.localFishingEqu.includes(this.tempFe)){
                    this.localFishingEqu.push(this.tempFe)    //zbog duplikata
                }
                
                this.tempFe = ''     //resetuje se trenutni
            }
        },
        deleteService(fe){
            //parametar je skill koji se brise
            //uzimamo trenutni item kad filter iterira i ako je to taj brisemo
            this.localFishingEqu = this.localFishingEqu.filter((item) => {
                return fe !== item       //ako vratimo true isti su
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
        changeadventure(){
            try { this.checkInputs(); } 
                catch(error) {        
                    this.errMessage = error;
                    this.errorPopUpVisible = true; 
                    return;
                }

            this.data.photos = this.localPhotos
            this.data.additionalServices = []
            let counter = 0
            for (let s in this.localFishingEqu){
                counter++
                this.data.additionalServices += this.localFishingEqu[s]
                if (counter < this.localFishingEqu.length) this.data.fishingEquipment += ','
            } 
            axios.put("api/fishingInstructor/change-adventure-data", this.data, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then((response) => {
                        this.localSuccPopUpVisible = true;
                    })
                    .catch(function (error) {
                        console.log(error);
                        alert(error.name)
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

            if (this.localFishingEqu.length < 1)
                throw "You need to add at least one local service.";

            if (this.changedLocation && !(this.adventure.city !== this.data.city || this.adventure.street !== this.data.street)){
                throw "Location on the map is changed, but you didn't change city/street in input fields.";
            }
        }
        
    },
    mounted(){
            this.data = Object.assign({}, this.adventure)
            try {
                this.localFishingEqu = this.adventure.fishingEquipment.split(',')
                this.localPhotos = Object.assign([], this.adventure.photos)
            }catch (err){
                this.localFishingEqu = this.adventure.fishingEquipment //ako ne uspe split znaci da ima samo jedna
                this.localPhotos = Object.assign([], this.adventure.photos)
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
        margin-top: 5%;
    }

    label {
        font-size: 12px;
    }

</style>
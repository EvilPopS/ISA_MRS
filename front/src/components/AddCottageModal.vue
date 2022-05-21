<template>
  <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow" class="close">X</button>
            <div class="container">
                <div class="row">
                    <div class="col-4">
                        <span>Cottage</span>
                        <hr class="solid">
                        <label class="label" for="name">Cottage name:</label>
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
                            <div class="inline-inputs">
                                <label class="label" for="noRooms">No. rooms:</label>
                                <input type="text" id="noRooms" class="form-control rating" v-model="data.noRooms">
                            </div>
                        </span>
                        <label class="label" for="additionalServices">Additional services(press alt + , to add):</label>
                        <input type="text" class="form-control" v-model="tempService" @keyup.alt="addService">
                        <div v-for="service in localServices" :key="service" class="pill">
                            <span  @click="deleteService(service)">{{service}}</span>
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
                        <label class="label" for="capacity">Capacity:</label>
                        <input type="text" id="capacity" class="form-control" v-model="data.capacity">
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
                            <span  @click="deletePic(pic)"><img id="picGallery" :src="require('@/assets/' + pic)"/></span>
                        </div>
                        <div>
                            <button type="button" class="btn btn-success btn-added" @click="AddNewPhoto">Add photo</button>
                        </div>
                        <div id="mapContainer">
                            <MapContainer
                                :coordinates = "[19.83383399956332, 45.25697997579121]"
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
                <button type="button" class="btn btn-secondary" @click="saveNewCottage">Save changes</button>
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
    name: "AddCottageModal",
    components: {
        ErrorPopUp, SuccessPopUp, MapContainer
    },
    props: {
        showAddNewCottage: Boolean,
        succPopUpVisible: Boolean
    },
    data(){
        return {
            localSuccPopUpVisible: this.succPopUpVisible, 
            tempService: '',
            newPicture: 'addPhoto.png',

            localPhotos: [],
            localServices: [],
            changedLocation: false,

            errMessage: '',
            succMessage: 'New cottage is added successfully!',
            errorPopUpVisible: false,

            data: {
                name: '',
                description: '',
                rules: '',
                price: '',
                noRooms: 0,
                additionalServices: '',
                city: '',
                street: '',
                country: '',
                averageRating: 0,
                noRatings: 0,
                capacity: 0,
                photos: [],
                lon: '',
                lat: ''
            }
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
            if (e.key === ',' && this.tempService){
                if (!this.localServices.includes(this.tempService)){
                    this.localServices.push(this.tempService)    //zbog duplikata
                }
                
                this.tempService = ''     //resetuje se trenutni
            }
        },
        deleteService(service){
            //parametar je skill koji se brise
            //uzimamo trenutni item kad filter iterira i ako je to taj brisemo
            this.localServices = this.localServices.filter((item) => {
                return service !== item       //ako vratimo true isti su
                //kad se uslov ispuni filtrira sta je stisnuto iz liste
            })
        },
        newPicAdded() {
            this.newPicture = this.$refs.picInp.value.split("\\")[2]
        },
        deletePic(pic) {
            this.localPhotos = this.localPhotos.filter((item) => {
                return pic !== item       //ako vratimo true isti su
                //kad se uslov ispuni filtrira sta je stisnuto iz liste
            })
            this.newPicture = "addPhoto.png"
        },
        AddNewPhoto(){
            this.localPhotos.push(this.newPicture)
        },
        saveNewCottage() {
            try { this.checkInputs(); } 
                catch(error) {        
                    this.errMessage = error;
                    this.errorPopUpVisible = true; 
                    return;
                }

            this.data.photos = this.localPhotos
            let counter = 0
            for (let s in this.localServices){
                counter++
                this.data.additionalServices += this.localServices[s]
                if (counter < this.localServices.length) this.data.additionalServices += ','
            } 
            axios.post("api/cottage-owner/add-cottage", this.data, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then((response) => {
                        this.localSuccPopUpVisible = true;
                    })
                    .catch(function (error) {
                        this.errMessage = "Error happened: " + error.data
                        this.errorPopUpVisible = true
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
            let doubleReg = /(\d+)?\.(\d+)?/;

            if (!this.validate(this.data.name, nameReg))
                throw "Make sure you entered a valid name. Name cannot contain digit.";

            if(!this.validate(this.data.city, cityReg))
                throw "Make sure you entered a valid city name!";
            
            if (!this.validate(this.data.country, cityReg))
                throw "Make sure you entered a valid country name.";
            
            if (!this.validate(this.data.street, streetReg))
                throw "Make sure you entered a valid street name.";
            
            if (!this.validate(this.data.noRooms, numReg) || this.data.noRooms <= 0)
                throw "Make sure your entered valid number of rooms.";
            
            if (!this.validate(this.data.capacity, numReg) || this.data.capacity <= 0)
                throw "Capacity must be greater than 0.";

            if (!this.validate(this.data.price, numReg) || this.data.price <= 0)
                throw "Please enter a valid price.";

            if (this.data.description.length < 7)
                throw "Description must have at least 8 characters";
            
            if (this.data.rules.length < 7)
                throw "Rules must have at least 8 characters";

            if (this.localPhotos.length < 1)
                throw "You must upload at least 1 photo.";

            if (this.localPhotos.length > 4)
                throw "You cannot upload more than 4 photos.";

            if (this.localServices.length < 1)
                throw "You need to add at least one local service.";

            if (!this.changedLocation)
                throw "You need to change location on the map";

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

    .btn-added{
        width: 100px; 
        margin: 10px auto;
        display: block;
    }

    .pillPic {
        display: inline-block;
        margin: 20px 10px 0 0;
        padding: 6px 12px;
        border-radius: 10px;
        cursor: pointer;
    }

    #picGallery {
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

</style>
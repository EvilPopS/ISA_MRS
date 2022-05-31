<template>
  <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow" class="close">X</button>
            <div class="container">
                <div class="row">
                    <div class="col-4">
                        <span class="span-text">Adventure</span>
                        <hr class="solid">
                        <label class="label" for="name">Adventure</label>
                        <input type="text" id="name" class="form-control" v-model="data.name">
                        <label class="label" for="description">Description</label>
                        <input type="text" id="description" class="form-control" v-model="data.description">
                        <label class="label" for="rules">Rules</label>
                        <input type="text" id="rules" class="form-control" v-model="data.rules">
                        <span >
                            <div class="inline-inputs">
                                <label class="label" for="price">Price</label>
                                <input type="text" id="price" class="form-control rating" v-model="data.price">
                            </div>
                            <div class="inline-inputs">
                                <label class="label" for="cancellationConditions">Cancellation conditions</label>
                                <input type="text" id="cancellationConditions" class="form-control rating" v-model="data.cancellationConditions">
                            </div>
                        </span>
                        <label class="label" for="fishingEquipment">Additional services(press alt + , to add):</label>
                        <input type="text" class="form-control" v-model="tempFishingEquipment" @keyup.alt="addFishingEqu">
                        <div v-for="fishingEqu in localFishingEquipment" :key="fishingEqu" class="pill">
                            <span  @click="deleteFishingEqu(fishingEqu)">{{fishingEqu}}</span>
                        </div>
                    </div>
                    <div class="col-4">
                        <span class="span-text">Address</span>
                        <hr class="solid">
                        <label class="label" for="zip-code">Country</label>
                        <input type="text" id="zip-code" class="form-control" v-model="data.country">
                        <label class="label" for="city">City</label>
                        <input type="text" id="city" class="form-control" v-model="data.city">
                        <label class="label" for="street">Street</label>
                        <input type="text" id="street" class="form-control" v-model="data.street">
                        <label class="label" for="biography">Biography</label>
                        <input type="text" id="biography" class="form-control" v-model="data.biography">
                    </div>
                    <div class="col-4">
                        <div>
                            <span class="span-text">Photos</span>
                            <hr class="solid">
                            <label id="popupLabel">Choose new picture: </label>
                            <input ref="picInp" class="form-control form-control-sm" type="file" @change="newPicAdded" accept="image/*"/>
                            <img id="imgPreview" :src="require('@/assets/' + newPicture)"/>
                        </div>
                        <div v-for="pic in this.localPhotos" :key="pic" class="pillPic">
                            <span  @click="deletePic(pic)"><img id="picGallery" :src="require('@/assets/' + pic)"/></span>
                        </div>
                        <button type="button" class="btn btn-success btn-added" @click="AddNewPhoto">Add photo</button>
                    </div>
                </div>
            </div>
            <div class="vstack gap-2 col-md-5 mx-auto" id="options-btns">
                <button type="button" class="btn btn-secondary" @click="saveNewAdventure">Add adventure</button>
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

export default {
    name: "AddAdventure",
    components: {
        ErrorPopUp, SuccessPopUp
    },
    props: {
        showAddNewAdventure: Boolean,
        succPopUpVisible: Boolean
    },
    data(){
        return {
            localSuccPopUpVisible: this.succPopUpVisible, 
            tempFishingEquipment: '',
            newPicture: 'addPhoto.png',

            localPhotos: [],
            localFishingEquipment: [],

            errMessage: '',
            succMessage: 'New Adventure is added successfully!',
            errorPopUpVisible: false,

            data: {
                name: '',
                description: '',
                rules: '',
                price: '',
                cancellationConditions: '',
                fishingEquipment: '',
                city: '',
                street: '',
                country: '',
                rating: 0,
                noRatings: 0,
                biography: '',
                photos: [],
            }
        }
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
        },
        closePopUp() {
            this.errorPopUpVisible = false;
        },
        closeSuccPopUp() {
                this.$emit("succ-popup-close");
                this.$router.go(); 
        },
        addFishingEqu(e) {
            //uvek je ocpiono prvi parametar event
            //key up je recimo kada se svaki put klikne nesto pojedinacno na tastaturi
            if (e.key === ',' && this.tempFishingEquipment){
                if (!this.localFishingEquipment.includes(this.tempFishingEquipment)){
                    this.localFishingEquipment.push(this.tempFishingEquipment)    //zbog duplikata
                }
                
                this.tempFishingEquipment = ''     //resetuje se trenutni
            }
        },
        deleteFishingEqu(service){
            //parametar je skill koji se brise
            //uzimamo trenutni item kad filter iterira i ako je to taj brisemo
            this.localFishingEquipment = this.localFishingEquipment.filter((item) => {
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
            this.newPicture = "logo.png"
        },
        AddNewPhoto(){
            this.localPhotos.push(this.newPicture)
        },
        saveNewAdventure() {
            try { this.checkInputs(); } 
                catch(error) {        
                    this.errMessage = error;
                    this.errorPopUpVisible = true; 
                    return;
                }

            this.data.photos = this.localPhotos
            let counter = 0
            for (let s in this.localFishingEquipment){
                counter++
                this.data.fishingEquipment += this.localFishingEquipment[s]
                if (counter < this.localFishingEquipment.length) this.data.fishingEquipment += ','
            } 
            axios.post("api/fishingInstructor/adventures/add-adventure", this.data, {headers: {'authorization': window.localStorage.getItem("token") }})
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
            
            if (!this.validate(this.data.country, numReg))
                throw "Make sure you entered a valid country name.";
            
            if (!this.validate(this.data.street, streetReg))
                throw "Make sure you entered a valid street name.";
            
            if (!this.validate(this.data.cancellationConditions, numReg) || this.data.cancellationConditions <= 0)
                throw "Make sure your entered a positive number.";
            
            if ( this.data.biography.length == 0)
                throw "biography must have at least one word.";

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

            if (this.localFishingEquipment.length < 1)
                throw "You need to add at least one equipment.";
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
        background-color: #198754;
        color: white;
    }

    .rating {
        width: 60px;
    }

    .span-text {
        color : #198754;
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
        background: #198754;
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

    label {
        color : #198754;
    }

    .pillPic {
        display: inline-block;
        margin: 20px 10px 0 0;
        padding: 6px 12px;
        border-radius: 10px;
        cursor: pointer;
    }

    #picGallery {
        height: 80px;
        width: auto;
        border-radius: 5px;
    }

    #picGallery:hover {
        border-radius: 10px;
        border: #198754 solid 3px;
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

</style>
<template>
    <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow" class="close">X</button>
            <div class="container">
                <div class="row">
                    <div class="col-4">
                        <span class="span-text">Adventure</span>
                        <hr class="solid">
                        <label class="label" for="name">Adventure name</label>
                        <input type="text" id="name" class="form-control" :value="adventure.name">
                        <label class="label" for="description">Description</label>
                        <input type="text" id="description" class="form-control" :value="adventure.description">
                        <label class="label" for="rules">Rules:</label>
                        <input type="text" id="rules" class="form-control" :value="adventure.rules">
                        <span>
                            <div class="inline-inputs">
                                <label class="label" for="price">Price</label>
                                <input type="number" id="price" class="form-control rating" :value="adventure.price">
                            </div>
                            <div class="inline-inputs">
                                <label class="label" for="canc-cond">Cancellation conditions</label>
                                <input type="number" id="canc-cond" class="form-control rating" :value="adventure.cancellationConditions">
                            </div>
                        </span>
                        <label class="label" for="biography"> Biography </label>
                        <input type="text" class="form-control" id="biography" v-model="adventure.biography">
                        <label class="label" for="additionalServices">Fishing equipment(press alt + , to add):</label>
                        <input type="text" id="fish-eq" class="form-control" v-model="tempFihingEqu" @keyup.alt="addFishingEqu">
                        <div v-for="fishingEqu in this.localFishingEquipment" :key="fishingEqu" class="pill">
                            <span  @click="deleteFishingEqu(fishingEqu)">{{fishingEqu}}</span>
                        </div>

                    </div>
                    <div class="col-4">
                        <span class="span-text">Address</span>
                        <hr class="solid">
                        <label class="label" for="city">City:</label>
                        <input type="text" id="city" class="form-control" v-model="adventure.city">
                        <label class="label" for="street">Street:</label>
                        <input type="text" id="street" class="form-control" v-model="adventure.street">
                        <label class="label" for="zip-code">Zip code:</label>
                        <input type="text" id="zip-code" class="form-control" v-model="adventure.zipcode">
                        <p>&nbsp;</p>
                        <p>&nbsp;</p>
                        <p>&nbsp;</p>
                        <span class="span-text">Rating</span>
                        <hr class="solid">
                        <span>
                            <div class="inline-inputs">
                                <label class="label" for="rating">Rating:</label>
                                <input type="text" id="rating" class="form-control rating" v-model="adventure.rating" disabled>
                            </div>
                            <div class="inline-inputs">
                                <label class="label" for="num-ratings">No. ratings:</label>
                                <input type="text" id="num-ratings" class="form-control rating" v-model="adventure.noRatings" disabled>
                            </div>
                        </span>
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
                        <button type="button" class="btn btn-success">Add photo</button>
                    </div>
                </div>
            </div>
            <div class="vstack gap-2 col-md-5 mx-auto" id="options-btns">
                <button @click="updateAdventure(adventure)" type="button" class="btn btn-secondary">Save changes</button>
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
import axios from 'axios';
import ErrorPopUp from '../components/ErrorPopUp.vue'
import SuccessPopUp from '../components/SuccessPopUp.vue'
export default {
    name: "AdventureDetail",
    components: {
        ErrorPopUp,
        SuccessPopUp

    },
    props: {
        adventure: Object,
        showDetails: Boolean,
    },
    data(){
        return {

            errMessage : '',
            succMessage : 'Adventure details updated successfully!',
            errorPopUpVisible : false,
            localSuccPopUpVisible : false,
            updatedData : {
                id : '',
                description : '',
                name : '',
                rules : '',
                price : '',
                cancellationConditions : '',
                biography : '',
                fishingEquipment : '',
                city : '',
                street : '',
                zipcode : '',
                capacity :  '',
                rating : '',
                noRatings : '',
                photos : '',


            },

            tempFihingEqu: '',
            newPicture: 'addPhoto.png',

            localPhotos: [],
            localFishingEquipment: []
        }
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
        },

        closePopUp() {
            this.errorPopUpVisible = false;
        },
        closeSuccPopUp(){
            this.$emit('succ-popup-close');
            this.$router.go();
        },

        addFishingEqu(e){
            //uvek je ocpiono prvi parametar event
            //key up je recimo kada se svaki put klikne nesto pojedinacno na tastaturi
            if (e.key === ',' && this.tempService){
                if (!this.localFishingEquipment.includes(this.tempFihingEqu)){
                    this.localFishingEquipment.push(this.tempFihingEqu)    //zbog duplikata
                }
                
                this.tempFihingEqu = ''     //resetuje se trenutni
            }

        },
        deleteFishingEqu(fe){
        //parametar je skill koji se brise
        //uzimamo trenutni item kad filter iterira i ako je to taj brisemo
        this.localFishingEquipment = this.localFishingEquipment.filter((item) => {
            return fe !== item       //ako vratimo true isti su
            //kad se uslov ispuni filtrira sta je stisnuto iz liste
        })
    },

        updateAdventure(adventure){
            

            this.adventure.photos = this.localPhotos;
            this.adventure.fishingEquipment = [];
            let counter = 0
            for (let fe in this.localFishingEquipment){
                counter++
                this.data.fishingEquipment += this.localFishingEquipment[fe]
                if (counter < this.localFishingEquipment.length) this.adventure.fishingEquipment += ','
            }


            axios.put('api/fishingInstructor/' + 'instructor@gmail.com' 
            + '/adventureUpdate/' + adventure.id, adventure).then((response) => {




            }).catch((e)=>{
                console.log(e.response)
                console.log(e.request)
                console.log(e.message)
            })

        },

        addFishingEqu(e) {
            //uvek je ocpiono prvi parametar event
            //key up je recimo kada se svaki put klikne nesto pojedinacno na tastaturi

            if (e.key === ',' && this.tempFihingEqu){
                if (!this.localFishingEquipment.includes(this.tempFihingEqu)){
                    this.localFishingEquipment.push(this.tempFihingEqu)    //zbog duplikata
                }
                
                this.tempFihingEqu = ''     //resetuje se trenutni
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
        newPicAdded(){
            this.newPicture = this.$refs.picInp.value.split("\\")[2]
        },
        deletePic(pic) {
            this.localPhotos = this.localPhotos.filter((item) => {
                return pic !== item       //ako vratimo true isti su
                //kad se uslov ispuni filtrira sta je stisnuto iz liste
            })
        },
        addNewPhoto(){
            this.localPhotos.push(this.newPicture);
        }
    },
    created(){
            try {
                this.localFishingEquipment = this.adventure.fishingEquipment.split(',')
                this.localPhotos = this.adventure.photos
            }catch (err){
                this.localFishingEquipment = this.adventure.fishingEquipment //ako ne uspe split znaci da ima samo jedna
                this.localPhotos = this.adventure.photos
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

    .span-text {
        color : #198754;
    }

    label {
        color : #198754;
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
<template>
    <div v-if="showDetails === true" id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow" class="close">X</button>
            <div class="container">
                <div class="row">
                    <div class="col-4">
                        <span>Cottage</span>
                        <hr class="solid">
                        <label class="label" for="name">Cottage name:</label>
                        <input type="text" id="name" class="form-control" :value="cottage.name">
                        <label class="label" for="description">description:</label>
                        <input type="text" id="description" class="form-control" :value="cottage.description">
                        <label class="label" for="rules">Rules:</label>
                        <input type="text" id="rules" class="form-control" :value="cottage.rules">
                        <span>
                            <div class="inline-inputs">
                                <label class="label" for="price">Price/day:</label>
                                <input type="text" id="price" class="form-control rating" :value="cottage.price">
                            </div>
                            <div class="inline-inputs">
                                <label class="label" for="no-rooms">No. rooms:</label>
                                <input type="text" id="no-rooms" class="form-control rating" :value="cottage.noRooms">
                            </div>
                        </span>
                        <label class="label" for="additionalServices">Additional services(press alt + , to add):</label>
                        <input type="text" class="form-control" v-model="tempService" @keyup.alt="addService">
                        <div v-for="service in this.localServices" :key="service" class="pill">
                            <span  @click="deleteService(service)">{{service}}</span>
                        </div>
                    </div>
                    <div class="col-4">
                        <span>Address</span>
                        <hr class="solid">
                        <label class="label" for="city">City:</label>
                        <input type="text" id="city" class="form-control" :value="cottage.city">
                        <label class="label" for="street">Street:</label>
                        <input type="text" id="street" class="form-control" :value="cottage.street">
                        <label class="label" for="zip-code">Zip code:</label>
                        <input type="text" id="zip-code" class="form-control" :value="cottage.zipCode">
                        <span>Rating</span>
                        <hr class="solid">
                        <span>
                            <div class="inline-inputs">
                                <label class="label" for="rating">Rating:</label>
                                <input type="text" id="rating" class="form-control rating" :value="cottage.averageRating" disabled>
                            </div>
                            <div class="inline-inputs">
                                <label class="label" for="num-ratings">No. ratings:</label>
                                <input type="text" id="num-ratings" class="form-control rating" :value="cottage.noRatings" disabled>
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
                        <div v-for="pic in this.localPhotos" :key="pic.id" class="pillPic">
                            <span  @click="deletePic(pic.id)"><img id="picGallery" :src="require('@/assets/' + pic.photoPath)"/></span>
                        </div>
                        <button type="button" class="btn btn-success">Add photo</button>
                    </div>
                </div>
            </div>
            <div class="vstack gap-2 col-md-5 mx-auto" id="options-btns">
                <button type="button" class="btn btn-secondary">Save changes</button>
                <button type="button" class="btn btn-outline-secondary" @click="closeWindow">Cancel</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "DetailCottageModal",
    components: {

    },
    props: {
        cottage: Object,
        showDetails: Boolean,
    },
    data(){
        return {
            tempService: '',
            newPicture: 'addPhoto.png',

            localPhotos: [],
            localServices: []
        }
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
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
        newPicAdded(){
            this.newPicture = this.$refs.picInp.value.split("\\")[2]
        },
        deletePic(pic) {
            this.localPhotos = this.localPhotos.filter((item) => {
                return pic !== item.id       //ako vratimo true isti su
                //kad se uslov ispuni filtrira sta je stisnuto iz liste
            })
        }
    },
    created(){
            try {
                this.localServices = this.cottage.additionalServices.split(',')
                this.localPhotos = this.cottage.photos
            }catch (err){
                this.localServices = this.cottage.additionalServices //ako ne uspe split znaci da ima samo jedna
                this.localPhotos = this.cottage.photos
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
        height: 140px;
        width: auto;
    }
    
    #imgPreview:hover {
        border-radius: 5px;
        height: 100px;
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
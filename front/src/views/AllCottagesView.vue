<template>
    <div class="container" style="margin-top: 5%">
        <div class="row">
            <div class="col-12 col-md-5 col-lg-4" v-for="cottage in cottages" :key="cottage.name">
                <div class="card" style="width: 18rem; margin-top: 5%" id="card-body-id">
                    <img :src="setPicture(cottage)" id="cottage-img" class="card-img-top" alt="This is cottage picture">
                    <div class="card-body">
                        <h5 class="card-title" id="heading-cottage">{{cottage.name}}</h5>
                        <p class="card-text"><b>Location:</b> {{cottage.city}}, {{cottage.street}}</p>
                        <p class="card-text"><b>Description:</b>{{cottage.description}}</p>
                        <p class="card-text"><b>Rate:</b> {{cottage.averageRating}}</p>
                        <span>
                            <button class="btn btn-success" @click="showDetailCottageModal(cottage)">Details</button>
                            <button class="btn btn-danger" @click="showConfirmDeletionDialog(cottage)">Delete</button>
                        </span>    
                    </div>
                </div>
            </div>
        </div>
        <div>
            <button class="btn btn-success" id="btn-add" @click="showAddCottageModal()">Add cottage</button>
        </div>
    </div>
    <div v-if="showDetails">
        <DetailCottageModal
        :cottage = "sendCottage"
        :showDetails = "showDetails"
        @modal-closed = "showDetails = false"
        @succ-popup-close = "succPopUpClose"
        />
    </div>
    <div v-else-if="showAddNewCottage">
        <AddCottageModal
        :showAddNewCottage = "showAddNewCottage"
        :succPopUpVisible = "succPopUpVisible"
        @modal-closed = "showAddNewCottage = false"
        @succ-popup-close = "succPopUpClose"
        />
    </div>
    <div v-else-if="confirmationPopUpVisible">
        <ConfirmationPopUp
        :succPopUpVisible = "succPopUpVisible"
        :title="deletionTitle"
        :message="deletionMessage"
        @modal-closed = "confirmationPopUpVisible = false"
        @succ-popup-close = "succPopUpVisible = false"
        @confirmed-event = "confirmDeletion"
        />
    </div>

    
</template>

<script>
 import axios from 'axios';
 import DetailCottageModal from '../components/DetailCottageModal.vue'
 import AddCottageModal from '../components/AddCottageModal.vue'
 import ConfirmationPopUp from '../components/ConfirmationPopUp.vue'

export default {
   name: "AllCottagesView",
   components: {
       DetailCottageModal, AddCottageModal, ConfirmationPopUp
   },
   data (){
       return {
           cottages: [],
           sendCottage: {},
           cottageToDelete: {},

           showDetails: false,
           showAddNewCottage: false,
           showDeleteCottage: false,
           succPopUpVisible: false,
           confirmationPopUpVisible: false,

           deletionMessage: "Are you sure about deleting this cottage?",
           deletionTitle: "Cottage deleting"
       }
   }, 
   methods: {
        setPicture(cottage) {
                try{
                    return require('../assets/' + cottage.photos[0]);
                } catch(e) {}
        },
        showDetailCottageModal(cottage) {
            this.sendCottage = cottage
            this.showDetails = true
        },
        showAddCottageModal() {
            this.showAddNewCottage = true
        },
        succPopUpClose() {
            this.succPopUpVisible = false;
        },
        showConfirmDeletionDialog(cottage) {
            this.cottageToDelete = cottage;
            this.confirmationPopUpVisible = true;
        },
        confirmDeletion(){
            axios.delete('api/cottage-owner/' + 'srdjan@gmail.com' + '/delete-cottage/' + this.cottageToDelete.id).then((response) => {
                
            }).catch(function (error) {
                console.log(error);
                alert(error)
            });
            this.cottages = this.cottages.filter((item) => {
                return this.cottageToDelete !== item       //ako vratimo true isti su
                //kad se uslov ispuni filtrira sta je stisnuto iz liste
            })
            this.cottageToDelete = {}   //reset vreednosti
        }
   },
   created(){
       axios.get('api/cottage-owner/all-cottages/' + "srdjan@gmail.com").then((response) => {
           this.cottages = response.data
        }).catch((error) => {
            console.log('Error happened: ' + error.name);
    })

   }

}
</script>

<style scoped>
    
    #cottage-img {
        margin-top: 10px !important;
        height: 150px;
        width: auto;
        display: block;
        margin: 0 auto;
        border-radius: 10%;
    }

    #cottage-img:hover{
        width: 230px;
        height: 170px;
        border-radius: 20px;
        background: linear-gradient(rgb(255, 253, 253), rgb(241, 239, 239));
    }

    b{
        color: rgba(51, 92, 80, 0.8);
    }

    span button {
        margin: 10px 10px;
    }

    div h5#heading-cottage {
        color: rgba(51, 92, 80, 0.8);
        font-weight: bold;
    }

    div div#card-body-id {
        background-color: rgba(214, 218, 216, 0.7);
    }

    #btn-add{
        margin: 30px;
        float: left;
    }

    #btn-add:hover{
        width: 140px;
        height: auto;
        border-radius: 10px;
    }

</style>
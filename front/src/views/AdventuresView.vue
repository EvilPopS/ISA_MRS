<template>
    <div class="container" style="margin-top: 5%">
        <div class="row">
            <div class="col-12 col-md-5 col-lg-4" v-for="adventure in adventures" :key="adventure.name">
                <div class="card" style="width: 18rem; margin-top: 5%" id="card-body-id">
                    <img :src="setPicture(adventure)" id="adventure-img" class="card-img-top" alt="This is adventure picture">
                    <div class="card-body">
                        <h5 class="card-title" id="heading-adventure">{{adventure.name}}</h5>
                        <p class="card-text"><b>Location:</b> {{adventure.city}}, {{adventure.street}}</p>
                        <p class="card-text"><b>Description:</b>{{adventure.description}}</p>
                        <p class="card-text"><b>Rate:</b> {{adventure.averageRating}}</p>
                        <span>
                            <button class="btn btn-success" @click="showAdventureDetail(adventure)">Details</button>
                            <button @click="deleteAdventure(adventure)" class="btn btn-danger">Delete</button>
                        </span>    
                    </div>
                </div>
            </div>
        </div>
        <div>
            <button class="btn btn-success" id="btn-add" @click="showAddAdventure()">Add adventure</button>
        </div>
    </div>
    <div v-if="showDetails">
        <AdventureDetail
        :adventure = "sendadventure"
        :showDetails = "showDetails"
        @modal-closed = "showDetails = false"
        />
    </div>
    <div v-else-if="showAddNewАdventure">
        <AddAdventure
        :showAddNewAdventure = "showAddNewАdventure"
        :succPopUpVisible = "succPopUpVisible"
        @modal-closed = "showAddNewАdventure = false"
        @succ-popup-close = "succPopUpClose"
        />
    </div>

    
</template>

<script>
 import axios from 'axios';
 import AdventureDetail from '../components/AdventureDetail.vue'
 import AddAdventure from '../components/AddAdventure.vue'

export default {
   name: "AdventuresView",
   components: {
     AdventureDetail, AddAdventure
   },
   data (){
       return {
           adventures: [],
           sendadventure: {},

           showDetails: false,
           showAddNewАdventure: false,
           showDeleteadventure: false,
           succPopUpVisible: false
       }
   }, 
   methods: {
        setPicture(adventure) {
                try{
                    return require('../assets/' + adventure.photos[0]);
                } catch(e) {}
        },
        showAdventureDetail(adventure) {
            this.showDetails = true
            this.sendadventure = adventure
        },
        showAddAdventure() {
            this.showAddNewАdventure = true
        },
        succPopUpClose() {
            this.succPopUpVisible = false;
        },
        deleteAdventure(adventure){
            axios.delete('api/fishingInstructor/' + 'instructor@gmail.com' + '/deleteAdventure/' + adventure.id).then((response) => {
                
            })
            this.adventures = this.adventures.filter(item => item != adventure);

        }
   },
   created(){
       axios.get('api/fishingInstructor/' + 'instructor@gmail.com' + '/adventures' ).then((response) => {
           this.adventures = response.data
        }).catch((error) => {
            console.log('Error happened: ' + error.name);
    })

   }
}


</script>

<style scoped>
    
    #adventure-img {
        margin-top: 10px !important;
        height: 150px;
        width: auto;
        display: block;
        margin: 0 auto;
        border-radius: 10%;
    }

    #adventure-img:hover{
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

    div h5#heading-adventure {
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
        width: 100px;
        height: auto;
        border-radius: 10px;
    }
</style>
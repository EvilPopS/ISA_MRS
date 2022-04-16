<template>
    <div class="card-container" v-for="cottage in cottages" :key="cottage.name">
        <div class="card" style="width: 18rem;">
            <img :src="setPicture(cottage)" class="card-img-top" alt="This is cottage picture">
            <div class="card-body">
                <h5 class="card-title">{{cottage.name}}</h5>
                <p class="card-text">{{cottage.address}}</p>
                <p class="card-text">Rate: {{cottage.rating}}</p>
                <span>
                    <a href="#" class="btn btn-primary">Details</a>
                    <a href="#" class="btn btn-primary">Delete</a>
                </span>    
            </div>
        </div>
    </div>
</template>

<script>
 import axios from 'axios';

export default {
   name: "AllCottagesView",
   components: {

   },
   data (){
       return {
           cottages: [],

           showDetails: false,
           showAddNewCottage: false,
           showDeleteCottage: false
       }
   }, 
   methods: {
       setPicture(cottage) {
                try{
                    return require('../assets/' + cottage.photos[0]);
                } catch(e) {}
        },
   },
   created(){
       axios.get('api/cottage-owner/all-cottages' + "srdjan@gmail.com").then((response) => {
           this.cottages = response.data
        }).catch((error) => {
            console.log('Error happened: ' + error.name);
    })

   }

}
</script>

<style scoped>

    .card-container {
        display: inline;
    }

</style>
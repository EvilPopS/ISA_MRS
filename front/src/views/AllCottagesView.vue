<template>
    <div class="container" style="margin-top: 5%">
        <div class="row">
            <div class="col-12 col-md-5 col-lg-4" v-for="cottage in cottages" :key="cottage.name">
                <div class="card" style="width: 18rem;" id="card-body-id">
                    <img :src="setPicture(cottage)" id="cottage-img" class="card-img-top" alt="This is cottage picture">
                    <div class="card-body">
                        <h5 class="card-title" id="heading-cottage">{{cottage.name}}</h5>
                        <p class="card-text">Location: {{cottage.city}}, {{cottage.street}}</p>
                        <p class="card-text">{{cottage.description}}</p>
                        <p class="card-text">Rate: {{cottage.averageRating}}</p>
                        <span>
                            <a href="#" class="btn btn-success">Details</a>
                            <a href="#" class="btn btn-danger">Delete</a>
                        </span>    
                    </div>
                </div>
            </div>
        </div>
        <div>
            <button class="btn btn-success" id="btn-add" >Add cottage</button>
        </div>
    </div>
</template>

<script>
 import axios from 'axios';
 import StarRating from 'vue3-star-ratings'

export default {
   name: "AllCottagesView",
   components: {
       StarRating
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
                    return require('../assets/' + cottage.photos[0].photoPath);
                    alert(cottage.photos[0])
                } catch(e) {}
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

    span a {
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
        width: 100px;
        height: auto;
        border-radius: 10px;
    }
   

</style>
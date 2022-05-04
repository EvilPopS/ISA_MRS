<template>
        <div class="container" style="margin-top: 5%">
            <div class="row" v-for="request in requests" :key="request.sender"> 
                <div v-if="!request.isAnswered" class="card mb-3" style="max-width: 740px;">
                    <div class="row no-gutters">
                        <div class="col-md-4">
                            <img :src="setPicture(request)" style="height : 100px;" alt="...">
                        </div>
                    <div class="col-md-8">
                        <div class="card-body">
                            <h5 class="card-title badge bg-success text-wrap rounded-pill status">Request type: {{request.requestType}}</h5>
                            <p class="card-text"> Request message: {{request.message}}</p>
                            <p class="card-text"><small class="text-muted">{{request.sendTime}}</small></p>
                            <span>
                                <button class="btn btn-success" @click="allowRequest(request)">Allow</button>
                                <button @click="rejectRequest(request)" class="btn btn-danger">Reject</button>
                            </span> 
   
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    name : 'AdminNotifications',
    data () {
        return {
            requests : [],

        }
    },

    methods : {
            setPicture(request) {
                try{
                    return require('../assets/' + 'logo.png'); // ovde promeniti da ucita sliku koju stvarno treba da ucita
                } catch(e) {}
        },

        allowRequest(request){
            console.log("Allowing request...");
            // obrisi sendera i stavi isAnswered na true
            axios.delete('api/admin/deleteUser/' + request.senderId + '/allow').then((response) => {

            })
            this.requests = this.requests.filter(item => item != request);
        },
        rejectRequest(request){
            axios.delete('api/admin/deleteUser/' + request.senderId + '/reject').then((response) => {

            })
            this.requests = this.requests.filter(item => item != request);
        }

    },

    created () {

        axios.get('api/admin/requests').then((response) => {
            this.requests = response.data;
        })

    }
}

</script>




<style scoped>
    
    #notification-img {
        margin-top: 10px !important;
        height: 100px;
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
        border-radius: 10px;
        background: rgb(25, 117, 7);
    }
</style>
<template>
        <div class="container" style="margin-top: 5%">
            <div class="row" v-for="request in data" :key="request.sender"> 
                <div v-if="!request.isAnswered" class="card mb-3" style="max-width: 740px;">
                    <div class="row no-gutters">
                        <div class="col-md-4">
                            <img :src="setPicture(request)" style="height : 100px;" alt="...">
                            <p>{{request.senderName}}</p>
                        </div>
                    <div class="col-md-8">
                        <div class="card-body">
                            <h5 class="card-title badge bg-success text-wrap rounded-pill status">Request type: {{request.requestType}}</h5>
                            <p class="card-text"> Request message: {{request.message}}</p>
                            <p class="card-text"><small class="text-muted">{{request.sendTime}}</small></p>
                            <span>
                                <button class="btn btn-success" @click="$emit('show-confirm-allowing-dialog', request)">Allow</button>
                                <button class="btn btn-danger"  @click="$emit('show-confirm-rejection-dialog',request)">Reject</button>
                            </span> 
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </div>
</template>


<script>

export default {
    name : 'DeletionRegistrationNotifications',

    props : {data : Array,},


    methods : {
                    setPicture(request) {
                try{
                    return require('../assets/' + request.senderProfilePhoto); // ovde promeniti da ucita sliku koju stvarno treba da ucita
                } catch(e) {}
        },
    }

}
</script>



<style scoped>
    


    #tabNav {
    margin: 50px 0px 30px 0px;
    }

    a {
    color: rgba(15, 95, 72, 0.95);
    }

    li {
    background-color: rgba(214, 218, 216, 0.7);
    }

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

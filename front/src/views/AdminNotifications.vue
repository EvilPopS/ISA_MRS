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
                                <button class="btn btn-success" @click="showConfirmAllowingDialog(request)">Allow</button>
                                <button class="btn btn-danger"  @click="showConfirmRejectionDialog(request)">Reject</button>
                            </span> 
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div v-if="confirmationPopUpVisible">
        <ConfirmationPopUp
        :title="rejectionTitle"
        :message="rejectionMessage"
        @modal-closed = "confirmationPopUpVisible = false"
        @confirmed-event = "rejectRequest"
        />
    </div>
    <SuccessPopUp v-show="succPopUpVisible"
        @close = "succPopUpVisible = false"
        :mess = succMessage
    />
    <ErrorPopUp v-show="errorPopUpVisible" 
        @close = closeErrorPopUp
        :mess = errMessage
    /> 

    </div>
    
        
</template>

<script>
import axios from 'axios'
import SuccessPopUp from '../components/SuccessPopUp.vue'
import ConfirmationPopUp from '../components/ConfirmationPopUp.vue'
import ErrorPopUp from '../components/ErrorPopUp.vue'
export default {
    name : 'AdminNotifications',
    components : {
        SuccessPopUp,
        ConfirmationPopUp,
        ErrorPopUp
    },
    data () {
        return {
            requests : [],
            confirmationPopUpVisible : false,
            succPopUpVisible : false,
            errorPopUpVisible : false,

            rejectionMessage : '',
            rejectionTitle : '',
            succMessage : '',
            errMessage : '',

            requestToBeAllowed : Object,
            requestToBeRejected : Object

        }
    },

    methods : {
            setPicture(request) {
                try{
                    return require('../assets/' + 'logo.png'); // ovde promeniti da ucita sliku koju stvarno treba da ucita
                } catch(e) {}
        },

        closeErrorPopUP()  {
            this.errorPopUpVisible = true;
        },

        showConfirmAllowingDialog(request){
            this.requestToBeAllowed = request;
            this.allowRequest();

        },
        showConfirmRejectionDialog(request){
            this.requestToBeRejected = request
            this.rejectionTitle = 'Are you sure?';
            this.rejectionMessage = 'Request is going to be rejected.'
            this.confirmationPopUpVisible = true;

        },


        allowRequest(){
            console.log("Allowing request...");
            this.confirmationPopUpVisible = false;
            // obrisi sendera i stavi isAnswered na true
            axios.delete('api/admin/deleteUser/' + this.requestToBeAllowed.senderId + '/allow').then((response) => {
                this.requests = this.requests.filter(item => item != this.requestToBeAllowed);
                this.succPopUpVisible = true;
                this.succMessage = 'Request is successfully allowed.';
                this.requests.push();
            }).catch((e) => {
                this.errMessage = e ;
                this.errorPopUpVisible = true;
            })
            
        },
        rejectRequest(){
            this.confirmationPopUpVisible = false;
            axios.delete('api/admin/deleteUser/' + this.requestToBeRejected.senderId + '/reject').then((response) => {
                this.requests = this.requests.filter(item => item != this.requestToBeRejected);
                this.succPopUpVisible = true;
                this.succMessage = 'Request is successfully rejected.';
                this.requests.push();
            }).catch((e) => {
                this.errMessage = e;
                this.errorPopUpVisible = true;

            })
            
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
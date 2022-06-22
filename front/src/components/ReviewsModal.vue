<template>
  <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow" class="close">X</button>
            <div class="container">
                <div v-if="data.length > 0">
                     <div v-for="res in data" :key="res.message" class="row green-row">
                        <p>Grade: {{res.grade}}/5</p>
                        <p>Client: {{res.clientFullName}}</p>
                        <p>Message: {{res.message}}</p>
                        <p>Time: {{res.sentTime.split('T')[0]}} at {{(res.sentTime.split('T')[1]).split(':')[0]}}:{{(res.sentTime.split('T')[1]).split(':')[1]}}</p>
                    </div>
                </div>
                <div v-else>
                    <h2>There is no reviews for choosen rental.</h2>
                </div>
            </div>
        </div>
        <ErrorPopUp v-show="errorPopUpVisible" 
        @close = closePopUp
        :mess = errMessage
        /> 
  </div>
</template>

<script>
import axios from 'axios';
import ErrorPopUp from "./ErrorPopUp.vue"

export default {
    name: "ReviewsModal",
    components: {
        ErrorPopUp
    },
    props: {
        choosenRental: Object
    },
    data() {
        return {
            errMessage: '',
            errorPopUpVisible: false,
            data: []
        }
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
        },
        closePopUp() {
            this.errorPopUpVisible = false;
        }
    },
    mounted() {
        axios.get('api/notification/get-reviews/' + this.choosenRental.id, {headers: {'authorization': window.localStorage.getItem("token") }}).then((response) => {
                this.data = response.data
                if (this.data.length > 0){
                        this.data.sort(function(left, right) { 
                        let lDate = new Date(left.sentTime).toISOString();
                        let rDate = new Date(right.sentTime).toISOString();
                        if (lDate < rDate) 
                            return -1;
                        else if (lDate > rDate)
                            return 1;

                        return 0;
                    });
                }
            }).catch(err => {
                    if (err.response.status === 404){
                        this.errMessage = "Client or owner with given email address is not found!";
                        this.errorPopUpVisible = true;
                    } 
                    else if (err.response.status === 401) {
                        this.errMessage = "You are not authorized!";
                        this.errorPopUpVisible = true;
                    }
                    else if (err.response.status === 422) {
                        this.errMessage = "Error! Wrong data!";
                        this.errorPopUpVisible = true;
                    } else {
                        this.errMessage = "Error! Wrong data!";
                        this.errorPopUpVisible = true;
                    }
                });
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
    width: 50%;
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

    h2 {
        margin-top: 5%;
        margin-bottom: 5%;
        color: rgba(51, 92, 80, 0.8);
    }

    .green-row {
        border-radius: 20px;
        background-color: rgb(146, 179, 146);
        font-size: 20px;
        margin: 5%;
    }

</style>
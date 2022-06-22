<template>
  <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow" class="close">X</button>
            <div class="container">
                <h2>User: {{data.name}} {{data.surname}}</h2>
                <div id="pictureDiv">
                    <img :src="setPicture(data.profilePicture)" />
                </div>
                <div class="badge bg-success text-wrap rounded-pill status" style="width: 6rem;">
                    {{data.loyaltyStatus}}
                </div>
                <div id="userInfoDiv">
                    <p><b>Email: </b>{{ data.email }}</p>
                    <p><b>Name: </b>{{ data.name }}</p>
                    <p><b>Surname: </b>{{ data.surname }}</p>
                    <p>
                        <b>Address: </b>{{ data.country }}, {{ data.city }}
                    </p>
                    <p><b>Price: </b>{{ reservation.price }} &euro;</p>
                    <p><b>Action: </b>{{reservation.action ? 'Yes' : 'No'}}</p>
                    <div class="start-data" v-if="showActionServ()">
                        <h4>Action services:</h4>
                            <div v-for="service in reservation.actionServices" :key="service" class="pill">
                                <span >{{service}}</span>
                            </div>
                        </div>
                    </div>
                <div class="vstack gap-2 col-md-5 mx-auto" id="options-btns">
                    <button type="button" class="btn btn-success" @click="closeWindow">Close</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'

export default {
    name: "BasicClientProfile",
    components: {

    },
    props: {
        clientEmail: String,
        reservation: Object
    },
    data(){
        return {
            data: {}
        }
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
        },
        setPicture(picture) {
                try{
                    return require('../assets/' + picture);
                } catch(e) {console.log(e)}
        },
        showActionServ() {
            if (!this.reservation.action)            
                return false

            try {
                this.reservation.actionServices = this.reservation.actionServices.split(',')
            } catch (err){
                this.reservation.actionServices = this.reservation.actionServices //ako ne uspe split znaci da ima samo jedna
            }

            return true
        }
    },
    mounted() {
        axios.get('api/client/basic-profile/' + this.clientEmail, {headers: {'authorization': window.localStorage.getItem("token") }}).then((response) => {
                this.data = response.data
            }).catch(err => {
                    if (err.response.status === 404){
                        this.errMsg = "Client or owner with given email address is not found!";
                        this.errorPoup = true;
                    } 
                    else if (err.response.status === 401) {
                        this.errMsg = "You are not authorized!";
                        this.errorPoup = true;
                    }
                    else if (err.response.status === 422) {
                        this.errMsg = "Error! Wrong data!";
                        this.errorPoup = true;
                    } else {
                        this.errMsg = "Error! Wrong data!";
                        this.errorPoup = true;
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
    width: 40%;
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
        color: rgba(51, 92, 80, 0.8);
    }

    b {
        color: rgba(51, 92, 80, 0.8);
    }

    img {
        max-width: 300px;
        height: auto;
        border-radius: 25%;
        align-content: center;
        margin: 3% 3%;
    }

    #userInfoDiv {
        text-align: left;
    }

    div.status{
        align-content: center;
        text-align: center;
        margin-bottom: 3%;
    }

    .start-data {
        margin-top: 5%;
        margin-bottom: 5%;
        border-radius: 15px;
        background-color: rgb(146, 179, 146);
        padding: 3%;
    }

    div .pill {
        display: inline-block;
        margin: 20px 10px 0 0;
        padding: 6px 12px;
        background: white;
        border-radius: 20px;
        font-size: 12px;
        letter-spacing: 1px;
        font-weight: bold;
        color: black;
        cursor: pointer;
    }

</style>
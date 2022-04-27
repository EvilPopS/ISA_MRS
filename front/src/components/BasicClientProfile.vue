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
                        <b>Address: </b>{{ data.city }}, {{data.zipcode}}
                    </p>
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
        clientEmail: String
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
        }
    },
    mounted() {
        axios.get('api/client/basic-profile/' + this.clientEmail).then((response) => {
                this.data = response.data
            }).catch((error) => {
                console.log('Error happened: ' + error.name)
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

</style>
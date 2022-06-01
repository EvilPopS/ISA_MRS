<template>
    <div class="main-cont">
        <div class="row">
            <div class="col">
                <label>Name:</label>
                <input type="text" class= "form-control" v-model="ownerInfo.name" disabled>
                <label>Surname:</label>
                <input type="text" class= "form-control" v-model="ownerInfo.surname" disabled>
                <label>Contact number:</label>
                <input type="text" class= "form-control" v-model="ownerInfo.phoneNum" disabled>
            </div>
            <div class="col">
                <div class="d-flex justify-content-center">
                    <img id="accPic" :src="setOwnerProfilePic(ownerInfo.profilePic)" >
                </div>
            </div>
            <div class="col">
                <label>Country:</label>
                <input type="text" class= "form-control" v-model="ownerInfo.country" disabled>
                <label>City:</label>
                <input type="text" class= "form-control" v-model="ownerInfo.city" disabled>
                <label>Street:</label>
                <input type="text" class= "form-control" v-model="ownerInfo.street" disabled>
            </div>
        </div>

        <div class="d-flex justify-content-center">
                <button v-if="showSubBtn" id="subsc-btn" class="button-style" @click="subscribe()">Subscribe</button>
                <button v-else id="unsub-btn" class="button-style" @click="unsubscribe()">Unsubscribe</button>
        </div>
        <div class="d-flex justify-content-center">
                <button id="back-btn" class="button-style" @click="$emit('close')">Back</button>
        </div>
    </div>
</template>

<script>
    import axios from 'axios';

    export default {
        name : "RentalOwnerProfileView",
        data() {
            return {
                showSubBtn: true
            }
        },
        props: {
            ownerInfo: Object
        },
        methods: {
            setOwnerProfilePic(ownerProfilePic) {
                try{
                    return require('@/assets/' + ownerProfilePic);
                } catch(e) {}
            }
        },
        created() {
            axios.get("api/client/check-if-subscribed/" + this.ownerInfo.id, {headers: {'authorization': window.localStorage.getItem("token") }})
                .then(() => {
                    this.showSubBtn = true;
                }).catch((error) => {
                    if (error.response.status == "404")
                        this.showSubBtn = false;
                });
        }
    }
</script>

<style scoped>
    .main-cont {
        border-right: 1px solid black;
        border-left: 1px solid black;
        border-radius: 30px;
        padding: 20px 60px;
        text-align: left;
        height: 70vh;
        margin: 9vh 5vw;
        background: linear-gradient(rgb(252, 250, 250), rgba(224, 220, 220, 0.5))
    }

    #accPic {
        margin-top: 30px;
        border-radius: 30px;
    }

    .button-style {
        width: 150px;
        height: 40px;
        border-radius: 25px;
        font-weight: bold;
        color: black;
    }

    .button-style:hover {
        width: 160px;
    }

    #subsc-btn {
        margin-top: 70px;
        background: rgb(40, 187, 11)
    }

    #unsub-btn {
        margin-top: 70px;
        background: rgb(231, 22, 22)
    }

    #back-btn {
        margin-top: 20px;
        background-color: rgb(58, 63, 57);
    }
</style>

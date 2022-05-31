<template>
    <div id="subs-cont">

        <div class="card-style d-flex align-items-center" v-for="sub in this.subscriptions" :key="sub.id">
            <div class="col-2">
                <img class="acc-img" src="@/assets/logo.png">
            </div>
            <div class="col-3 text-style">
                <p>{{sub.name}}</p>
                <p>{{sub.surname}}</p>
            </div>
            <div class="col-5 text-style">
                <label>Phone num: </label>
                <span> {{sub.phoneNum}}</span>

                <div>
                    <img v-if="sub.rentType == 'ADVENTURE'" src="@/assets/adventure_icon.png" class="rental-icon">
                    <img v-else-if="sub.rentType == 'BOAT'" src="@/assets/boat_icon.png" class="rental-icon">
                    <img v-else-if="sub.rentType == 'COTTAGE'" src="@/assets/cottage_icon.png" class="rental-icon">
                </div>
            </div>
            <div class="col-2">
                <button class="unsub-btn">Unsubscribe</button>
            </div>  
        </div> 
              
    </div>
</template>

<script>
    import axios from 'axios';

    export default {
        name: "ClientSubscriptionsView",
        data() {
            return {
                subscriptions: []
            }
        },
        methods: {},
        created() {
            axios.get("api/client/subscriptions", {headers: {'authorization': window.localStorage.getItem("token") }})
                .then((response) => {
                    this.subscriptions = response.data;
                }
            );
        },

    }
</script>

<style scoped>
    #subs-cont {
        margin-top: 70px;
    }

    .card-style {
        width: 750px;
        height: 120px;
        border: 1px solid black;
        border-radius: 30px;
        padding: 10px;
        margin: 20px auto;
    }

    .card-style:hover {
        width: 770px;
        background: linear-gradient(rgba(150, 144, 144, 0.3), 
                                    rgba(255, 255, 255, 0.3), 
                                    rgba(112, 110, 110, 0.3));
    }

    .acc-img {
        border-radius: 30px;
        height: 100px;
        width: 100px;
        left: 0;
    }
    
    .text-style p {
        margin-left: 3px;
        font-family: "Palatino Linotype", "Book Antiqua", Palatino, serif;
        font-size: 22px;
        letter-spacing: 2px;
        word-spacing: 2px;
        color: #000000;
        font-weight: 700;
        text-decoration: none;
        font-style: normal;
        font-variant: small-caps;
        text-transform: none;
    }
    
    .text-style span {
        margin-top: 0px;
        font-family: "Palatino Linotype", "Book Antiqua", Palatino, serif;
        font-size: 27px;
        letter-spacing: 2px;
        word-spacing: 2px;
        color: #000000;
        font-weight: 700;
        text-decoration: none;
        font-style: normal;
        font-variant: small-caps;
        text-transform: none;
    }

    label {
        margin-top: 0px;
        margin-bottom: 5px;
    }

    .unsub-btn {
        background-color: rgb(230, 46, 46);
        font-weight: bold;
        border-radius: 20px;
        margin: 17px 10px;
    }

    .unsub-btn:hover {
        background-color: rgb(235, 112, 112);
        color: white;
    }

    .rental-icon {
        height: 40px;
        widows: 40px;
        margin-bottom: 15px;
        margin-top: 10px;
    }
</style>
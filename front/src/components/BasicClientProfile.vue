<template>
  <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow" class="close">X</button>
            <div class="container">
                <div class="row">
                    <div class="col-6">
                        <h3>User</h3>
                        <label class="label" for="name">Cottage name:</label>
                        <input type="text" id="name" class="form-control" v-model="data.name">
                        <label class="label" for="description">description:</label>
                        <input type="text" id="description" class="form-control" v-model="data.description">
                        <label class="label" for="rules">Rules:</label>
                        <input type="text" id="rules" class="form-control" v-model="data.rules">
                        <span>
                            <div class="inline-inputs">
                                <label class="label" for="price">Price/day:</label>
                                <input type="text" id="price" class="form-control rating" v-model="data.price">
                            </div>
                            <div class="inline-inputs">
                                <label class="label" for="noRooms">No. rooms:</label>
                                <input type="text" id="noRooms" class="form-control rating" v-model="data.noRooms">
                            </div>
                        </span>
                        <label class="label" for="additionalServices">Additional services(press alt + , to add):</label>
                        <input type="text" class="form-control" v-model="tempService" @keyup.alt="addService">
                        <div v-for="service in localServices" :key="service" class="pill">
                            <span  @click="deleteService(service)">{{service}}</span>
                        </div>
                    </div>
                    <div class="col-6">
                        <label class="label" for="city">City:</label>
                        <input type="text" id="city" class="form-control" v-model="data.city">
                        <label class="label" for="street">Street:</label>
                        <input type="text" id="street" class="form-control" v-model="data.street">
                        <label class="label" for="zip-code">Zip code:</label>
                        <input type="text" id="zip-code" class="form-control" v-model="data.zipCode">
                        <label class="label" for="capacity">Capacity:</label>
                        <input type="text" id="capacity" class="form-control" v-model="data.capacity">
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
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
        }
    },
    mounted() {
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
    width: 80%;
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

</style>
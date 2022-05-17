<template>
    <div class="popup-overlay" @click="emitClose()">
        <div class="container popup" @click.stop>
            <div class="row modal-style">
                <div class="col col-style">
                    <div id="info-holder">
                        <div v-if="showCottageIcon" class="d-flex justify-content-center">
                            <img class="icon-style" src="@/assets/cottage_icon.png" alt="">
                        </div>
                        <div v-else-if="showBoatIcon" class="d-flex justify-content-center">
                            <img class="icon-style" src="@/assets/boat_icon.png" alt="">
                        </div>
                        
                        <div v-else-if="showAdventureIcon" class="d-flex justify-content-center"> 
                            <img class="icon-style" src="@/assets/adventure_icon.png" alt="">
                        </div>
                        
                        <div class="row">
                            <div class="col-md-8">
                                <label>Name:</label>
                                <input type="text" v-model="name" disabled>
                            </div>
                            <div class="col-md-2">
                                <label>Price:</label>
                                <input type="text" v-model="price" disabled>
                            </div>
                            <div class="col-md-2">
                                <label>Rating:</label>
                                <input type="text" v-model="rate" disabled>
                            </div>
                        </div>

                        <label>Address:</label>
                        <input type="text" v-model="address" disabled>

                        <label>Description:</label>
                        <textarea id="desc-style" v-model="description" disabled></textarea>
                    </div>
                </div>

                <div class="col">
                    <div class="images-cont">
                        <label>Images:</label>
                        <div class="row justify-content-center">
                            <img v-for="pic in images" :key="pic" class="entity-img-style" :src="setProfPic(pic)" alt="">
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
        name: "RentalViewModal",
        props: {
            id: Number,
            type: String
        },
        data() {
            return {
                name: "",
                description: "",
                price: "",
                rate: "",
                address: "",
                images: [],

                showCottageIcon: false,
                showBoatIcon: false,
                showAdventureIcon: false
            }
        },
        created() {
            axios.get("api/rental/basic/" + this.id +"?type=" + this.type).then((response) => {
                if (this.type === "Cottage")
                    this.showCottageIcon = true;
                else if (this.type === "Adventure")
                    this.showAdventureIcon = true;
                else if (this.type === "Boat")
                    this.showBoatIcon = true;

                let rental = response.data;
                
                this.name = rental.name;
                this.description = rental.description;
                this.price = rental.price + "€/day";
                this.rate = rental.rate + " ★";
                this.address = rental.address.placeName + " " + rental.address.street;
                this.images = rental.photos;
            });
        },
        methods: {
            emitClose() {
                this.showCottageIcon = false;
                this.showBoatIcon = false;
                this.showAdventureIcon = false;
                this.$emit('close');
            },
            setProfPic(imagePath) {
                try{
                    return require('@/assets/' + imagePath);
                } catch(e) {}
            }
        }
    }
</script>

<style scoped>
    .popup-overlay {
        display: block;
        position: fixed;
        z-index: 999;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background-color: #000000da;
        overflow: auto;
        width: 100%; 
        height: 100%; 
        padding-top: 25px; 
    }

    .popup {
        background: white;
        border: 2px solid black;
        border-radius: 20px;
        margin: auto;
        width: 80%;
        min-height: 90vh;
    }

    div label {
        font-size: 15px;
    }

    .modal-style{
        padding: 20px;
    }

    .icon-style {
        width: 40px;
        height: 40px;
        margin-left: auto;
        margin-right: auto;
        margin-bottom: 10px;
    }

    #info-holder {
        text-align: left;
    }

    #desc-style {
        display: block;
        padding: 10px 6px;
        width: 100%;
        box-sizing: border-box;
        border: none;
        border-bottom: 1px solid #ddd;
        color: #555;
        max-height: 400px;
        min-height: 50px;
    }

    .images-cont {
        margin-top: 48px;
        padding-left: 20px;
        padding-right: 20px;
        min-height: 90%;
    }

    .entity-img-style:hover {
        background-color: rgba(26, 25, 25, 0.1);
        height: 210px;
        width: 210px;
        margin-left: 25px;
        margin-right: 15px;
        margin-bottom: 10px;
    }

    .entity-img-style {
        height: 200px;
        width: 200px;
        border-radius: 20px;
        margin-left: 30px;
        margin-right: 20px;
        margin-bottom: 20px;
    }

</style>
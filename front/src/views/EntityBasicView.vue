<template>
    <div class="split left">
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

    <div class="split right">
        <div class="images-cont">
            <label>Images:</label>
            <div class="row justify-content-center">
                <img v-for="pic in images" :key="pic" class="entity-img-style" :src="setProfPic(pic)" alt="">
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "EntityBasicView",
        data() {
            return {
                name: "",
                description: "",
                price: "",
                rate: "",
                address: "",
                images: [],

                showCottageIcon: true,
                showBoatIcon: false,
                showAdventureIcon: false
            }
        },
        created() {
            let id = window.sessionStorage.getItem("entityDataId");
            let type = window.sessionStorage.getItem("entityDataType");
            axios.get("api/rental/basic/" + id +"?type=" + type).then((response) => {
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
            setProfPic(imagePath) {
                try{
                    return require('@/assets/' + imagePath);
                } catch(e) {}
            }
        }
    }
</script>

<style scoped>
    div label {
        font-size: 15px;
    }

    .split {
        height: 100%;
        position: fixed;
        z-index: 1;
        top: 0;
        overflow-x: hidden;
    }

    .left {
        width: 50%;
        margin-top: 32px;
        padding-top: 40px;
        padding-left: 100px;
        left: 0;
    }

    .right {
        width: 50%;
        right: 0;
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
        margin-top: 72px;
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

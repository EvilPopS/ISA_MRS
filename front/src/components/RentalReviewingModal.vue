<template>
    <div class="row modal-cont">
        <div id="left-part" class="col-sm-5">
            <label>Rate your experience:</label>
            <StarRating 
                :border-width="5" 
                :increment="0.01"
                :star-size="30"
                :animate="true"
                v-model:rating="rating">
            </StarRating>

            <label>Tell us what you've liked/not like:</label>
            <textarea class="text-area-style" v-model="review"></textarea>

            <div class="row btns-cont" >
                    <div class="col-8 ">
                        <button class="btn-style send-btn-style" @click="sendReview()">Confirm review</button>
                    </div>
                    <div class="col-4 d-flex flex-row-reverse">
                        <button class="btn-style back-btn-style" @click="$emit('close')">Back</button>
                    </div>
            </div>
        </div>
        <div id="right-part" class="col-sm-7 overflow-auto">

            <div class="rate-review-cont" v-for="rev in this.reviews" :key="rev">
                <StarRating 
                    :border-width="3" 
                    :star-size="20"
                    :read-only="true"
                    :increment="0.01"
                    :animate="true"
                    :rating="rev.rating">
                </StarRating>
                <textarea class="txt-area-for-review" v-model="rev.review" disabled></textarea>
            </div>
          
        </div>

        <RentalReportingForm
        />

        <SuccessPopUp v-show="successPopUpVisible"
            @close = closeSuccPopUp
            :mess = succMessage
        />

        <ErrorPopUp v-show="errorPopUpVisible" 
            @close = closePopUp
            :mess = errMessage
        /> 
    </div>
</template>

<script>
    import axios from 'axios'; 
    import SuccessPopUp from "@/components/SuccessPopUp.vue";
    import StarRating from 'vue-star-rating';
    import ErrorPopUp from "@/components/ErrorPopUp.vue";
    import RentalReportingForm from "@/components/RentalReportingForm.vue";

    export default {
        name: "RentalReviewingModal",
        components: {
            StarRating,
            RentalReportingForm,
            SuccessPopUp,
            ErrorPopUp
        },
        props: {
            rentalId: Number,
            rentalType: String
        },
        data() {
            return {
                reviews: [],

                review: "",
                rating: 0,

                successPopUpVisible: false,
                succMessage: "You review has been successfully submitted to admin for approval!",

                errorPopUpVisible: false,
                errMessage: "Make sure you have at least one completed reservation before attempting to send a review of this rental!"
            };
        },
        methods: {
            sendReview() {
                let reqBody = {
                    review: this.review,
                    rating: this.rating
                }
                axios.post("api/notification/new-review/" + this.rentalId + "?rentalType=" + this.rentalType,
                            reqBody,
                            {headers: {'authorization': window.localStorage.getItem("token") }}
                )
                    .then(() => {
                        this.successPopUpVisible = true;
                    })
                    .catch((error) => {
                        if (error.response.status === "400")
                                errorPopUpVisible = true;
                    });
            },
            closeSuccPopUp() {
                this.successPopUpVisible = false;
                this.$emit('close');
            },
            closePopUp() {
                this.errorPopUpVisible = false;
            }
        },
        created() {
            axios.get("api/notification/get-rental-reviews/" + this.rentalId, {headers: {'authorization': window.localStorage.getItem("token") }})
                .then((response) => {
                    this.reviews = response.data;
                });
        }
    }
</script>

<style scoped>  
    .modal-cont {
        padding: 30px;
    }

    #left-part {
        text-align: left;
        border-right: 1px solid black;
        border-radius: 30px;
        min-height: 82vh;
        background: linear-gradient(rgba(145, 243, 17, 0.3),
                                    rgba(182, 179, 179, 0.2),
                                    rgba(145, 243, 17, 0.3))
    }

    #right-part {
        padding-top: 20px;
        border-left: 1px solid black;
        border-radius: 30px;
        min-height: 82vh;
        max-height: 82vh;
        background: linear-gradient(rgba(145, 243, 17, 0.3),
                                    rgba(182, 179, 179, 0.2),
                                    rgba(145, 243, 17, 0.3))
    }

    .text-area-style {
        display: block;
        padding: 10px 6px;
        width: 100%;
        box-sizing: border-box;
        border: none;
        border-bottom: 1px solid #ddd;
        color: #555;
        max-height: 380px;
        min-height: 250px;
        border-radius: 20px;
    }

    .txt-area-for-review {
        display: block;
        padding: 10px 6px;
        width: 100%;
        box-sizing: border-box;
        border: none;
        border-bottom: 1px solid #ddd;
        color: #555;
        max-height: 150px;
        min-height: 150px;
        border-radius: 20px;
        border: 1px solid black;
        margin-top: 5px;
    }

    .rate-review-cont {
        margin-bottom: 25px;
    }

    .btn-style {
        background: rgb(51, 160, 233);
        margin: 10px 0px;
        font-weight: bold;
        font-size: 15px;
        border-radius: 20px;
        height: 30px;
    }

    .btn-style:hover {
        margin: 5px 0px;
        height: 40px;
    }

    .back-btn-style {
        background: rgb(146, 144, 144);
        width: 100px;
    }

    .send-btn-style {
        background: rgb(53, 209, 22);
        width: 200px;
    }
</style>

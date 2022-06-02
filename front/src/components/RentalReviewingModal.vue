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
        </div>
        <div id="right-part" class="col-sm-7 overflow-auto">

            <div class="rate-review-cont" v-for="rev in this.reviews" :key="rev">
                <StarRating 
                    :border-width="3" 
                    :star-size="20"
                    :read-only="true"
                    :animate="true"
                    :rating="rev.rating">
                </StarRating>
                <textarea class="txt-area-for-review" v-model="rev.review" disabled></textarea>
            </div>
          
        </div>
    </div>
</template>

<script>
    import StarRating from 'vue-star-rating'
    
    export default {
        name: "RentalReviewingModal",
        components: {
            StarRating
        },
        data() {
            return {
                reviews: [],

                review: "",
                rating: 0
            };
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
        max-height: 400px;
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
</style>

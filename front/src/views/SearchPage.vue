<template>
    <div class="split left">

    </div>
    <div class="split right">
        <form id="search-form" class="form-group" @submit.prevent="submitSearchForm()">
            <div class="row">
                <div class="col-md-6">
                    <label >Start Date:</label>
                    <input class="input-style" type="date" v-model="startDate" title="The starting date you wish for your rental period to be.">
                </div>
                <div class="col-md-6">
                    <label >End Date:</label>
                    <input class="input-style" type="date" v-model="endDate" title="The ending date you wish for your rental period to be.">
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <label >Min Price:</label>
                    <input class="input-style" type="number" min="0" v-model="minPrice" title="Minimum price/day for a rental to be. (euros)">
                </div>
                <div class="col-md-6">
                    <label >Max Price:</label>
                    <input class="input-style" type="number" min="0" v-model="maxPrice" title="Maximum price/day for a rental to be. (euros)">
                </div>
            </div>

            <div class="row">
                <div class="col-md-2">
                    <label >Rating: </label>
                    <select class="input-style" value="None" v-model="rate">
                        <option value="">ALL</option>
                        <option value="1">1 &#9733;</option>
                        <option value="2">2 &#9733;</option>
                        <option value="3">3 &#9733;</option>
                        <option value="4">4 &#9733;</option>
                        <option value="5">5 &#9733;</option>
                    </select>
                </div> 
                <div class="col-md-10">
                    <label >Location: </label>
                    <input class="input-style" type="text" v-model="location">
                </div>
            </div>
            
            <label>What to search for: </label>
            <div class="row checkbox-border d-flex justify-content-center text-center">
                <div class="col-sm">
                    <label>Adventures: </label>
                    <input type="checkbox" value="adventures" v-model="entities">
                </div>
                <div class="col-sm">
                    <label>Cottages: </label>
                    <input type="checkbox" value="cottages" v-model="entities">
                </div>
                <div class="col-sm">
                    <label>Boats: </label>
                    <input type="checkbox" value="boats" v-model="entities">
                </div>                
            </div>

            <div class="submit">
                <button id="subBtn">Search</button>
            </div>
        </form>
    </div>

    <ErrorPopUp v-show="errorPopUpVisible" 
        @close = closePopUp
        :mess = errMessage
    /> 
</template>

<script>
    import ErrorPopUp from "@/components/ErrorPopUp.vue"

    export default {
        name: "SearchPage",
        components: {
            ErrorPopUp
        },
        data() {
            return {
                startDate: '',
                endDate: '',
                minPrice: '',
                maxPrice: '',
                location: '',
                rate: '',
                entities: ["adventures", "cottages", "boats"],

                errMessage : '',
                errorPopUpVisible: false,

                searchResult: []
            };
        },
        methods: {
            closePopUp() {
                this.errorPopUpVisible = false;
            },
            submitSearchForm() {
                let formData = {
                    startDate: this.startDate,
                    endDate: this.endDate,
                    minPrice: this.minPrice,
                    maxPrice: this.maxPrice,
                    location: this.location,
                    rate: this.rate,
                    entities: this.entities
                }

                try { validateForm(formData) }
                catch(errMess) {
                    this.errMessage = errMess;
                    this.errorPopUpVisible = true;
                }

                axios.get("api/rental/search?" + "startDate=" + this.startDate 
                                                + "&endDate=" + this.endDate 
                                                + "&minPrice=" + this.minPrice
                                                + "&maxPrice=" + this.maxPrice
                                                + "&location=" + this.location 
                                                + "&rate=" + this.rate 
                                                + "&entities=" + this.entities.join(","))
                   .then((response) => {
                       console.log(response.data);
                   });
            }
        }
    }

    function validateForm(formData) {
        if (formData.startDate === '' || formData.endDate === '') 
            throw "Please make sure you entered both start and end date for your prefered reservation period."
        
        if (new Date(formData.startDate) <= new Date() || new Date(formData.endDate) <= new Date())
            throw "You can change past or what? Select some future date!";

        if (new Date(formData.startDate) >= new Date(formData.endDate))
            throw "How comes future is before past? Check those start and ending dates...";
    
        if (formData.maxPrice !== '' && formData.minPrice !== '' && formData.maxPrice < formData.minPrice)
            throw "You mixed up minimal and maximal price values. Fix it up!"
        
        if (formData.location !== '' && !/^[a-zA-Z -]{2,50}$/.test(formData.location))
            throw "Make sure you entered valid location name."

        if (formData.entities.length === 0)
            throw "You need to check at least one rental type out of 3."
    }
</script>

<style scoped>
    label {
        font-size: 11px;
    }

    .split {
        height: 100%;
        position: fixed;
        z-index: 1;
        top: 0;
        overflow-x: hidden;
        padding-top: 10px;
    }

    .left {
        width: 60%;
        margin-top: 32px;
        left: 0;
    }

    .right {
        width: 40%;
        right: 0;
    }

    .date-inp {
        width: 20px;
    }

    #search-form {
        margin: 0;
        width: 100%;
        margin: 30px auto;
        text-align: left;
        padding: 40px;
        border-radius: 10px;
    }

   .checkbox-border {
        border: solid 1px #aaa;
        border-radius: 20px;
        padding-bottom: 10px;
        margin-top: 10px;
    }

</style>
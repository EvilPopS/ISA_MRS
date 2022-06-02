<template>
    <div>
        <div id="sort-bar">
            <div class="d-flex justify-content-center">
                <button v-bind:class="{ btn_clicked: nameSortBtnClicked }" @click="sortByName();">&#8645; Rental Name</button>
                <button v-bind:class="{ btn_clicked: startDateSortBtnClicked }" @click="sortByStartDate();">&#8645; Duration Start Date</button>
                <button v-bind:class="{ btn_clicked: endDateSortBtnClicked }" @click="sortByEndDate();">&#8645; Duration End Date</button>
                <button v-bind:class="{ btn_clicked: priceSortBtnClicked }" @click="sortByPrice();">&#8645; Price</button>
            </div>
        </div>

        <div id="res-container" class="row justify-content-center">
            <div id="cards-cont" class="row justify-content-center">
                <div v-for="reserv in this.reservations" :key="reserv.id" class="card-style row" @click="openEntityView(reserv.rentalId, reserv.rentalType)">
                    <div class="col">
                        <img :src="setRentalPic(reserv.rentalPic)" class="rent-image">
                    </div>
                    <div class="col">
                        <label>Reserved rental name:</label>
                        <p>{{reserv.name}}</p>
                        <label>Reservation start date:</label>
                        <p>{{formatDateString(reserv.startDate)}}</p>
                        <label>Reservation end date:</label>
                        <p>{{formatDateString(reserv.endDate)}}</p>
                        <label>Reservation price per day:</label>
                        <p>€{{reserv.price}}</p>
                    </div>
                    <div>
                        <button v-if="!this.isHistoryRes && checkIfCancelable(reserv.startDate)" @click.stop="emitCancel(reserv.id)" class="cancel-btn active-cancel-btn">Cancel</button>
                        <button v-else-if="!this.isHistoryRes && !checkIfCancelable(reserv.startDate)" class="cancel-btn disabled-cancel-btn"><s>Cancel</s></button>
                    </div>
                </div>
            </div>

        </div>
        <RentalViewModal v-if="showRentalViewModal"
            @close = closePopUp
            :id = selectRentalId
            :type = selectRentalType
        />

    </div>
</template>

<script>
    import RentalViewModal from "@/components/RentalViewModal.vue";

    export default {
        name: "ReservationsGallery",
        components: {
            RentalViewModal
        },
        props: {
            reservations: Array,
            isHistoryRes: Boolean
        },
        data() {
            return {
                nameSortBtnClicked: false,
                startDateSortBtnClicked: false,
                endDateSortBtnClicked: false,
                priceSortBtnClicked: false,

                showRentalViewModal: false,
                selectRentalId: null,
                selectRentalType: null
            };
        },
        methods: {
            openEntityView(rentalId, rentalType) {
                this.showRentalViewModal = true;
                this.selectRentalId = rentalId;
                this.selectRentalType = rentalType;
            },
            closePopUp() {
                this.showRentalViewModal = false;
            },
            setRentalPic(rentalPic) {
                try{
                    return require('@/assets/' + rentalPic);
                } catch(e) {}
            },
            emitCancel(resId) {
                this.$emit("cancel-reservation", resId);
            },
            checkIfCancelable(dat) {
                let dNow = new Date(dat);
                dNow.setDate(dNow.getDate()-3)
                return dNow.toISOString() > new Date().toISOString();
            },
            formatDateString(date) {
                return date.split("T")[0].split("-").reverse().join("/");
            },
            sortByName() {
                if (this.nameSortBtnClicked)
                    this.reservations.reverse();
                else {
                    uncheckSortButtons(this);
                    this.nameSortBtnClicked = true;

                    this.reservations.sort(function(left, right) { 
                        let lName = left.name.toUpperCase();
                        let rName = right.name.toUpperCase();
                        if (lName < rName) 
                            return -1;
                        else if (lName > rName)
                            return 1;
                        
                        return 0;
                    });
                }
            },
            sortByStartDate() {
                if (this.startDateSortBtnClicked)
                    this.reservations.reverse();
                else {
                    uncheckSortButtons(this);
                    this.startDateSortBtnClicked = true;

                    this.reservations.sort(function(left, right) {
                        let lDate = new Date(left.startDate).toISOString();
                        let rDate = new Date(right.startDate).toISOString();
                        if (lDate < rDate) 
                            return -1;
                        else if (lDate > rDate)
                            return 1;

                        return 0;
                    });
                }
            },
            sortByEndDate() {
                if (this.endDateSortBtnClicked)
                    this.reservations.reverse();
                else {
                    uncheckSortButtons(this);
                    this.endDateSortBtnClicked = true;

                    this.reservations.sort(function(left, right) { 
                        let lDate = new Date(left.endDate).toISOString();
                        let rDate = new Date(right.endDate).toISOString();
                        if (lDate < rDate) 
                            return -1;
                        else if (lDate > rDate)
                            return 1;

                        return 0;
                    });
                }
            },
            sortByPrice() {
                if (this.priceSortBtnClicked)
                    this.reservations.reverse();
                else {
                    uncheckSortButtons(this);
                    this.priceSortBtnClicked = true;

                    this.reservations.sort(function(left, right) { 
                        let lPrice = left.price;
                        let rPrice = right.price;
                        if (lPrice < rPrice)
                            return -1;
                        else if (lPrice > rPrice)
                            return 1;
                        return 0;
                    });
                }
            }
        }
    }

    function uncheckSortButtons(btns) {
        btns.nameSortBtnClicked = false;
        btns.startDateSortBtnClicked = false;
        btns.endDateSortBtnClicked = false;
        btns.priceSortBtnClicked = false;
    }
</script>

<style scoped>
    #sort-bar {
        margin-top: 60px;
    }

    #sort-bar button {
        margin: 0 5px;
        border-radius: 10px;
        font-weight: bold;
        padding: 4px 15px;
        background-color: rgb(6, 94, 40);
        border: 1px rgb(71, 69, 69) solid;
        color: white;
    }

    #sort-bar button:hover {
        background-color: rgb(13, 143, 63);
    }

    .btn_clicked {
        background-color: rgb(12, 165, 71) !important;
    }

    #cards-cont {
        width: 80%;
        min-height: 80vh;
        border-left: 1px solid rgb(103, 104, 101);
        border-right: 1px solid rgb(104, 101, 101);
        border-radius: 20px;
        background: linear-gradient(rgba(47, 105, 40, 0.05), 
                                    rgba(8, 37, 9, 0.2), 
                                    rgba(47, 105, 40, 0.05));
    }

    #res-container {
        margin-top: 30px;
        max-width: 99.5vw;
    }

    .card-style {
        border: 1px solid black;
        border-radius: 20px;
        width: 535px;
        height: 335px;
        padding-top: 20px;
        margin: 20px;
    }

    .card-style:hover {
        margin: 12px 12px;
        width: 551px;
        height: 351px;
        background-color: rgba(245, 238, 238, 0.8);
    }

    .card-style label {
        font-size: 12px;
        color: rgb(16, 92, 9);
        border-top: 1px solid black;
        margin: 0;
    }

    .card-style p {
        font-weight: bold;
    }

    .cancel-btn {
        border-radius: 20px;
        font-weight: bold;
        width: 150px;
        margin-bottom: 5px;
    }

    .rent-image {
        height: 250px;
        width: 250px;
        border-radius: 30px 10px 30px 10px;
    }

    .active-cancel-btn {
        background-color: rgb(32, 204, 32);
    }

    .disabled-cancel-btn {
        background-color: rgb(226, 38, 38);
    }
</style>
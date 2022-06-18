<template>
    <LoyaltyProgram 
        :userProgram="userProgram"
        @buy-loyalty-program="buyLoyaltyProgram"
    />

    <SuccessPopUp v-show="succPopUpVisible"
        :mess="sucMessage"
        @close="closePopUp"
    />

    <ErrorPopUp v-show="errPopUpVisible"
        :mess="errMessage"
        @close="closePopUp"
    />
</template>

<script>
    import SuccessPopUp from '@/components/SuccessPopUp.vue';
    import ErrorPopUp from '@/components/ErrorPopUp.vue';
    import LoyaltyProgram from "@/components/LoyaltyProgram.vue";
    import axios from 'axios';

    export default {
        name: "ClientLoyaltyProgramView",
        components: {
            LoyaltyProgram,
            SuccessPopUp,
            ErrorPopUp
        },
        data() {
            return {
                userProgram: "REGULAR",

                sucMessage: "You successfully upgraded your loyalty program!",
                errMessage: "",

                succPopUpVisible: false,
                errPopUpVisible: false,
                searchRole: '',
                roleURL: ''
            };
        },
        mounted() {
            axios.get("api/" + this.roleURL, {headers: {'authorization': window.localStorage.getItem("token") }})
                .then((response) => {
                    this.userProgram = response.data.loyaltyStatus;
                });
        },
        methods: {
            buyLoyaltyProgram(loyaltyProgram) {
                axios.put("api/" + this.roleURL + "/buy-loyalty-program/" + loyaltyProgram, {}, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then(() => {
                        this.userProgram = loyaltyProgram;
                        this.succPopUpVisible = true;
                    }).catch((error) => {
                        if (error.response.status == 400) {
                            this.errMessage = "Looks like you don't have enough points to make an upgrade!"
                            this.errPopUpVisible = true
                        }
                    });
            },
            closePopUp() {
                this.succPopUpVisible = false;
                this.errPopUpVisible = false;
            }
        },
        mounted() {
            this.searchRole = window.localStorage.getItem("userRole")
            if (this.searchRole === "COTTAGE_OWNER"){
                this.roleURL = "cottage-owner"
            } else if (this.searchRole === "INSTRUCTOR"){
                this.roleURL = "fishingInstructor"
            } else if (this.searchRole === "BOAT_OWNER"){
                this.roleURL = "boat-owner"
            } else if (this.searchRole === "CLIENT"){
                this.roleURL = "client"
            }
        }
    }
</script>

<style scoped>

</style>

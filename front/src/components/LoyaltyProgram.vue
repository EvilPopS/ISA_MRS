<template>
    <div class="row cards-cont">

        <div class="col">
            <div id="bronze-clr" class="loyalty-card" :class="{ glowing_card: userProgram === 'BRONZE' }" @click="buyBronzeProgram()">
                <div class="hover-fade">
                    <p class="program-name">BRONZE</p>

                    <p v-if="this.role === 'CLIENT'" class="info-caption">
                        The bronze program grants you a discount of 
                        <span class="spans-style">{{bronzeDisc}}</span> 
                        on all reservations!
                    </p>
                    <p v-else class="info-caption">
                        The bronze program grants you an increase of 
                        <span class="spans-style">{{bronzeBonus}}</span> 
                        on overall profit of every reservations!
                    </p>

                    <p class="cost-caption">
                        Bronze program costs 
                        <span class="spans-style">{{bronzePrice}}</span>
                        loyalty points!
                    </p>
                </div>
            </div>
        </div>

        <div class="col">
            <div id="silver-clr" class="loyalty-card" :class="{ glowing_card: userProgram === 'SILVER' }" @click="buySilverProgram()">
                <div class="hover-fade">
                    <p class="program-name">SILVER</p>

                    <p v-if="this.role === 'CLIENT'" class="info-caption">
                        The silver program grants you a discount of 
                        <span class="spans-style">{{silverDisc}}</span> 
                        on all reservations.
                    </p>
                    <p v-else class="info-caption">
                        The silver program grants you an increase of 
                        <span class="spans-style">{{silverBonus}}</span> 
                        on overall profit of every reservations!
                    </p>

                    <p class="cost-caption">
                        Silver program costs 
                        <span class="spans-style">{{silverPrice}}</span>
                        loyalty points!
                    </p>
                </div>
            </div>
        </div>

        <div class="col">
            <div id="gold-clr" class="loyalty-card" :class="{ glowing_card: userProgram === 'GOLD' }" @click="buyGoldProgram()">
                <div class="hover-fade">
                    <p class="program-name">GOLD</p>

                    <p v-if="this.role === 'CLIENT'" class="info-caption">
                        The gold program grants you a discount of 
                        <span class="spans-style">{{goldDisc}}</span> 
                        on all reservations.
                    </p>
                    <p v-else class="info-caption">
                        The bronze program grants you an increase of 
                        <span class="spans-style">{{goldBonus}}</span> 
                        on overall profit of every reservations!
                    </p>

                    <p class="cost-caption">
                        Gold program costs 
                        <span class="spans-style">{{goldPrice}}</span>
                        loyalty points!
                    </p>
                </div>
            </div>
        </div>

    <ConfirmationPopUp v-show="showConfPopUp"
        :title="confTitle"
        :message="confMessage"
        @modal-closed="closeConfirmationPopUp"
        @confirmed-event="buyLoyaltyProgram"
    />

    <ErrorPopUp v-show="showErrPopUp"
        :mess="errMessage"
        @close="closeErrorPopUp"
    />

    </div>
</template>

<script>
    import axios from "axios";
    import ConfirmationPopUp from "@/components/ConfirmationPopUp.vue";
    import ErrorPopUp from "@/components/ErrorPopUp.vue";

    export default {
        name: "LoyaltyProgram",
        components: {
            ConfirmationPopUp,
            ErrorPopUp
        },
        props: {
            userProgram: String
        },
        data() {
            return {
                role: window.localStorage.getItem("userRole"),

                confTitle: "",
                confMessage: "",
                showConfPopUp: false,
                selectedProgram: "",

                showErrPopUp: false,
                errMessage: "",

                bronzeDisc: "",
                bronzePrice: "",
                bronzeBonus: "",
                silverDisc: "",
                silverPrice: "",
                silverBonus: "",
                goldDisc: "",
                goldPrice: "",
                goldBonus: ""
            };
        },
        mounted() {
            axios.get("api/loyalty-program/get-info", {headers: {'authorization': window.localStorage.getItem("token") }})
                .then((response) => {
                    let lpData = response.data;
                    
                    if (this.role === "CLIENT") {
                        this.bronzeDisc = lpData.bronzeDisc + "%";
                        this.silverDisc =  lpData.silverDisc + "%";
                        this.goldDisc =  lpData.goldDisc + "%";
                    }
                    else {
                        this.bronzeBonus = lpData.bronzeBonus + "%";
                        this.silverBonus = lpData.silverBonus + "%";
                        this.goldBonus = lpData.goldBonus + "%";
                    }

                    this.bronzePrice = lpData.bronzePrice;
                    this.silverPrice = lpData.silverPrice;
                    this.goldPrice =  lpData.goldPrice;
                }
            );
        },
        methods: {
            buyBronzeProgram() {
                if (this.userProgram === "REGULAR") {
                    this.confTitle = "BRONZE LOYALTY PROGRAM.";
                    this.confMessage = "You are about to spend " + this.bronzePrice + " points on the BRONZE loyalty program. Are you sure you want to proceed?";
                    this.selectedProgram = "BRONZE";
                    this.showConfPopUp = true;
                }
                else {
                    this.errMessage = "You can buy BRONZE loyalty program only if you are currenlty on REGULAR program!";
                    this.showErrPopUp = true;
                }
            },
            buySilverProgram() {
                if (this.userProgram === "REGULAR" || this.userProgram === "BRONZE") {
                    this.confTitle = "SILVER LOYALTY PROGRAM.";
                    this.confMessage = "You are about to spend " + this.silverPrice + " points on the SILVER loyalty program. Are you sure you want to proceed?";
                    this.selectedProgram = "SILVER";
                    this.showConfPopUp = true;
                }
                else {
                    this.errMessage = "You can buy SILVER loyalty program only if you are currenlty on REGULAR or BRONZE program!";
                    this.showErrPopUp = true;
                }
            },
            buyGoldProgram() {
                if (this.userProgram === "REGULAR" || this.userProgram === "BRONZE" || this.userProgram === "SILVER") {
                    this.confTitle = "GOLD LOYALTY PROGRAM.";
                    this.confMessage = "You are about to spend " + this.goldPrice + " points on the GOLD loyalty program. Are you sure you want to proceed?";
                    this.selectedProgram = "GOLD";
                    this.showConfPopUp = true;
                }
                else {
                    this.errMessage = "You can buy GOLD loyalty program only if you are currenlty on REGULAR, BRONZE or SILVER program!";
                    this.showErrPopUp = true;
                }
            },
            closeConfirmationPopUp() {
                this.showConfPopUp = false;
            },
            closeErrorPopUp() {
                this.showErrPopUp = false;
            },
            buyLoyaltyProgram() {
                this.showConfPopUp = false;
                this.$emit("buy-loyalty-program", this.selectedProgram);
            }
        }
    }
</script>

<style scoped>
    .cards-cont {
        margin-top: 50px;
        max-width: 100vw;
    }

    .loyalty-card {
        border: 1px solid black;
        border-radius: 20px 45px 20px 45px;
        margin: calc((100vh - 50px - 60vh) /2) auto;
        height: 60vh;
        width: 20vw;
    }

    .loyalty-card:hover { 
        margin: calc((100vh - 50px - 65vh) /2) auto;
        height: 65vh;
        width: 24vw;
    }

    #gold-clr {
        background: linear-gradient(45deg, rgb(248, 232, 3), rgb(230, 223, 165), rgb(255, 251, 0));
    }

    #silver-clr {
        background: linear-gradient(45deg, rgb(85, 85, 84), rgb(194, 193, 183), rgb(85, 85, 84));
    }

    #bronze-clr {
        background: linear-gradient(45deg, rgb(114, 69, 1), rgb(228, 219, 137), rgb(114, 69, 1));
    }

    .hover-fade {
        height: 100%;
        width: 100%;
    }
    .hover-fade:hover {
        background: rgba(255, 255, 255, 0.211);
    }

    .program-name {
        padding-top: 1vh;
        padding-bottom: 10vh;
        font-family: Impact, Charcoal, sans-serif;
        font-size: 45px;
        letter-spacing: 2.6px;
        word-spacing: 2px;
        color: #000000;
        font-weight: 700;
        text-decoration: overline solid rgb(68, 68, 68);
        font-style: italic;
        font-variant: normal;
        text-transform: uppercase;
    }

    .info-caption {
        padding: 0 0.5vw;
        font-family: Charcoal, sans-serif;
        font-size: 20px;
        letter-spacing: 2.6px;
        word-spacing: 2px;
        color: #000000;
        font-weight: 700;
        font-style: italic;
        font-variant: normal;
    }

    .cost-caption {
        padding: 8vh 1vw;
        font-family: Charcoal, sans-serif;
        font-size: 20px;
        letter-spacing: 2.6px;
        word-spacing: 2px;
        color: #000000;
        font-weight: 700;
        font-style: italic;
        font-variant: normal;
    }
    
    .spans-style {
        font-size: 25px;
        color: rgb(154, 7, 173);
    }

    .glowing_card {
        border-color: #9ecaed;
        box-shadow: 0 0 100px #056bbe;
    }

</style>

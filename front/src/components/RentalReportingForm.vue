<template>
    <div class="popup-overlay" @click="$emit('close')">
        <div id="report-form-cont" @click.stop>
            <p class="modul-caption">Hm... You rated this rental with a low mark. If you want to report something just fill the form below and hit the green button!</p>
            <textarea class="report-txt-area" v-model="reportMess"></textarea>
            <div class="d-flex justify-content-center">
                <button class="btn-style send-btn-style" type="submit" @click="sendReport()">Send report</button>
            </div>
            <div class="row-reverse">
                <button class="btn-style quit-btn-style" @click="$emit('close')">Quit reporting</button>
            </div>
        </div>

        <SuccessPopUp v-show="successPopUpVisible"
            @close = closeSuccPopUp
            :mess = succMessage
        />
    </div>
</template>

<script>
    import axios from "axios";
    import SuccessPopUp from "@/components/SuccessPopUp.vue";

    export default {
        name: "RentalReportingForm",
        components: {
            SuccessPopUp
        },
        props: {
            rentalId: Number,
            rentalType: String
        },
        data() {
            return { 
                reportMess: "",

                successPopUpVisible: false,
                succMessage: "Your report has been submitted successfully!"
            }
        },
        methods: {
            sendReport() {
                if (this.reportMess === "")
                    return;
                axios.post("api/notification/new-report/" + this.rentalId + "?rentalType=" + this.rentalType,
                            {report: this.reportMess},
                            {headers: {'authorization': window.localStorage.getItem("token") }}
                ).then(() => {
                        this.successPopUpVisible = true;
                    });
            },
            closeSuccPopUp() {
                this.successPopUpVisible = false;
                this.$emit('close');
            }
        }
    }
</script>

<style scoped>
    .popup-overlay {
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        display: flex;
        justify-content: center;
        background-color: #000000da;
        z-index: 999;
    }

    #report-form-cont {
        width: 60vw;
        height: 64vh;
        min-height: 500px;
        background: white;
        margin-top: 9vh;
        border-radius: 30px;
        padding: 10px;
        background: linear-gradient(rgb(182, 179, 179), rgb(245, 241, 241), rgb(182, 179, 179));
    }

    .modul-caption {
        font-family: Georgia, serif;
        font-size: 25px;
        letter-spacing: -1.2px;
        word-spacing: 0px;
        color: #000000;
        font-weight: 700;
        text-decoration: none;
        font-style: italic;
        font-variant: small-caps;
        text-transform: none;
    }

    .report-txt-area {
        margin-top: 25px;
        width: 37vw;
        height: 32vh;
        min-height: 32vh;
        max-height: 32vh;
        border-radius: 30px 50px 30px 50px;
        border: 1px solid black;
        padding: 10px 20px;
        background: white;
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

    .quit-btn-style {
        background: rgb(223, 54, 54);
        width: 150px;
    }

    .send-btn-style {
        background: rgb(53, 209, 22);
        width: 200px;
    }
</style>
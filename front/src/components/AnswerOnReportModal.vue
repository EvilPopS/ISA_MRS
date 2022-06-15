<template>
    <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow" class="close">X</button>
            <div class="container">
                <div class="my-text-area">
                    <label for="floatingTextarea2" class="label-text-area d-flex justify-content-start">Message for client {{clientFullName}}:</label>
                    <textarea class="form-control" placeholder="Leave your message for client here..." id="floatingTextarea2" style="height: 100px" v-model="answerForClient.message"></textarea>
                </div>
                <div class="my-text-area">
                    <label for="floatingTextarea2" class="label-text-area d-flex justify-content-start">Message for owner {{ownerFullName}}:</label>
                    <textarea class="form-control" placeholder="Leave your message for owner here..." id="floatingTextarea2" style="height: 100px" v-model="answerForOWner.message"></textarea>
                </div>
                <div class="vstack gap-2 col-md-5 mx-auto" id="options-btns">
                    <button type="button" class="btn btn-success" @click="send(false)">Send messages to owner and client</button>
                    <button type="button" class="btn btn-danger" @click="closeWindow">Close</button>
                </div>
            </div>
        </div>
        <ErrorPopUp v-show="errorPopUpVisible" 
            @close = closePopUp
            :mess = errMessage
        /> 
        <SuccessPopUp v-show="localSuccPopUpVisible"
            @close = closeSuccPopUp
            :mess = succMessage
        />
    </div>
</template>

<script>
import axios from 'axios'
import ErrorPopUp from "./ErrorPopUp.vue"
import SuccessPopUp from "./SuccessPopUp.vue"

export default {
    name: "MakeReportModal",
    components: {
        ErrorPopUp, SuccessPopUp
    },
    props: {
        clientEmail: String,
        ownerEmail: String,
        reportId: String,
        clientFullName: String,
        ownerFullName: String,
    },
    data(){
        return {
            answerForClient: {
                reportId: this.reportId,
                message: '',
                clientEmail: this.clientEmail,
                ownerEmail: this.ownerEmail
            },
            answerForOWner: {
                reportId: this.reportId,
                message: '',
                clientEmail: this.clientEmail,
                ownerEmail: this.ownerEmail
            },
            errorPopUpVisible: false,
            localSuccPopUpVisible: false,
            succMessage: '',
            errMessage: ''
        }
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
        },
        positiveFeedback() {
            this.report.isNegative = false
        },
        negativeFeedback() {
            this.report.isNegative = true
        },
        changeShowedUp() {
            this.report.hasShowedUp = !this.report.hasShowedUp;
        },
        closePopUp() {
            this.errorPopUpVisible = false;
        },
        closeSuccPopUp() {
                this.localSuccPopUpVisible = false;
                this.$emit('succ-pop-up-closed');
                this.$router.go(); 
        },
        send() {
                if (this.answerForClient.message.length < 15 || this.answerForClient.message.length < 15) {
                    this.errMessage = "Message must have at least 15 characters!";
                    this.errorPopUpVisible = true;
                } 
                else {    
                    axios.post("api/admin/answer-on-report/client", this.answerForClient, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then((response) => {
                        // this.localSuccPopUpVisible = true;
                        // this.succMessage = 'You have successfully answered on ' + this.clientFullName  + ' report!';
                    })
                    .catch(err => {
                            if (err.response.status === 404){
                                this.errMessage = "Client or owner with given email address is not found!";
                                this.errorPopUpVisible = true;
                            } 
                            else if (err.response.status === 401) {
                                this.errMessage = "You are not authorized!";
                                this.errorPopUpVisible = true;
                            }
                            else if (err.response.status === 422) {
                                this.errMessage = "Message must have at least 15 characters!";
                                this.errorPopUpVisible = true;
                            } else {
                                this.errMessage = "Uups! Something went wrong...";
                                this.errorPopUpVisible = true;
                            }
                        });
                    axios.post("api/admin/answer-on-report/owner", this.answerForOWner, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then((response) => {
                        this.localSuccPopUpVisible = true;
                        this.succMessage = 'You have successfully answered on '  + this.clientFullName  + "'s report on " + this.ownerFullName  + '.';
                    })
                    .catch(err => {
                            if (err.response.status === 404){
                                this.errMessage = "Client or owner with given email address is not found!";
                                this.errorPopUpVisible = true;
                            } 
                            else if (err.response.status === 401) {
                                this.errMessage = "You are not authorized!";
                                this.errorPopUpVisible = true;
                            }
                            else if (err.response.status === 422) {
                                this.errMessage = "Message must have at least 15 characters!";
                                this.errorPopUpVisible = true;
                            } else {
                                this.errMessage = "Uups! Something went wrong...";
                                this.errorPopUpVisible = true;
                            }
                        });
                    
                }
    }
}
}

</script>

<style scoped>
    b {
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
        width: 60%;
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
    .my-text-area {
        margin-top: 5%;
        margin-bottom: 5%;
    }
    .label-text-area, .label-desc {
        font-size: 15px;
        color: #335c50cc;
        display: block;
    }
    .label-desc {
        margin-top: 5%;
    }
    textarea.form-control:focus {
        outline: none !important;
        border:1px solid rgba(51, 92, 80, 0.8);
        box-shadow: 0 0 10px rgba(51, 92, 80, 0.8);
    }
    h3 {
        color: rgba(51, 92, 80, 0.8);
    }
    div input.btn-check{
        visibility: hidden;
    }
    label.left-btn-check {
        margin-right: 2%;
    }
</style> 
<template>
    <div id="optionsBar" class="card flex-card">
            <TabNav
                :tabs="['Account deletion/registration', 'Rental reviews']"
                :selected="selected"
                @selected="setSelected"
            >
            <Tab :isSelected="selected === 'Account deletion/registration'">
                <DeletionRegistrationNotifications :data="requests"
                @show-confirm-rejection-dialog = "showConfirmRejectionDialog"
                @show-confirm-allowing-dialog  = "showConfirmAllowingDialog"
                
                ></DeletionRegistrationNotifications>
            </Tab>

            <Tab :isSelected="selected === 'Rental reviews'">
                <RentalReviews :data="reviews"
                @show-confirm-rejection-dialog = "confirmReviewRejectingDialog"
                @show-confirm-allowing-dialog  = "confirmReviewAllowingDialog"
                
                ></RentalReviews>
            </Tab>


            </TabNav>
    </div>

        <div v-if="confirmationPopUpVisible">
        <ConfirmationPopUp
        :title="rejectionTitle"
        :message="rejectionMessage"
        @modal-closed = "confirmationPopUpVisible = false"
        @confirmed-event = "confirmedEvent" 
        />
        </div>
        <SuccessPopUp v-show="succPopUpVisible"
            @close = "succPopUpVisible = false"
            :mess = succMessage
        />
        <ErrorPopUp v-show="errorPopUpVisible" 
            @close = closeErrorPopUp
            :mess = errMessage
        /> 

    
    
        
</template>

<script>
import axios from 'axios'
import SuccessPopUp from '../components/SuccessPopUp.vue'
import ConfirmationPopUp from '../components/ConfirmationPopUp.vue'
import ErrorPopUp from '../components/ErrorPopUp.vue'
import TabNav from '../components/TabNav.vue'
import Tab from '../components/Tab.vue'
import DeletionRegistrationNotifications from '../components/DeletionRegistrationNotifications.vue'
import RentalReviews from '../components/RentalReviews.vue'

export default {
    name : 'AdminNotifications',
    components : {
        SuccessPopUp,
        ConfirmationPopUp,
        ErrorPopUp,
        TabNav,
        Tab, 
        DeletionRegistrationNotifications,
        RentalReviews
    },
    data () {
        return {
            requests : [],
            reviews : [],
            confirmationPopUpVisible : false,
            succPopUpVisible : false,
            errorPopUpVisible : false,
            selected : '',

            rejectionMessage : '',
            rejectionTitle : '',
            succMessage : '',
            errMessage : '',

            requestToBeAllowed : Object,
            requestToBeRejected : Object,
            reviewToBeAllowed : Object,
            reviewToBeRejected : Object

        }
    },

    methods : {
            setPicture(request) {
                try{
                    return require('../assets/' + 'logo.png'); // ovde promeniti da ucita sliku koju stvarno treba da ucita
                } catch(e) {}
        },
            setSelected(tab) {
                this.selected = tab;
        },
        confirmedEvent(){
            if (this.selected === 'Rental reviews'){
                this.rejectReview();
            }else{
                this.rejectRequest();
            }
        },

        closeErrorPopUP()  {
            this.errorPopUpVisible = true;
        },

        showConfirmAllowingDialog: function(request){
            this.requestToBeAllowed = request;
            this.allowRequest();

        },
        showConfirmRejectionDialog : function(request){
            this.requestToBeRejected = request
            this.rejectionTitle = 'Are you sure?';
            this.rejectionMessage = 'Request is going to be rejected.'
            this.confirmationPopUpVisible = true;

        },

        confirmReviewAllowingDialog: function(review){
            this.reviewToBeAllowed = review;
            this.allowReview();

        },
        confirmReviewRejectingDialog : function(review){
            this.reviewToBeRejected = review
            this.rejectionTitle = 'Are you sure?';
            this.rejectionMessage = 'Review is going to be rejected.';
            this.confirmationPopUpVisible = true;

        },

        allowReview(){

            axios.post('api/rental/gradeUpdate', this.reviewToBeAllowed, {headers: {'authorization': window.localStorage.getItem("token")}}).then((response) => {
                
                this.reviews = this.reviews.filter(item => item != this.reviewToBeAllowed);
                this.succPopUpVisible = true;
                this.succMessage = 'Review is successfully allowed.';
                this.reviews.push();

            }).catch((e) => {
                this.errMessage = e ;
                this.errorPopUpVisible = true;
            }) 

        },



        allowRequest(){
            console.log("Allowing request...");
            console.log( window.localStorage.getItem("token"));
            console.log(this.requestToBeAllowed.requestType);
            this.confirmationPopUpVisible = false;
            // obrisi sendera i stavi isAnswered na true
            if (this.requestToBeAllowed.requestType === 'ACCOUNT DELETION'){
                console.log("usaoooooooooooooo");
                axios.post('api/admin/delete-user/allow', this.requestToBeAllowed, {headers: {'authorization': window.localStorage.getItem("token")}}).then((response) => {
                this.requests = this.requests.filter(item => item != this.requestToBeAllowed);
                this.succPopUpVisible = true;
                this.succMessage = 'Account deletion request is successfully allowed.';
                this.requests.push();
            }).catch((e) => {
                this.errMessage = e ;
                this.errorPopUpVisible = true;
            });
            } else if (this.requestToBeAllowed.requestType === "RENTAL SERVICE RATE"){
                console.log("allow rental service rate");
            }
            else if (this.requestToBeAllowed.requestType === 'ACCOUNT REGISTRATION'){
                axios.post('api/admin/registration/allow',this.requestToBeAllowed, {headers: {'authorization': window.localStorage.getItem("token") }}).then((response) => {
                this.requests = this.requests.filter(item => item != this.requestToBeAllowed);
                this.succPopUpVisible = true;
                this.succMessage = 'Account registration request is successfully allowed.';
                this.requests.push();


            }).catch((e) => {
                this.errMessage = e;
                this.errorPopUpVisible = true;
            });
            }
        },
        rejectRequest(){
            this.confirmationPopUpVisible = false;
            axios.post('api/admin/delete-user/reject', this.requestToBeRejected, {headers: {'authorization': window.localStorage.getItem("token") }}).then((response) => {
                this.requests = this.requests.filter(item => item != this.requestToBeRejected);
                this.succPopUpVisible = true;
                this.succMessage = 'Request is successfully rejected.';
                this.requests.push();
            }).catch((e) => {
                this.errMessage = e;
                this.errorPopUpVisible = true;

            })
            
        },
        rejectReview(){

            axios.post('api/admin/reject-review',this.reviewToBeRejected, {headers: {'authorization': window.localStorage.getItem("token") }}).then((response) => {
                this.reviews = this.reviews.filter(item => item != this.reviewToBeRejected);
                this.succPopUpVisible = true;
                this.confirmationPopUpVisible = false;
                this.succMessage = 'Review is successfully rejected.';
                this.reviews.push();
            }).catch((e) => {
                this.errMessage = e;
                this.errorPopUpVisible = true;

            })

            console.log('aaaaaaaaaa');
            
            
        }

    },

    created () {

        axios.get('api/admin/requests', {headers: {'authorization': window.localStorage.getItem("token") }} ).then((response) => {
            this.requests = response.data;
        })

        axios.get('api/admin/reviews', {headers: {'authorization': window.localStorage.getItem("token") }} ).then((response) => {
            this.reviews = response.data;
        })



    }
}

</script>




<style scoped>
    


    #tabNav {
    margin: 50px 0px 30px 0px;
    }

    a {
    color: rgba(15, 95, 72, 0.95);
    }

    li {
    background-color: rgba(214, 218, 216, 0.7);
    }

    #notification-img {
        margin-top: 10px !important;
        height: 100px;
        width: auto;
        display: block;
        margin: 0 auto;
        border-radius: 10%;
    }

    #adventure-img:hover{
        width: 230px;
        height: 170px;
        border-radius: 20px;
        background: linear-gradient(rgb(255, 253, 253), rgb(241, 239, 239));
    }

    b{
        color: rgba(51, 92, 80, 0.8);
    }

    span button {
        margin: 10px 10px;
    }

    div h5#heading-adventure {
        color: rgba(51, 92, 80, 0.8);
        font-weight: bold;
    }

    div div#card-body-id {
        background-color: rgba(214, 218, 216, 0.7);
    }

    #btn-add{
        margin: 30px;
        float: left;
    }

    #btn-add:hover{
        border-radius: 10px;
        background: rgb(25, 117, 7);
    }
</style>
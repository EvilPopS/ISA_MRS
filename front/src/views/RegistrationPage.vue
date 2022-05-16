<template>
    <div class="d-flex justify-content-center tabs-nav">
        <button :class='!this.isRegClientSelected ? "tab-btn left": "tab-btn left toggled"' @click='this.isRegClientSelected = true'>
            Register as client
        </button>
        <button :class='this.isRegClientSelected ? "tab-btn right": "tab-btn right toggled"' @click='this.isRegClientSelected = false'>
            Register as owner/instructor
        </button>
    </div>

    <form id="form-style" class="justify-content-center">
        <div id="form-cont" class="row">
            <div class="col split">
                <div class="row">
                    <div class="col-sm-6">
                        <label>Name: </label>
                        <input type="text" v-model="name">
                    </div>
                    <div class="col-sm-6">
                        <label>Surname: </label>
                        <input type="text" v-model="surname">
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-7">
                        <label>Email: </label>
                        <input type="text" v-model="email">
                    </div>
                    <div class="col-sm-5">
                        <label>Phone Number: </label>
                        <input type="text" v-model="phoneNumber">
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-3">
                        <label>Country: </label>
                        <input type="text" v-model="country">
                    </div>
                    <div class="col-sm-3">
                        <label>City: </label>
                        <input type="text" v-model="city">
                    </div>
                    <div class="col-sm-6">
                        <label>Address: </label>
                        <input type="text" v-model="address">
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <label>Password: </label>
                        <input type="password" v-model="password">
                    </div>
                    <div class="col-sm-6">
                        <label>Confirm password: </label>
                        <input type="password" v-model="confirmPassword">
                    </div>
                </div>
            </div>
            <div class="col split">
                <div class="row">
                    <div class="col-sm-6">
                        <label>Profile picture: </label>
                        <input ref="picInp" class="form-control form-control-sm" type="file" accept="image/*" @change="setProfilePicture();"/>
                    </div>
                    <div class="col-sm-6">
                        <img id="img-prev" :src='setPicturePreview()' alt="">
                    </div>
                </div>
                <div class="btn-group row" role="group" v-show="!isRegClientSelected">
                    <label>Choose account type:</label>
                    <div class="d-flex">
                        <input type="radio" class="btn-check" name="btnradio" id="cottage-owner" autocomplete="off" value="cottage-owner" v-model="userType">
                        <label id="cottage-owner-lbl" class="btn btn-outline-primary" for="cottage-owner">Cottage owner</label>

                        <input type="radio" class="btn-check" name="btnradio" id="boat-owner" autocomplete="off" value="boat-owner" v-model="userType">
                        <label id="boat-owner-lbl" class="btn btn-outline-primary" for="boat-owner">Boat owner</label>

                        <input type="radio" class="btn-check" name="btnradio" id="instructor" autocomplete="off" value="instructor" v-model="userType">
                        <label id="instructor-lbl" class="btn btn-outline-primary" for="instructor">Instructor</label>
                    </div>
                </div>

                <div v-show="!isRegClientSelected"> 
                    <label>Registration reason: </label>
                    <textarea id="reg-reason" v-model="regReason"></textarea>
                </div>
            </div>
        </div>
    </form>
    <button id="reg-btn" @click="submitRegistration()">REGISTER</button>

    <ErrorPopUp v-show="errorPopUpVisible" 
        @close = closePopUp
        :mess = errMessage
    /> 
    
    <SuccessPopUp v-show="succPopUpVisible"
        @close = closeSuccPopUp
        :mess = succMessage
    />
</template>

<script>
    import ErrorPopUp from "@/components/ErrorPopUp.vue"; 
    import SuccessPopUp from "@/components/SuccessPopUp.vue";
    import axios from 'axios';

    export default {
        name: "RegistrationPage",
        components: {
            ErrorPopUp,
            SuccessPopUp
        },
        data() {
            return {
                isRegClientSelected: true,

                name: "",
                surname: "",
                email: "",
                password: "",
                confirmPassword: "",
                country: "",
                city: "",
                address: "",
                phoneNumber: "",
                profilePicture: "addPhoto.png",

                userType: "",
                regReason: "",

                errMessage: "",
                errorPopUpVisible: false,

                succMessage: "",
                succPopUpVisible: false
            };
        },
        methods: {
            setProfilePicture() {
                this.profilePicture = this.$refs.picInp.value.split("\\")[2];
            },
            setPicturePreview() {
                return require("@/assets/" + this.profilePicture);
            },
            closePopUp() {
                this.errorPopUpVisible = false;
            },
            closeSuccPopUp() {
                this.succPopUpVisible = false;
                this.$router.push({ name: "LoginPage" });
            },
            submitRegistration() {
                try {
                    validateUniRegForm(this);
                    if (!this.isRegClientSelected)
                        validateSpecRegForm(this);
                } catch (error) {
                    this.errMessage = error;
                    this.errorPopUpVisible = true;
                    return;
                }

                if (this.isRegClientSelected) {
                    let requestBody = {
                        name: this.name,
                        surname: this.surname,
                        email: this.email,
                        password: this.password,
                        country: this.country,
                        city: this.city,
                        address: this.address,
                        phoneNumber: this.phoneNumber,
                        profilePicture: this.profilePicture
                    }
                    axios.post("api/unauth/register/client", requestBody).then((response) => {
                        axios.get("api/unauth/send-confirmation-mail/" + this.email).then(response => {console.log("Uspeoooooooooooooooo")});
                        this.succMessage = "Your account has been successfully registered, and confirmation email has been sent to " 
                                                + this.email + 
                                            ". To activate your account please click link in the email message!"
                        this.succPopUpVisible = true;
                    }).catch(err => {
                        if (err.response.status === 409){
                            this.errMessage = "An account with the given email address already exists!";
                            this.errorPopUpVisible = true;
                        } 
                        else if (err.response.status === 422) {
                            this.errMessage = "Please make sure the data you entered are valid and the phone number is not used already.";
                            this.errorPopUpVisible = true;
                        }
                    });
                }
                else {
                    let requestBody = {
                        name: this.name,
                        surname: this.surname,
                        email: this.email,
                        password: this.password,
                        country: this.country,
                        city: this.city,
                        address: this.address,
                        phoneNumber: this.phoneNumber,
                        profilePicture: this.profilePicture,
                        userType: this.userType,
                        regReason: this.regReason
                    }
                    if (this.userType == "cottage-owner"){
                        axios.post("api/cottage-owner/register", requestBody).then((response) => {
                            this.succMessage = "Your account has been successfully registered. Registration request will be processed by admin as soon as possible!"
                            this.succPopUpVisible = true;
                        }).catch(err => {
                            if (err.response.status === 409){
                                this.errMessage = "An account with the given email address already exists!";
                                this.errorPopUpVisible = true;
                            } 
                            else if (err.response.status === 422) {
                                this.errMessage = "Please make sure your phone number is not used by another account.";
                                this.errorPopUpVisible = true;
                            }
                        });
                    } else if (this.userType == "instructor"){
                        axios.post("api/fishingInstructor/register", requestBody).then((response) => {
                            this.succMessage = "Your account has been successfully registered. Registration request will be processed by admin as soon as possible!"
                            this.succPopUpVisible = true;
                        }).catch(err => {
                            if (err.response.status === 409){
                                this.errMessage = "An account with the given email address already exists!";
                                this.errorPopUpVisible = true;
                            } 
                            else if (err.response.status === 422) {
                                this.errMessage = "Please make sure your phone number is not used by another account.";
                                this.errorPopUpVisible = true;
                            }
                        });
                    } else if (this.userType == "boat-owner") {
                        //ovde ce ici boat
                    }
                    
                }
            }

        }
    }

    function validateUniRegForm(formData) {
        let nameReg = /^[a-zA-Z ]{2,30}$/;
        let passwordReg = /^[a-zA-Z0-9!@#$%^&*()_+-]{6,30}$/;
        let numReg = /^[0-9]{5,30}$/;
        let streetReg = /^[a-zA-Z0-9 -.,]{2,40}$/;
        let cityReg = /^[a-zA-Z -]{2,50}$/
        let countryReg = /^[a-zA-Z -]{2,20}$/
        let emailReg = /^[a-zA-Z0-9-.,]{7,25}@[a-zA-Z0-9 -.,]{7,25}$/


        if (!validate(formData.name, nameReg) || !validate(formData.surname, nameReg))
            throw "Make sure you entered a valid name or surname.";
        
        if (!validate(formData.email, emailReg))
            throw "Make sure you entered a valid email address."

        if (!validate(formData.phoneNumber, numReg))
            throw "Make sure you entered a valid phone number."

        if (!validate(formData.country, countryReg))
            throw "Make sure you entered a valid country name.";

        if (!validate(formData.city, cityReg))
            throw "Make sure you entered a valid city name.";

        if (!validate(formData.street, streetReg))
            throw "Make sure you entered a valid street name.";

        if (!validate(formData.password, passwordReg) || formData.password != formData.confirmPassword)
            throw "Make sure your confirmation password is the same as the new password and they're at least 6 characters long";
    }

    function validateSpecRegForm(formData) {
        if (formData.userType === "") 
            throw "Make sure you selected the account type you wish to make."

        if (formData.regReason.length < 10)
            throw "Make sure you provide a good reason for making this account (at least 10 characters long)."
    }   

    function validate(toTest, regex) {
        return regex.test(toTest)
    }
</script>


<style scoped>
    label {
        font-size: 12px;
        margin-bottom: 0;
    }

    .tabs-nav {
        margin-top: 50px;
    }

    .tab-btn {
        width: 600px;
        height: 40px;
        border: 0px;
        background-color: rgb(123, 206, 171);
        font-size: 20px;
        font-weight: bold;
        font-variant: small-caps;
    }

    .left { 
        right: 0;
        border-radius: 0 0 0 100px;
    }

    .right {
        left: 0;
        border-radius: 0 0 100px 0;
    }

    .toggled {
        background-color: rgb(181, 235, 212);
    }

    #form-style {
        background: white;
        text-align: left;
        margin-left: 0;
        margin-right: 0;
    }

    #form-cont {
        width: 98vw;
    }

    .split {
        padding: 0 40px;
    }

    #reg-btn {
        margin-top: 60px;
        border-radius: 20px;
        background-color: rgb(10, 241, 99);
        width: 200px;
        font-weight: bold;
    }

    #reg-btn:hover {
        background-color: rgba(10, 241, 99, 80%);
    }

    #img-prev {
        height: 125px;
        width: 125px;
        border-radius: 30px;
    }

    #cottage-owner-lbl {
        margin-right: 5px;
    }

    #boat-owner-lbl {
        margin-right: 5px;
        margin-left: 5px;
    }

    #instructor-lbl {
        margin-left: 5px;
    }

    #reg-reason {
        display: block;
        padding: 10px 6px;
        width: 100%;
        box-sizing: border-box;
        border: none;
        border-bottom: 1px solid #ddd;
        color: #555;
        max-height: 115px;
        min-height: 115px;
    }
</style>
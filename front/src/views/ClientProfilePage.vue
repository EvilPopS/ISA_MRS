<template>
  <form @submit.prevent="handleSubmit">
        <img id="profilePic" @click="changeProfilePhoto()"  :src="setProfPic()" alt="Client profile picture should be here...">

        <label>Account type: </label>
        <input disabled type="text" v-model="type">

        <label>Number of accumulated loyalty points: </label>
        <input disabled type="text" v-model="points">

        <label>Loyalty prpgram status: </label>
        <input disabled type="text" v-model="loyalty">

        <label>Email: </label>
        <input disabled type="text" v-model="email">
        
        <label>Name: </label>
        <input type="text" v-model="name">

        <label>Surname: </label>
        <input type="text" v-model="surname">

        <label>City: </label>
        <input type="text" v-model="city">

        <label>Zipcode: </label>
        <input type="text" v-model="zipcode">

        <label>Street: </label>
        <input type="text" v-model="street">

        <label>Phone number: </label>
        <input type="text" v-model="phoneNumber">

        <label>Password: </label>
        <input type="password" v-model="password">

        <label>Confirm Password: </label>
        <input type="password" v-model="confirmPassword">

        <div class="submit">
            <button id="subBtn">Update personal data</button>
        </div>
    </form>

    <PopUp v-show="picPopUpVisible" @close="closePopUp();">
        <div id="popupCont">
            <label id="popupLabel">Choose new profile picture: </label>
            <input ref="picInp" class="form-control form-control-sm" type="file" accept="image/*" @change="previewPic();"/>
            <img id="imgPreview" :src="require('../assets/' + profilePicture)"/>
            
            <div class="reset">
                <button id="resPicBtn" @click="resetPicPreview();">Reset</button>
                <button id="confirmBtn" @click="closePopUp();">Confirm</button>
            </div>
        </div>
    </PopUp>

    <PopUp v-show="errorPopUpVisible" @close="closePopUp();"> 
        <div id="errorCont">
            <div id="errMess">{{errMessage}}</div>
            <br>
            <button id="closeErrPopUpBtn" @click="closePopUp();">X</button>
        </div>
    </PopUp>
    
    <PopUp v-show="succPopUpVisible" @close="closeSuccPopUp();"> 
        <div id="successCont">
            <div id="succMess">{{succMessage}}</div>
            <br>
            <button id="closeSuccPopUpBtn" @click="closeSuccPopUp();">X</button>
        </div>
    </PopUp>
</template>

<script>
    import PopUp from "../components/PopUp.vue";
    import axios from 'axios';
    
    export default {
        name: "ClientProfilePage",
        components: {
            PopUp
        },
        data() {
            return {
                email: '',
                password: '',
                confirmPassword: '',
                name: '',
                surname: '',
                city: '',
                zipcode: '',
                street: '',
                phoneNumber: '',
                profilePicture: 'logo.png',
                type: '',
                loyalty: '',
                points: '',

                picPopUpVisible: false,
                errorPopUpVisible: false,
                succPopUpVisible: false,
                errMessage: '',
                succMessage: '',

                currentProfilePic: '',
                currentPassword: '',
                currentName: '',
                currentSurname: '',
                currentCity: '',
                currentZipcode: '',
                currentStreet: '',
                currentPhoneNumber: '',
                
            }
        },
        methods: {
            handleSubmit(){
                try { validateForm(this); } 
                catch(error) {        
                    console.log(error);        
                    this.errMessage = error;
                    this.errorPopUpVisible = true; 
                    return;
                }

                let requestBody = {
                    email: this.email,
                    password: this.password,
                    name: this.name,
                    surname: this.surname,
                    city: this.city,
                    zipcode: this.zipcode,
                    street: this.street,
                    phoneNumber: this.phoneNumber,
                    profilePicture: this.profilePicture
                }
                axios.put("http://localhost:8080/api/client/data-update", requestBody)
                    .then(() => {
                        this.succMessage = "Azuriranje vasih licnih podatak je uspesno izvrseno!";
                        this.succPopUpVisible = true;
                    });
            },
            
            changeProfilePhoto() {
                this.picPopUpVisible = true;
            },
            closePopUp() {
                this.picPopUpVisible = false;
                this.errorPopUpVisible = false;
            },
            previewPic() {
                this.profilePicture = this.$refs.picInp.value.split("\\")[2]; 
            },
            resetPicPreview() {
                this.profilePicture = this.currentProfilePic; 
            },
            setProfPic() {
                try{
                    return require('../assets/' + this.profilePicture);
                } catch(e) {}
            },
            closeSuccPopUp() {
                this.succPopUpVisible = false;
                this.$router.go(); 
            }

        },
        props: ['picInp'],
        mounted() {
            axios.get("http://localhost:8080/api/client/" + "strahinjapopovic@gmail.com")
                .then((response) => {
                    let data = response.data;

                    this.email = data.email;
                    this.password = data.password;
                    this.confirmPassword = data.password;
                    this.name = data.name;
                    this.surname = data.surname;
                    this.city = data.city;
                    this.zipcode = data.zipcode;
                    this.street = data.street;
                    this.phoneNumber = data.phoneNumber;
                    this.profilePicture = data.profilePicture;
                    this.type = data.userType;
                    this.loyalty = data.loyaltyStatus;
                    this.points = data.loyaltyPoints;
                    
                    this.currentPassword = data.password;
                    this.currentName = data.name;
                    this.currentSurname = data.surname;
                    this.currentCity = data.city;
                    this.currentZipcode = data.zipcode;
                    this.currentStreet = data.street;
                    this.currentPhoneNumber =data.phoneNumber;
                    this.currentProfilePic =data.profilePicture;
                })
        }
    }

    function validateForm(formData) {
        let nameReg = /^[a-zA-Z ]{2,30}$/;
        let passwordReg = /^[a-zA-Z0-9!@#$%^&*()_+-]{6,30}$/;
        let numReg = /^[0-9]+$/;
        let streetReg = /^[a-zA-Z0-9 -.,]+$/;
        let cityReg = /^[a-zA-Z -]{2,50}$/

        if (!validate(formData.name, nameReg) || !validate(formData.surname, nameReg))
            throw "Make sure you entered a valid name or surname.";

        if(!validate(formData.city, cityReg))
            throw "Make sure you entered a valid city name!";
        
        if (!validate(formData.zipcode, numReg))
            throw "Make sure you entered a valid zipcode.";
        
        if (!validate(formData.street, streetReg))
            throw "Make sure you entered a valid street name.";
        
        if (!validate(formData.phoneNumber, numReg))
            throw "Make sure your entered valid phone number.";
        
        if (!validate(formData.password, passwordReg) || formData.password != formData.confirmPassword)
            throw "Make sure your confirmation password is the same as the new password and they're at least 6 characters long";


        if (formData.currentPassword === formData.password &&
                formData.currentName === formData.name &&
                formData.currentSurname === formData.surname &&
                formData.currentCity === formData.city &&
                formData.currentZipcode === formData.zipcode &&
                formData.currentStreet ===formData.street &&
                formData.currentPhoneNumber === formData.phoneNumber &&
                formData.currentProfilePic === formData.profilePicture)
            throw "No changes made to form, please make sure you changed at least one editable field!";
    }

    function validate(toTest, regex) {
        return regex.test(toTest)
    }


</script>


<style>
    #imgPreview:hover {
        border-radius: 5px;
        width: 35%;
        height: 35%;
    }

    #imgPreview {
        border: solid 1px black;
        border-radius: 50px;
        margin-top: 5%;
        width: 30%;
        height: 30%;
        padding: 5px;
    }

    #popupCont {
        padding: 10px;
    }

    #popupLabel {
        font-size: 20px;
        color: rgb(45, 70, 56);
    }

    #profilePic {
        border-radius: 40px;
        width: 170px;
        height: 170px;
        display: block;
        margin: 0 auto;
        padding: 10px;
    }

    #profilePic:hover {
        width: 200px;
        height: 200px;
        border-radius: 20px;
        background: linear-gradient(rgb(255, 253, 253), rgb(241, 239, 239));
    }

    #subBtn:hover {
        color: #fff;
        background-color: #0a7706;
        border-color: #d6d30a;
        font-weight: bold;
    }

    form{
        max-width: 420px;
        margin: 30px auto;
        background: white;
        text-align: left;
        padding: 40px;
        border-radius: 10px;
    }

    label {
        color: #aaa;
        display: inline-block;
        margin: 25px 0 15px;
        font-size: 0.6rem;
        text-transform: uppercase;
        letter-spacing: 1px;
        font-weight: bold;
    }
    input, select {
        display: block;
        padding: 10px 6px;
        width: 100%;
        box-sizing: border-box;
        border: none;
        border-bottom: 1px solid #ddd;
        color: #555
    }
    input[type="checkbox"]{
        display: inline-block;
        width: 16px;
        margin: 0 10px 0 0;
        position: relative;
        top: 2px
    }
    .pill {
        display: inline-block;
        margin: 20px 10px 0 0;
        padding: 6px 12px;
        background: #eee;
        border-radius: 20px;
        font-size: 12px;
        letter-spacing: 1px;
        font-weight: bold;
        color: #777;
        cursor: pointer;
    }
    .submit button {
        background: blue;
        border: 0;
        padding: 10px 20px;
        margin-top: 20px;
        color: white;
        border-radius: 20px;
    }
    .submit {
        text-align: center;
    }

    .reset {
        text-align: center
    }
    .reset button {
        border: 0;
        padding: 10px 20px;
        margin-top: 20px;
        color: white;
        border-radius: 20px;
        margin: 15px 10px;
    }

    #resPicBtn {
        background: rgb(226, 50, 19);
    }

    #confirmBtn {
        background: rgb(1, 51, 22);
    }   

    .reset button:hover {
        background: rgba(218, 214, 214, 70%);
        color: black;
    }

    .error{
        color: red;
        margin-top: 10px;
        font-size: 0.8rem;
        font-weight: bold;
    }

    #errorCont, #successCont {
        align-content: center;
        padding:20px;
    }

    #errMess {
        color: red;
        font-size: 20px;
        font-weight: bold;
        margin-top: 20%;
    }

    #closeErrPopUpBtn, #closeSuccPopUpBtn {
        border: 0;
        padding: 10px 20px;
        margin-top: 20px;
        color: rgb(190, 22, 22);
        border-radius: 20px;
        margin: 15px 10px;
    }

    #closeErrPopUpBtn:hover, #closeSuccPopUpBtn:hover{
        background: rgba(218, 214, 214, 70%);
        color: black;
    }

    #succMess {
        color: rgb(4, 87, 22);
        font-size: 20px;
        font-weight: bold;
        margin-top: 20%;
    }
</style>
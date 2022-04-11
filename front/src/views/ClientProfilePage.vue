<template>
  <form @submit.prevent="handleSubmit">
        <img id="profilePic" @click="changeProfilePhoto()"  :src="require('../assets/' + 'logo.png')" alt="Client profile picture should be here...">

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

    <PopUp v-show="popVisible" @close="closePopUp();">
        <input v-model="phoneNumber"/>
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
                profilePicture: '',
                type: '',
                loyalty: '',
                points: '',
                popVisible: false
                
            }
        },
        methods: {
            handleSubmit(){
                try {
                    validateForm(this);
                } catch(error) {
                    console.log(error);
                }
            },
            changeProfilePhoto() {
                this.popVisible = true;
            },
            closePopUp() {
                this.popVisible = false;
            }
        },
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
                    })
        }

    }

    function validateForm(formData) {
        // Dodati proveru za adresu 
        let nameReg = /^[a-zA-Z ]{2,30}$/;
        let passwordReg = /^[a-zA-Z0-9!@#$%^&*()_+-]{6,30}$/;
        let phoneReg = /^[0-9]{5,20}$/;

        if (!validate(formData.name, nameReg) || !validate(formData.surname, nameReg))
            throw "Invalid name or surname.";
        if (!validate(formData.password, passwordReg) || formData.password != formData.confirmPassword)
            throw "Make sure your confirmation password is the same as the new password and they're at least 6 characters long";
        if (!validate(formData.phoneNumber, phoneReg))
            throw "Make sure your entered valid phone number."
    }

    function validate(toTest, regex) {
        return regex.test(toTest)
    }


</script>


<style>
    #profilePic {
        border-radius: 40px;
        width: 150px;
        height: 150px;
        display: block;
        margin: 0 auto;
    }

    #profilePic:hover {
        width: 200px;
        height: 200px;
        border-radius: 20px;
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
    .error{
        color: red;
        margin-top: 10px;
        font-size: 0.8rem;
        font-weight: bold;
    }
</style>
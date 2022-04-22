<template>
  <form id="form-style" @submit.prevent="emitSubmit()">
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
            <img id="imgPreview" :src="require('@/assets/' + profilePicture)"/>
            
            <div class="reset">
                <button id="resPicBtn" @click="resetPicPreview();">Reset</button>
                <button id="confirmBtn" @click="closePopUp();">Confirm</button>
            </div>
        </div>
    </PopUp>

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
    import PopUp from "@/components/PopUp.vue"
    import ErrorPopUp from "@/components/ErrorPopUp.vue"
    import SuccessPopUp from "@/components/SuccessPopUp.vue"

    export default {
        name: "UserEditProfileForm",
        components: {
            PopUp,
            ErrorPopUp,
            SuccessPopUp
        },
        props :  {
            succPopUpVisible: Boolean,

            profilePicture : String,
            email : String,
            password : String,
            confirmPassword : String,
            name : String,
            surname : String,
            city : String,
            zipcode : String,
            street : String,
            phoneNumber : String,
            type : String,
            points : String,
            loyalty : String,

            currentProfilePic: String,
            currentPassword: String,
            currentName: String,
            currentSurname: String,
            currentCity: String,
            currentZipcode: String,
            currentStreet: String,
            currentPhoneNumber: String,
        },
        data() {
            return {
                picPopUpVisible: false,
                errMessage : '',
                errorPopUpVisible: false,
                succMessage: "Personal information has been changed successfully!"
            }
        },
        methods: {
            changeProfilePhoto() {
                this.picPopUpVisible = true;
            },
            closePopUp() {
                this.picPopUpVisible = false;
                this.errorPopUpVisible = false;
            },
            previewPic() {
                this.$emit("set-new-profile-pic", this.$refs.picInp.value.split("\\")[2]);
            },
            resetPicPreview() {
                this.$refs.picInp.value = "";
                this.$emit("set-new-profile-pic", this.currentProfilePic);
            },
            setProfPic() {
                try{
                    return require('@/assets/' + this.profilePicture);
                } catch(e) {}
            },
            closeSuccPopUp() {
                this.$emit("succ-popup-close");
                this.$router.go(); 
                
            },
            emitSubmit() {
                try { validateForm(this); } 
                catch(error) {        
                    this.errMessage = error;
                    this.errorPopUpVisible = true; 
                    return;
                }

                this.$emit('handle-submit', {
                    profilePicture: this.profilePicture,
                    email: this.email,
                    password: this.password,
                    confirmPassword: this.confirmPassword,
                    name: this.name,
                    surname: this.surname,
                    city: this.city,
                    zipcode: this.zipcode,
                    street: this.street,
                    phoneNumber: this.phoneNumber
                });
            }
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
                formData.currentStreet === formData.street &&
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

    #form-style {
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

    .submit button {
        background: rgb(3, 94, 30);
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
</style>
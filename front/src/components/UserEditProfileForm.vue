<template>
  <form @submit.prevent="">
      <div class="acc-info-cont">
            <div class="row">

                <div id="left-col" class="col">
                    <div class="inline-inputs">
                        <label style="display: block">Email: </label>
                        <input disabled type="text" class= "form-control" v-model="email">
                    </div>
                    
                    <div v-if="userType !== 'admin'">
                        <div class="inline-inputs">
                            <label style="display: block">Loyalty points: </label>
                            <input disabled type="text" class= "form-control" v-model="points">
                        </div>
                    </div>

                    <div class="inline-inputs">
                        <label>Name: </label>
                        <input type="text" class= "form-control" v-model="name">        
                    </div>

                    <div class="inline-inputs">
                        <label>Surname: </label>
                        <input type="text" class= "form-control" v-model="surname">
                    </div>     

                    <div class="inline-inputs">
                        <label>Phone number: </label>
                        <input type="text" class= "form-control" v-model="phoneNumber">
                    </div>          
                </div>

                <div class="col">
                    <div class="row">
                        <div class="d-flex justify-content-center">
                            <img id="profilePic" @click="changeProfilePhoto()" :src="setProfPic()" alt="Client profile picture should be here...">
                        </div>    
                    </div>

                    <div class="inline-inputs">
                        <label>Password: </label>
                        <input type="password" class= "form-control" v-model="password">
                    </div>

                    <div class="inline-inputs">
                        <label>Confirm Password: </label>
                        <input type="password" class= "form-control" v-model="confirmPassword">
                    </div>
    
                    <div class="submit">                    
                        <button id="sub-btn" @click="emitSubmit()">Update personal data</button>
                    </div>
                    <button id="del-btn" class="btn btn-danger" @click="deleteClicked()">Delete my account</button>      
                </div>

                <div id="right-col" class="col">
                    <div class="inline-inputs">
                        <label style="display: block">Type: </label>
                        <input disabled type="text" class= "form-control" v-model="type">
                    </div>

                    <div v-show="userType !== 'admin'">
                        <div class="inline-inputs">
                            <label>Loyalty program status: </label>
                            <input disabled type="text" class= "form-control" v-model="loyalty">
                        </div>
                    </div>

                    <div class="inline-inputs">
                        <label>Country: </label>
                        <input type="text" class= "form-control" v-model="country">
                    </div>

                    <div class="inline-inputs">
                        <label>City: </label>
                        <input type="text" class= "form-control" v-model="city">
                    </div>
                    <div class="inline-inputs">
                        <label>Street: </label>
                        <input type="text" class= "form-control" v-model="street">
                    </div>
                </div>
            </div>
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

    <MessageInputModal  v-if="showMessageModal" 
        @message-modal-closed="showMessageModal = false"
        @delete-request-sent="sendRequestToAdmin"
    />
</template>

<script>
    import PopUp from "@/components/PopUp.vue"
    import ErrorPopUp from "@/components/ErrorPopUp.vue"
    import SuccessPopUp from "@/components/SuccessPopUp.vue"
    import MessageInputModal from '@/components/MessageInputModal.vue'

    export default {
        name: "UserEditProfileForm",
        components: {
            PopUp,
            ErrorPopUp,
            SuccessPopUp,
            MessageInputModal
            

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
            country : String,
            street : String,
            phoneNumber : String,
            type : String,
            points : String,
            loyalty : String,

            currentProfilePic: String,
            currentName: String,
            currentSurname: String,
            currentCity: String,
            currentCountry: String,
            currentStreet: String,
            currentPhoneNumber: String,
        },
        data() {
            return {
                picPopUpVisible: false,
                errMessage : '',
                errorPopUpVisible: false,
                succMessage: "",

                userType: window.localStorage.getItem("userRole"),

                showMessageModal : false
            }
        },
        methods: {
            sendRequestToAdmin(reasonMessage){
                const dtNow = new Date();
                
                const deletitonRequest = {
                    message : reasonMessage,
                    sentTime : [dtNow.getDate(), (dtNow.getMonth()+1), dtNow.getFullYear()].join("/") + 
                                                " " +
                                                [dtNow.getHours(), dtNow.getMinutes()].join(":")
                }
                this.showMessageModal = false;
                this.succMessage = "Request for deletion of you account has been sent successfully!";
                this.$emit('delete-request-sent', deletitonRequest);
            },

            deleteClicked(){
                this.showMessageModal = true;
            },

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
                this.succMessage = "Personal information has been changed successfully!";

                this.$emit('handle-submit', {
                    profilePicture: this.profilePicture,
                    email: this.email,
                    password: this.password,
                    name: this.name,
                    surname: this.surname,
                    city: this.city,
                    country: this.country,
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
        
        if (!validate(formData.country, cityReg))
            throw "Make sure you entered a valid country name.";
        
        if (!validate(formData.street, streetReg))
            throw "Make sure you entered a valid street name.";
        
        if (!validate(formData.phoneNumber, numReg))
            throw "Make sure your entered valid phone number.";
        
        if ( formData.password !== "" && !validate(formData.password, passwordReg) || formData.password != formData.confirmPassword)
            throw "Make sure your confirmation password is the same as the new password and they're at least 6 characters long";

        if (formData.password === "" &&
                formData.currentName === formData.name &&
                formData.currentSurname === formData.surname &&
                formData.currentCity === formData.city &&
                formData.currentCountry === formData.country &&
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
    .acc-info-cont {
        margin: 0 50px;
    }

    .upper-line {
        margin-bottom: 0;
    }

    #form-style {
        max-width: 420px;
        margin: 30px auto;
        background: white;
        text-align: left;
        padding: 40px;
        border-radius: 10px;
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

    #imgPreview:hover {
        border-radius: 5px;
        width: 170px;
        height: 170px;
        margin-bottom: 0px;
    }

    #imgPreview {
        border: solid 1px black;
        border-radius: 50px;
        margin-top: 5%;
        margin-bottom: 20px;
        width: 150px;
        height: 150px;
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
        width: 200px;
        height: 200px;
        display: block;
        margin-bottom: 10px;
        padding: 10px;
    }

    #profilePic:hover {
        margin-bottom: 0;
        width: 210px;
        height: 210px;
        border-radius: 20px;
        background: linear-gradient(rgb(255, 253, 253), rgb(241, 239, 239));
    }

    #sub-btn {
        margin-top: 35px;
    }

    #sub-btn:hover {
        color: #fff;
        background-color: #0a7706;
        border-color: #d6d30a;
        font-weight: bold;
    }

    #del-btn {
        border-radius: 30px;
        margin-top: 35px;
    }

    label {
        color: #aaa;
        display: inline-block;
        margin: 25px 0 15px;
        font-size: 0.6rem;
        text-transform: uppercase;
        letter-spacing: 1px;
        font-weight: bold;
        font-size: 14px;
    }

    .submit button {
        background: rgb(3, 94, 30);
        border: 0;
        padding: 10px 20px;
        color: white;
        border-radius: 20px;
        margin-bottom: 5%;
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

    /*Ovo je h3 za don't have permision da se vidi ispod navigacije, da bude global */
    h3 {
        margin-top: 10%;
    }

</style>

<style scoped>

    div.status{
        font-size: 20px;
        align-content: center;
        text-align: center;
        margin-bottom: 3%;
        margin-top: 5%;
    }

    div.row {
        margin-top: 5%;
    }

    .inline-inputs {
        display: inline-block;
    }

    #right-col {
        margin-right: 320px;
    }

    #left-col {
        margin-left: 320px;
    }
</style>
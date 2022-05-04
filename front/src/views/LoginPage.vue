<template>
    <div class="row align-items-center form-cont">
        <div class="col"></div>
        <div class="col">
            <form id="form-style">
                <div class="row form-caption">
                    <p>Welcome to login page!</p>
                </div>
                <div class="row">
                    <label>Email: </label>
                    <input type="text" v-model="email">
                </div>
                <div class="row">
                    <label>Password: </label>
                    <input type="password" v-model="password">
                </div>
                <div class="row">
                </div>
            </form>    
            <button id="log-btn" @click="submitLogin()">LOGIN</button>
        </div>
        <div class="col"></div>
    </div>

    <ErrorPopUp v-show="errorPopUpVisible" 
        @close = closePopUp
        :mess = errMessage
    /> 
</template>

<script>
    import ErrorPopUp from "@/components/ErrorPopUp.vue"; 
    import axios from 'axios';

    export default {
        name: "LoginPage",
        components: {
            ErrorPopUp
        },
        data() {
            return {
                email: "",
                password: "",

                errMessage: "",
                errorPopUpVisible: false,
            };
        },
        methods: {
            closePopUp() {
                this.errorPopUpVisible = false;
            },
            submitLogin() {
                try {
                    validateForm(this);
                } catch (error) {
                    this.errMessage = error;
                    this.errorPopUpVisible = true;
                    return;
                }

                let requestBody = {
                    email: this.email,
                    password: this.password
                }

                axios.post("api/user/login", requestBody).then(response => {
                    window.sessionStorage.setItem("email", this.email);
                    let userType = response.data;
                    if (userType === "client") 
                        this.$router.push({ name: "ClientProfilePage" });
                    else if (userType === "cottageOwner")
                        this.$router.push({ name: "CottageOwnerHomePage" });
                    // else if (userType === "boatOwner")
                    //     this.$router.push({ name: viewName });
                    else if (userType === "instructor")
                        this.$router.push({ name: "InstructorProfilePage" });

                }).catch(err => {
                    if (err.response.status === 401) {
                        this.errMessage = "You put in wrong username or password, please try again.";
                        this.errorPopUpVisible = true;
                    }
                });
            }
        }
    }

    function validateForm(formData) {
        let emailReg = /^[a-zA-Z0-9-.,]{7,25}@[a-zA-Z0-9 -.,]{7,25}$/
        let passwordReg = /^[a-zA-Z0-9!@#$%^&*()_+-]{6,30}$/;

        if (!validate(formData.email, emailReg))
            throw "Make sure you entered a valid email address."

        if (!validate(formData.password, passwordReg))
            throw "Make sure entered a valid password.";

    }

    function validate(toTest, regex) {
        return regex.test(toTest)
    }
</script>

<style scoped>
    #form {
        margin-left: 0;
        margin-right: 0;
        background: white;
    }

    label {
        font-size: 12px;
    }

    .form-cont {
        height: 99vh;
        width: 100%;
    }

    input {
        width: 100%;
    }

    .form-caption {
        margin-bottom: 30px
    }

    #form-style div p {
        font-family: "Palatino Linotype", "Book Antiqua", Palatino, serif;
        font-size: 25px;
        letter-spacing: 2px;
        word-spacing: 2px;
        color: #4c8126;
        font-weight: 700;
        text-decoration: none;
        font-style: normal;
        font-variant: small-caps;
        text-transform: none;
    }

    #log-btn {
        margin: 20px 0;
        width: 150px;
        height: 35px;
        border-radius: 20px;
        font-weight: bold;
        font-size: 17px;
        background-color: rgb(5, 143, 58);
        color: white;
    }

    #log-btn:hover {
        margin: 17px 0;
        width: 160px;
        height: 41px;
        font-size: 18px;
    }
</style>

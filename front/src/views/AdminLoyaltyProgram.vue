<template>
  <div class="row">
    <div class="col-md-4 mr-10">
        <h3> BRONZE LOYALTY PROGRAM </h3>
        <div class="d-flex justify-content-center">
            <img id="profilePic" src="../assets/bronze3.png" alt="Bronze loyalty program picture should be here...">
        </div>
        <div class="row">
            <div class="col-md-3 ms-5">
                <label>Discount:</label>
                <input class="form-control" type="number" v-model="bronzeProgram.discount"/>
            </div>
            <div class="col-md-3 ms-5">
                <label>Increase:</label>
                <input class="form-control" type="number" v-model="bronzeProgram.increase"/>
            </div>
            <div class="col-md-3 ms-5">
                <label>Price:</label>
                <input class="form-control" type="number" v-model="bronzeProgram.price"/>
            </div>
            
        </div>
    </div>
    <div class="col-md-4 mr-10">
        <h3> SILVER LOYALTY PROGRAM </h3>
        <div class="d-flex justify-content-center">
            <img id="profilePic" src="../assets/silver2.png" alt="Bronze loyalty program picture should be here...">
        </div>
        <div class="row">
            <div class="col-md-3 ms-5">
                <label>Discount:</label>
                <input class="form-control" type="number" v-model="silverProgram.discount"/>
            </div>
            <div class="col-md-3 ms-5">
                <label>Increase:</label>
                <input class="form-control" type="number" v-model="silverProgram.increase"/>
            </div>
            <div class="col-md-3 ms-5">
                <label>Price:</label>
                <input class="form-control" type="number" v-model="silverProgram.price"/>
            </div>
        </div>
    </div>
    <div class="col-md-4 mr-10">
        <h3> GOLD LOYALTY PROGRAM </h3>
        <div class="d-flex justify-content-center">
            <img id="profilePic" src="../assets/goldd.png" alt="Bronze loyalty program picture should be here...">
        </div>
        <div class="row">
            <div class="col-md-3 ms-5">
                <label>Discount:</label>
                <input class="form-control" type="number" v-model="goldProgram.discount" />
            </div>
            <div class="col-md-3 ms-5">
                <label>Increase:</label>
                <input class="form-control" type="number" v-model="goldProgram.increase"/>
            </div>
            <div class="col-md-3 ms-5">
                <label>Price:</label>
                <input class="form-control" type="number" v-model="goldProgram.price"/>
            </div>
        </div>
    </div>
  </div>
  <div class="row align-center">
    <div class="col aling-self-center">
        <button class="btn btn-success btn-lg" @click="emitSubmit"> Configure loyalty program </button>
    </div>
  </div>
  <ErrorPopUp v-show="errorPopUpVisible" 
            @close = closePopUp
            :mess = errMessage
        />
    <SuccessPopUp v-show="succPopUpVisible"
        @close = "succPopUpVisible = false"
        :mess = succMessage
    /> 
  
    
</template>

<script>
    import axios from 'axios'
    import PopUp from "@/components/PopUp.vue"
    import ErrorPopUp from "@/components/ErrorPopUp.vue"
    import SuccessPopUp from "@/components/SuccessPopUp.vue"
    import MessageInputModal from '@/components/MessageInputModal.vue'

    export default {
        name: "AdminLoyaltyProgram",
        components: {
            PopUp,
            ErrorPopUp,
            SuccessPopUp,
            MessageInputModal
            

        },
    
        data() {
            return {
                picPopUpVisible: false,
                errMessage : '',
                errorPopUpVisible: false,
                succMessage: "",
                succPopUpVisible : false,

                programs :[],

                userType: window.localStorage.getItem("userRole"),

                showMessageModal : false,

                silverProgram : {
                    loyaltyType : 'SILVER',
                    discount : '',
                    increase : '',
                    price : ''
                },
                bronzeProgram : {
                    loyaltyType : 'BRONZE',
                    discount : '',
                    increase : '',
                    price : ''
                },
                goldProgram : {
                    loyaltyType : 'GOLD',
                    discount : '',
                    increase : '',
                    price : ''
                },
                
            }
        },
        methods: {
            

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
                try { validateForm(this.bronzeProgram, this.silverProgram, this.goldProgram); } 
                catch(error) {        
                    this.errMessage = error;
                    this.errorPopUpVisible = true; 
                    return;
                }
                console.log("RADIIIIIIIIIIIIIIIIIIi");
                console.log(window.localStorage.getItem("token"));


                axios.post('api/admin/configure-loyalty-program',[this.bronzeProgram, this.silverProgram, this.goldProgram] ,{headers: {'authorization': window.localStorage.getItem("token") }}).then((response) => {
                    this.succPopUpVisible = true;
                    this.succMessage = "Loyalty program has been successfully configured.";
                    for (let i = 0; i < response.data.length ; i++){
                        if (response.data[i].loyaltyType === "BRONZE"){
                            this.bronzeProgram.discount = response.data[i].discount;
                            this.bronzeProgram.increase = response.data[i].increase;
                            this.bronzeProgram.price    = response.data[i].price;
                        }else if (response.data[i].loyaltyType === "SILVER"){
                            this.silverProgram.discount = response.data[i].discount;
                            this.silverProgram.increase = response.data[i].increase;
                            this.silverProgram.price    = response.data[i].price;

                        }else if (response.data[i].loyaltyType === "GOLD"){
                            this.goldProgram.discount = response.data[i].discount;
                            this.goldProgram.increase = response.data[i].increase;
                            this.goldProgram.price    = response.data[i].price;
                        }
                    }
                                        


                }).catch((e) => {
                    this.errMessage = e;
                    this.errorPopUpVisible = true;
    });
            }
        },
        mounted (){
            axios.get('api/admin/get-loyalty-program',{headers: {'authorization': window.localStorage.getItem("token") }}).then((response) => {
                    // this.succPopUpVisible = true;
                    // this.succMessage = "Loyalty program has been successfully configured.";
                    console.log('sadada');
                    this.programs = response.data;
                    for (let i = 0; i < response.data.length ; i++){
                        if (response.data[i].loyaltyType === "BRONZE"){
                            this.bronzeProgram.discount = response.data[i].discount;
                            this.bronzeProgram.increase = response.data[i].increase;
                            this.bronzeProgram.price    = response.data[i].price;
                        }else if (response.data[i].loyaltyType === "SILVER"){
                            this.silverProgram.discount = response.data[i].discount;
                            this.silverProgram.increase = response.data[i].increase;
                            this.silverProgram.price    = response.data[i].price;

                        }else if (response.data[i].loyaltyType === "GOLD"){
                            this.goldProgram.discount = response.data[i].discount;
                            this.goldProgram.increase = response.data[i].increase;
                            this.goldProgram.price    = response.data[i].price;
                        }
                    }
                                        


                }).catch((e) => {
                    this.errMessage = e;
                    this.errorPopUpVisible = true;
    });


        }
    }

    function validateForm(bronzeProgram, silverProgram, goldProgram) {
        let nameReg = /^[a-zA-Z ]{2,30}$/;
        let passwordReg = /^[a-zA-Z0-9!@#$%^&*()_+-]{6,30}$/;
        let numReg = /^[0-9]+$/;
        let streetReg = /^[a-zA-Z0-9 -.,]+$/;
        let cityReg = /^[a-zA-Z -]{2,50}$/

        

        // za discount
        if (bronzeProgram.discount >= silverProgram.discount)
            throw "Make sure your bronze discount is lower than silver discount.";

        if (silverProgram.discount <= bronzeProgram.discount || silverProgram.discount >= goldProgram.discount)
            throw "Make sure your silver discount is lower than gold and higher than bronze discount.";

        if (goldProgram.discount <= silverProgram.discount)
            throw "Make sure your gold discount is higher than silver discount.";

        // za increase
        if (bronzeProgram.increase >= silverProgram.increase)
            throw "Make sure your bronze increase is lower than silver increase.";

        if (silverProgram.increase <= bronzeProgram.increase || silverProgram.increase >= goldProgram.increase)
            throw "Make sure your silver increase is lower than gold and higher than bronze increase.";

        if (goldProgram.increase <= silverProgram.increase)
            throw "Make sure your gold increase is higher than silver increase.";
        // za price
        if (bronzeProgram.price >= silverProgram.price)
            throw "Make sure your bronze price is lower than silver price.";

        if (silverProgram.price <= bronzeProgram.price || silverProgram.price >= goldProgram.price)
            throw "Make sure your silver price is lower than gold and higher than bronze price.";

        if (goldProgram.price <= silverProgram.price)
            throw "Make sure your gold price is higher than silver price.";

        
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

    .loyalty-title {
        font-size: 1.25em;
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

    #loyaltyTitle {
        font-size: 10px;

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

    #renterTag {
        margin-top: 195px;  
        width: 100px;
    }

    #clientTag {
        margin-top : 242px;
        width: 100px;
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

    .two-col {
        overflow: hidden;/* Makes this div contain its floats */
    }

    .two-col .col1,
    .two-col .col2 {
        width: 33%;
    }

    .two-col .col1 {
        float: left;
    }

    .two-col .col2 {
        float: right;
    }

    .two-col label {
        display: block;
    }

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

    /* #left-col {
        margin-left: 5px;
    } */
</style>
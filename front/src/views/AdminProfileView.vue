<template>
    <UserEditProfileForm 
        :type                = this.type
        :profilePicture      = this.profilePicture
        :points              = this.points
        :loyalty             = this.loyalty
        :email               = this.email
        :password            = this.password
        :confirmPassword     = this.confirmPassword
        :name                = this.name
        :surname             = this.surname
        :phoneNumber         = this.phoneNumber
        :zipcode             = this.zipcode
        :city                = this.city
        :street              = this.street
        :currentProfilePic   = this.currentProfilePic
        :currentPassword     = this.currentPassword
        :currentName         = this.currentName
        :currentSurname      = this.currentSurname
        :currentCity         = this.currentCity
        :currentZipcode      = this.currentZipcode
        :currentStreet       = this.currentStreet
        :currentPhoneNumber  = this.currentPhoneNumber
        :succPopUpVisible    = this.succPopUpVisible
        @handle-submit       = "handleSubmit"
        @set-new-profile-pic = "setNewProfilePic"
        @succ-popup-close    = "succPopUpClose"
    
    />
</template>


<script>
    import UserEditProfileForm from "../components/UserEditProfileForm.vue"
    import axios from 'axios';
    
    export default {
        name: "AdminProfilePage",
       components: {
            UserEditProfileForm
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

                succPopUpVisible: false
            }
        },
        methods: {
            handleSubmit(data){
                let requestBody = {
                    email: data.email,
                    password: data.password,
                    name: data.name,
                    surname: data.surname,
                    city: data.city,
                    zipcode: data.zipcode,
                    street: data.street,
                    phoneNumber: data.phoneNumber,
                    profilePicture: data.profilePicture
                }
                axios.put("api/fishingInstructor/data-update", requestBody)
                    .then(() => {
                        this.succPopUpVisible = true;
                    });
            },
            setNewProfilePic(newPic) {
                this.profilePicture = newPic; 
            },
            succPopUpClose() {
                this.succPopUpVisible = false;
            }
        },
        created() {
            axios.get("api/admin/" + "admin1@gmail.com")
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
</script>
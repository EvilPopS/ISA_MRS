<template>
    <div class="topnav">
        <div v-if="userRole === 'unauth'">
            <a @click="mainHomePageRedirect()" class="homeNav">Home</a>
            <a @click="registrationPageRedirect()" class="homeNav">Register</a>
            <a @click="loginPageRedirect()" class="homeNav">Login</a>
            <a @click="searchPageRedirect()" class="homeNav">Search</a>
        </div>  

        <div v-else-if="userRole === 'admin'">
            <a @click="adminProfileRedirect()" class="homeNav">Profile</a>
            <a @click="AdminNotificationsRedirect()" class="homeNav">Notifications</a>
        </div>

        <div v-else-if="userRole === 'client'">
            <a @click="clientProfileRedirect()" class="homeNav">Profile</a>
        </div>

        <div v-else-if="userRole === 'cottageOwner'">
            <a @click="cottageOwnerHomeRedirect()" class="homeNav">Profile</a>
            <a @click="allCottagesRedirect()" class="homeNav">Cottages</a>
            <a @click="allReservationsRedirect()" class="homeNav">Reservations</a>
        </div>

        <div v-else-if="userRole === 'instructor'">
            <a @click="instructorProfilePageRedirect()" class="homeNav">Profile</a>
            <a @click="adventuresRedirect()" class="homeNav">Adventures</a>
        </div>
    </div>
    
</template>


<script>
    export default {
        name: "Navigation",
        data(){
            let role = "unauth";
            try {
                role = window.sessionStorage.getItem("userRole");
            } catch (e) {}
            return {
                userRole: role
            }
        },
        methods: {

            adminProfileRedirect : function () {
                pushView(this, "AdminProfilePage");
            },

            AdminNotificationsRedirect : function (){
                pushView(this, "AdminNotifications");
            },


            cottageOwnerHomeRedirect : function () {
                pushView(this, "CottageOwnerHomePage");
            },
            
            allCottagesRedirect: function() {
                pushView(this, "AllCottagesView")
            },
            
            clientProfileRedirect : function () {
                pushView(this, "ClientProfilePage");
            },
            
            instructorProfilePageRedirect : function () {
                pushView(this, "InstructorProfilePage");
            },
            
            cottagesRedirect : function () {
                pushView(this, "CottageManagmentPage");
            },

            allReservationsRedirect : function () {
                pushView(this, "AllReservations");
            },

            adventuresRedirect : function () {
                pushView(this, "AdventuresView");
            },

            mainHomePageRedirect : function () {
                pushView(this, "MainHomePage");
            },

            registrationPageRedirect : function() {
                pushView(this, "RegistrationPage");
            },

            loginPageRedirect : function() {
                pushView(this, "LoginPage");
            },
            
            searchPageRedirect: function() {
                pushView(this, "SearchPage");
            }
        }
    }
  
    function pushView(routerOwner, viewName) { 
        /* 
            Helper funckija ubacivanje View-eva na ruter pri kliku na link 
            (ponavalja se isti kod kroz sve metode u exportu)
        */  
        routerOwner.$router
            .push({ name: viewName })
            .catch((err) => {     
                console.error(err);
            });
    }
</script>

   
<style scoped>
    .topnav {
        background-color: rgba(15, 95, 72, 1);
        overflow: hidden;
        width: 100%;
        position: fixed;
        top: 0;
        z-index: 999;
        height: 50px;
    }
    .topnav a {
        float: left;
        color: #f2f2f2;
        text-align: center;
        padding: 14px 16px;
        text-decoration: none;
        font-size: 17px;
        z-index: 999;
        max-height: 100%;
    }
    .topnav a:hover {
        background-color: rgb(8, 250, 177);
        color: rgba(51, 92, 80, 0.8);
    }
    .topnav a.active {
        background-color: #272327;
        color: white;
    }
    #logoutNav {
        float: right;
    }
</style>
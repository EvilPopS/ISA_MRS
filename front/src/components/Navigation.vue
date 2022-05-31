<template>
    <div class="topnav">
        <div v-if="userRole === 'UNAUTH'">
            <a @click="mainHomePageRedirect()" class="homeNav">Home</a>
            <a @click="searchPageRedirect()" class="homeNav">Search</a>
            <a @click="registrationPageRedirect()" class="homeNav">Register</a>
            <a @click="loginPageRedirect()" class="homeNav">Login</a>
        </div>  

        <div v-else-if="userRole === 'ADMIN'">
            <a @click="adminProfileRedirect()" class="homeNav">Profile</a>
            <a @click="AdminNotificationsRedirect()" class="homeNav">Notifications</a>
        </div>

        <div v-else-if="userRole === 'CLIENT'">
            <a @click="clientProfileRedirect()" class="homeNav">Profile</a>
            <a @click="searchPageRedirect()" class="homeNav">Search</a>
            <a @click="clientReservHistoryRedirect()" class="homeNav">Reservation History</a>
            <a @click="clientUpcomingReservsRedirect()" class="homeNav">Upcoming Reservations</a>
            <a @click="clientSubscriptionsViewRedirect()" class="homeNav">Subscriptions</a>
        </div>

        <div v-else-if="userRole === 'COTTAGE_OWNER'">
            <a @click="cottageOwnerHomeRedirect()" class="homeNav">Profile</a>
            <a @click="allCottagesRedirect()" class="homeNav">Cottages</a>
            <a @click="allReservationsRedirect()" class="homeNav">Reservations</a>
            <a @click="ownersSearch()" class="homeNav">Search</a>
        </div>

        <div v-else-if="userRole === 'INSTRUCTOR'">
            <a @click="instructorProfilePageRedirect()" class="homeNav">Profile</a>
            <a @click="adventuresRedirect()" class="homeNav">Adventures</a>
            <a @click="allReservationsRedirect()" class="homeNav">Reservations</a>
        </div>
    </div>
    
</template>


<script>
    export default {
        name: "Navigation",
        data(){
            let role = window.localStorage.getItem("userRole");
            role =  role === null ? "UNAUTH" : role;

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
            },
            ownersSearch: function() {
                pushView(this, "OwnersSearch");
            },
            clientReservHistoryRedirect: function() {
                pushView(this, "ClientReservationHistory");
            },
            clientUpcomingReservsRedirect: function() {
                pushView(this, "ClientUpcomingReservations");
            },
            clientSubscriptionsViewRedirect: function() {
                pushView(this, "ClientSubscriptionsView");
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
    .topnav div a {
        float: left;
        color: #f2f2f2;
        text-align: center;
        padding: 14px 16px;
        text-decoration: none;
        font-size: 17px;
        z-index: 999;
        max-height: 100%;
    }
    .topnav div a:hover {
        background-color: rgb(8, 250, 177);
        color: rgba(51, 92, 80, 0.8);
    }
    .topnav div a.active {
        background-color: #272327;
        color: white;
    }
    #logoutNav {
        float: right;
    }
</style>
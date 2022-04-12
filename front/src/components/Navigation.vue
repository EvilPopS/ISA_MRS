<template>

    <div class="topnav">
        
        <div v-if="isAdmin">
            <a @click="homeRedirect()" class="homeNav">Profile</a>
        </div>

        <div v-if="isClient">
            <a @click="clientProfileRedirect()" class="homeNav">Profile</a>
        </div>

        <div v-if="isCottageOwner">
            <a @click="cottageOwnerHomeRedirect()" class="homeNav">Profile</a>
            <a @click="homeRedirect()" class="homeNav">Cottages</a>
        </div>


    </div>
    
</template>

<script>
    export default {
        name: "Navigation",

        data(){
            return {
                isAdmin: false,
                isClient: false,
                isCottageOwner: true,
            }
        },
        
        methods: {
            cottageOwnerHomeRedirect : function () {
                pushView(this, "CottageOwnerHomePage");
            },

            clientProfileRedirect : function () {
                pushView(this, "ClientProfilePage");
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
<template>
    <div id="home-page-cont">
        <div id="card-1" class="flip-card" @click="searchPageRedirect()">
            <div class="flip-card-inner">
                <div class="flip-card-front">
                    <p class="desc-txt">Need a holiday cottage?</p>
                </div>
                <div class="flip-card-back">
                    <img class="img-style" src="../assets/home-page-pic-cottage.jpg" alt="Pic of cottage" style="width:300px;height:300px;">
                </div>
            </div>
        </div>

        <div id="card-2" class="flip-card" @click="searchPageRedirect()">
            <div class="flip-card-inner">
                <div class="flip-card-front">
                    <p class="desc-txt">Or a fishing ship?</p>
                </div>
                <div class="flip-card-back">
                    <img class="img-style" src="../assets/home-page-pic-boat.jpg" alt="Pic of cottage" style="width:300px;height:300px;">
                </div>
            </div>
        </div>

        <div id="card-3" class="flip-card" @click="searchPageRedirect()">
            <div class="flip-card-inner">
                <div class="flip-card-front">
                    <p class="desc-txt">Perhaps some fishing lessons?</p>
                </div>
                <div class="flip-card-back">
                    <img class="img-style" src="../assets/home-page-pic-instructor.jpg" alt="Pic of cottage" style="width:300px;height:300px;">
                </div>
            </div>
        </div>

        <div id="card-4" class="flip-card">
            <div id="log-reg-card" class="flip-card-inner">
                <div class="flip-card-front" @click="registrationPageRedirect()">
                    <p class="desc-txt">Register NOW!</p>
                </div>
                <div class="flip-card-back" @click="loginPageRedirect()"> 
                    <p id="login-txt" class="desc-txt">Or login if you have an account...</p>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        name: "MainHomePage",
        methods: {
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
    #card-1 {
        transform: translate(21%, 25%);
    }

    #card-2 {
        transform: translate(125%, 190%);
    }

    #card-3 {
        transform: translate(250%, 10%);
    }

    #card-4 {
        transform: translate(390%, 180%);
    }

    #home-page-cont {
        margin-top: 50px;
        padding: 10px;
    }

    #login-txt {
        transform: translate(0, 0);
    }

    #log-reg-card {
        transition: transform 1.2s;
    }
    .desc-txt {
        font-family: "Arial Black", Gadget, sans-serif;
        font-size: 32px;
        letter-spacing: -0.6px;
        word-spacing: 1px;
        font-weight: 700;
        font-style: italic;
        font-variant: small-caps;
        transform: translate(0, 100%);
    }

    .img-style {
        border-radius: 30px;
    }

    .flip-card {
        position: absolute;
        background-color: transparent;
        width: 300px;
        height: 200px;
        perspective: 1000px; 
        border-radius: 10px;
    }

    .flip-card-inner {
        position: relative;
        width: 100%;
        height: 100%;
        text-align: center;
        transition: transform 0.8s;
        transform-style: preserve-3d;
    }

    .flip-card:hover .flip-card-inner {
        transform: rotateY(180deg);
    }

    .flip-card-front, .flip-card-back {
        position: absolute;
        width: 100%;
        height: 100%;
        backface-visibility: hidden;
    }

    .flip-card-back {
        background-color: transparent;
        transform: rotateY(180deg);
    }

    .flip-card-front {
        background-color: transparent;
    }
</style>
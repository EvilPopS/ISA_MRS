<template>
    <PopUp :v-show="isVisible"  @click="$emit('close')"> 
        <div id="successCont">
            <div id="succMess">{{mess}}</div>
            <br>
            <button id="closeSuccPopUpBtn"  @click="$emit('close')">X</button>
        </div>
    </PopUp>
</template>

<script>
    import PopUp from "../components/PopUp.vue";

    export default {
        name: "SuccessPopUp",
        components: {
            PopUp
        },
        props: {
            isVisible: Boolean,
            mess: String
        }
    }
</script>

<style scoped>
    #successCont {
        align-content: center;
        padding:20px;
    }

    #closeSuccPopUpBtn {
        border: 0;
        padding: 10px 20px;
        margin-top: 20px;
        color: rgb(190, 22, 22);
        border-radius: 20px;
        margin: 15px 10px;
    }

    #closeSuccPopUpBtn:hover{
        background: rgba(218, 214, 214, 70%);
        color: black;
    }

    #succMess {
        color: rgb(4, 87, 22);
        font-size: 20px;
        font-weight: bold;
        margin-top: 20%;
    }
</style>
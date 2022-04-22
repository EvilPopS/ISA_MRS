<template>
    <PopUp :v-show="isVisible"  @click="$emit('close')"> 
        <div id="errorCont">
            <div id="errMess">{{mess}}</div>
            <br>
            <button id="closeErrPopUpBtn" @click="$emit('close')">X</button>
        </div>
    </PopUp>
</template>

<script>
    import PopUp from "../components/PopUp.vue";

    export default {
        name: "ErrorPopUp",
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
    .error{
        color: red;
        margin-top: 10px;
        font-size: 0.8rem;
        font-weight: bold;
    }

    #errorCont {
        align-content: center;
        padding:20px;
    }

    #errMess {
        color: red;
        font-size: 20px;
        font-weight: bold;
        margin-top: 20%;
    }

    #closeErrPopUpBtn {
        border: 0;
        padding: 10px 20px;
        margin-top: 20px;
        color: rgb(190, 22, 22);
        border-radius: 20px;
        margin: 15px 10px;
    }

    #closeErrPopUpBtn:hover {
        background: rgba(218, 214, 214, 70%);
        color: black;
    }
</style>

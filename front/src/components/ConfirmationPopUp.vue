<template>
    <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow" class="close">X</button>
            <div class="container">
                <div class="dialogTitle">{{title}}</div>
                <br>
                <div id="message">{{message}}</div>
                <br>
            </div>
            <div class="vstack gap-2 col-md-5 mx-auto" id="options-btns">
                <button type="button" class="btn btn-success" @click="confirmedEvent">Yes</button>
                <button type="button" class="btn btn-outline-secondary" id="cancel-btn" @click="closeWindow">No</button>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: "ConfirmationPopUp",
    props: {
        title: String,
        message: String,
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
        },
        confirmedEvent(){
            this.$emit('confirmed-event');
        }
    }
    
}
</script>

<style scoped>
    #close_btn{
        height: 30px;
        width: 25px;
        border-radius: 20px;
        background: rgb(219, 45, 45);
    }

    .modal {
        display: block; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 100px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0,0,0); /* Fallback color */
        background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
    }

    /* Modal Content */
    .modal-content {
        background-color: #fefefe;
        margin: auto;
        padding: 20px;
        border: 1px solid #888;
        width: 50%;
        border-radius: 20px 40px;
    }

    /* The Close Button */
    .close {
        color: #ffffff;
        float: right;
        font-size: 16px;
        font-weight: bold;
    }

    .close:hover, .close:focus {
        color: #000;
        text-decoration: none;
        cursor: pointer;
    }

    div button#cancel-btn:hover {
        background-color: rgba(51, 92, 80, 0.8);
    }

    .dialogTitle {
        size: 40px;
        font-weight: bold;
        color: rgba(51, 92, 80, 0.8);
    }

</style>
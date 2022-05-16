<template>
    <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow" class="close">X</button>
            <div class="container">
                <h3>Report for client: {{clientFullName}}</h3>
                <div class="btn-group row d-flex justify-content-start">
                    <label class="label-desc d-flex justify-content-start">Your experience with client was: </label>
                    <div class="d-flex">
                        <input type="radio" class="btn-check" name="btnradio" id="btnradio1" autocomplete="off" checked>
                        <label class="btn btn-outline-success  left-btn-check" for="btnradio1">Positive</label>

                        <input type="radio" class="btn-check" name="btnradio" id="btnradio2" autocomplete="off">
                        <label class="btn btn-outline-success" for="btnradio2">Negative</label>
                    </div>
                </div>
                <div class="btn-group-checkbox row d-flex justify-content-start">
                    <label class="label-desc d-flex justify-content-start">Client showed up: </label>
                    <div class="d-flex justify-content-start">
                        <input type="checkbox" class="btn-check" id="btncheck1" autocomplete="off" checked>
                        <label class="btn btn-outline-success" for="btncheck1">Showed up</label>
                    </div>
                </div>
                <div class="my-text-area">
                    <label for="floatingTextarea2" class="label-text-area d-flex justify-content-start">Message:</label>
                    <textarea class="form-control" placeholder="Leave your message here" id="floatingTextarea2" style="height: 100px"></textarea>
                </div>
                <div class="vstack gap-2 col-md-5 mx-auto" id="options-btns">
                    <button type="button" class="btn btn-success" @click="closeWindow">Send</button>
                    <button type="button" class="btn btn-success" @click="closeWindow">Close</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "MakeReportModal",
    components: {

    },
    props: {
        clientEmail: String,
        reservationId: String,
        clientFullName: String
    },
    data(){
        return {
            data: {}
        }
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
        },
    }
}
</script>

<style scoped>
    b {
        color: black;
    }

    #close_btn{
        height: 30px;
        width: 25px;
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
        width: 60%;
    }

    /* The Close Button */
    .close {
        color: #aaaaaa;
        float: right;
        font-size: 16px;
        font-weight: bold;
    }

    .close:hover,
    .close:focus {
        color: #000;
        text-decoration: none;
        cursor: pointer;
    }

    .my-text-area {
        margin-top: 5%;
        margin-bottom: 10%;
    }

    .label-text-area, .label-desc {
        font-size: 15px;
        color: #335c50cc;
        display: block;
    }

    .label-desc {
        margin-top: 5%;
    }

    textarea.form-control:focus {
        outline: none !important;
        border:1px solid rgba(51, 92, 80, 0.8);
        box-shadow: 0 0 10px rgba(51, 92, 80, 0.8);
    }

    h3 {
        color: rgba(51, 92, 80, 0.8);
    }

    div input.btn-check{
        visibility: hidden;
    }

    label.left-btn-check {
        margin-right: 2%;
    }

</style>
<template>
        <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="$emit('message-modal-closed')" class="close">X</button>
            <div class="container">
                <span class="caption-style">What is the reason for account deletion?</span>
                <div class="form-group caption-style d-flex justify-content-center">
                    <textarea id="mess-inp" class="form-control" rows="3" v-model="reasonMessage" required></textarea>
                </div>
                <button id="send-btn" type="button" class="btn btn-success" @click="deleteRequestSent">Send delete request</button>
            </div>
        </div>  
        </div>  
</template>

<script>
    export default {
        name : "MessageInputModal",
        data (){
            return {
                reasonMessage : '',
            }
        },
        methods : {
            deleteRequestSent(){
                if (this.reasonMessage.length > 10)
                    this.$emit('delete-request-sent', this.reasonMessage);
            },
        }
    }
</script>

<style scoped>
    #mess-inp {
        width: 80%;
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

    #close_btn{
        height: 30px;
        width: 25px;
        border-radius: 20px;
        background-color: rgb(228, 59, 59);
        color: white;
    }
    #send-btn{
        margin-top: 10px;
        width: 40%;
    }

    .caption-style {
        font-weight: bold;
    }

</style>
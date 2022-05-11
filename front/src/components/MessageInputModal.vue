<template>
        <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="$emit('message-modal-closed')" class="close">X</button>
            <div class="container">
                <span>What is the reason for account deletion?</span>
                <input type="text" id="inputlg" class="form-control" v-model="reasonMessage" required>
                <button type="button" class="btn btn-success" id="conf_button" @click="deleteRequestSent">Send delete request</button>
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
    width: 90%;
    }

    #close_btn{
        height: 30px;
        width: 25px;
    }
    #conf_button{
        width: 100%;
    }

</style>
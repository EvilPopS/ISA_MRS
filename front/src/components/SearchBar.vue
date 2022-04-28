<template>
    <div>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <input v-model="searchVal" placeholder="Search Adventure..." />
        <p>&nbsp;</p>
        <button class="btn btn-success" id="btn-add" @click="$emit('search-adventure', {adventureName : this.searchVal})">Search Adventure </button>


        
    </div>
        
</template>

<script>
import { ref } from 'vue'
import SearchInput from 'vue-search-input'
import Button from '../components/Button.vue'
// Optionally import default styling

const searchVal = ref('')

export default {
  components: {
    SearchInput,
    Button
  },
  setup() {
    return {
      searchVal : ''
    }
  },

  methods : {


  }

    

}
</script>


<style scoped>
    
    #adventure-img {
        margin-top: 10px !important;
        height: 150px;
        width: auto;
        display: block;
        margin: 0 auto;
        border-radius: 10%;
    }

    #adventure-img:hover{
        width: 230px;
        height: 170px;
        border-radius: 20px;
        background: linear-gradient(rgb(255, 253, 253), rgb(241, 239, 239));
    }

    b{
        color: rgba(51, 92, 80, 0.8);
    }

    span button {
        margin: 10px 10px;
    }

    div h5#heading-adventure {
        color: rgba(51, 92, 80, 0.8);
        font-weight: bold;
    }

    div div#card-body-id {
        background-color: rgba(214, 218, 216, 0.7);
    }

    #btn-add{
        margin: 30px;
        float: left;
    }

    #btn-add:hover{
        border-radius: 10px;
        background: rgb(25, 117, 7);
    }
</style>
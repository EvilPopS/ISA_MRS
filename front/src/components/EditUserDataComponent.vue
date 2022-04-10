<template>
  <form @submit.prevent="handleSubmit">
      
      <img @click="changeProfilePhoto()"  :src="profilePicture" alt="">

      <label>Email: </label>
      <input type="email" required v-model="email">

      <label>Password: </label>
      <input type="password" required v-model="password">
      <div v-if="passwordError" class="error">{{passwordError}}</div> <!-- prazan string je jedna false -->

      <label>Confirm Password: </label>
      <input type="confirmPassword" required v-model="confirmPassword">
      <div v-if="passwordError" class="error">{{passwordError}}</div> <!-- prazan string je jedna false -->

      <label>Name: </label>
      <input type="text" required v-model="name">

      <label>Surname: </label>
      <input type="text" required v-model="surname">

      <label>Address: </label>
      <input type="text" required v-model="address">

      <label>Phone number: </label>
      <input type="text" required v-model="phoneNumber">

      <div class="submit">
          <button>Update</button>
      </div>
    </form>
</template>

<script>
export default {
    name: "EditUserDataComponent",
    data() {
        return {
            email: '',   //cilj je sta unesu u input da se ovo azurira, to je v-model
            password: '',                   //ovo radi u oba smera
            confirmPassword: '',
            passwordError: '',
            name: '',
            surname: '',
            address: '',
            phoneNumber: '',
            profilePicture: '../assets/logo.png'
            
        }
    },
    methods: {
        handleSubmit(){
            //ovo se desi kad se stisne na button submit sto automatski izaziva submit event
            console.log('form submited')
            //sa .prevent se sprecava da se submit odmah desi vec ovde recimo validacija
            this.passwordError = this.password.length > 5 ? '' : 'Password must be at least 6 chars long'
        },
        changeProfilePhoto() {
            //logika za redirektuje na prozor za unos nove slike
        }
    }
}
</script>

<style>
    form{
        max-width: 420px;
        margin: 30px auto;
        background: white;
        text-align: left;
        padding: 40px;
        border-radius: 10px;
    }

    label {
        color: #aaa;
        display: inline-block;
        margin: 25px 0 15px;
        font-size: 0.6rem;
        text-transform: uppercase;
        letter-spacing: 1px;
        font-weight: bold;
    }
    input, select {
        display: block;
        padding: 10px 6px;
        width: 100%;
        box-sizing: border-box;
        border: none;
        border-bottom: 1px solid #ddd;
        color: #555
    }
    input[type="checkbox"]{
        display: inline-block;
        width: 16px;
        margin: 0 10px 0 0;
        position: relative;
        top: 2px
    }
    .pill {
        display: inline-block;
        margin: 20px 10px 0 0;
        padding: 6px 12px;
        background: #eee;
        border-radius: 20px;
        font-size: 12px;
        letter-spacing: 1px;
        font-weight: bold;
        color: #777;
        cursor: pointer;
    }
    .submit button {
        background: blue;
        border: 0;
        padding: 10px 20px;
        margin-top: 20px;
        color: white;
        border-radius: 20px;
    }
    .submit {
        text-align: center;
    }
    .error{
        color: red;
        margin-top: 10px;
        font-size: 0.8rem;
        font-weight: bold;
    }

    img {
        width: 150px;
        height: 150px;
        display: block;
        margin: 0 auto;
    }
</style>
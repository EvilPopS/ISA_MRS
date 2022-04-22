<template>
  <div class="container">
    <div class="row"> 
      <div class="col-4">
        <span>Adventure</span>
        <hr class="solid">
        <label >Name</label>
        <input type="text" v-model="data.name" name="name" :placeholder="`Adventure name`" />
        <label>Description</label>
        <input type="text" v-model="data.description" name="description" :placeholder="`Adventure description...`" />
        <label>Rules</label>
        <input type="text" v-model="data.rules" name="rules" placeholder="Rules..." /> 
        <label>Price</label>
        <input type="number"  v-model="data.price" name="price" placeholder="Price" /> 
        <label>Biography</label>
        <input type="text"  v-model="data.biography" name="biography" placeholder="Biography..." /> 
        
        <label>Fishing equipment</label>
        <input type="text"  v-model="data.fishingEquipment" name="fishingEquipment" placeholder="Fishing Equipment..." /> 
        <div v-for="fishingEq in localFishingEquipment" :key="fishingEq" class="pill">
          <span  @click="deleteService(fishingEq)">{{fishingEq}}</span>
        </div>
        <label>Cancellation Conditions</label>
        <input type="number"  v-model="data.cancellationConditions" name="cancellationConditions" placeholder="Cancellation Conditions" /> 
      </div>
      <div class="col-4">
        <span> Address </span>
        <hr class="solid">
        <label class="label">City</label>
        <input type="text" v-model="data.city" name="city" placeholder="Adventure city">
        <label class="label">Street</label>
        <input type="text" v-model="data.street" name="street" placeholder="Adventure street">
        <label class="label">Zip code</label>
        <input type="text" v-model="data.zipCode" name="zipcode" placeholder="Adventure zip code">
      </div>
      <div class="col-4">
        <div>
          <span> Photos </span>
          <hr class="solid"/>
          <img id="imgPreview" :src="require('@/assets/' + 'logo.png')"/>
        </div>


      </div>
    </div>
    
  </div>
</template>

<script>
export default {
  name: 'UniversalAdditionForm',
  data() {
    return {
       localFishingEquipment :'',

        //univerzalni

        data : {
          name: '',
          description : '',
          photos : '' ,
          capacity : '',
          rules : '',
          street : '',
          city : '' ,
          zipcode : '',

          rentalType : '',
          price : '',

          biography : '',
          fishingEquipment : '', 
          cancellationConditions : '', 
        }

        

    
    }
  },
  props : {
      serviceType : String,
  },

  methods: {
    onSubmit(e) {
    //   e.preventDefault()
    //   if (!this.name) {
    //     alert('Please add a name')
    //     return
        let data = ''; // ovde napraviti objekat spram logovaog usera i psolati ga nazad na view i tamo POST metodu implementirati

        this.$emit('form-submitted', data);
      }
    //   const newTask = {
    //     // id: Math.floor(Math.random() * 100000),
    //     text: this.text,
    //     day: this.day,
    //     reminder: this.reminder,
    //   }
    //   this.$emit('add-task', newTask)
    //   this.text = ''
    //   this.day = ''
    //   this.reminder = false
  },
}
</script>

<style scoped>

label {
    color : #00a055;

}

.add-form {
  margin-bottom: 40px;
}
.form-control {
  margin: 20px 0;
}
.form-control label {
  display: block;
}
.form-control input {
  width: 100%;
  height: 40px;
  margin: 5px;
  padding: 3px 7px;
  font-size: 17px;
}
.form-control-check {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.form-control-check label {
  flex: 1;
}
.form-control-check input {
  flex: 2;
  height: 20px;
}

.btn-block {
  display: block;
  width: 100%;
}

div .pill {
      display: inline-block;
      margin: 20px 10px 0 0;
      padding: 6px 12px;
      background: rgba(51, 92, 80, 0.8);
      border-radius: 20px;
      font-size: 12px;
      letter-spacing: 1px;
      font-weight: bold;
      color: white;
      cursor: pointer;
  }

.btn {
  display: inline-block;
  background: rgb(0, 160, 85);
  color: #fff;
  border: none;
  padding: 10px 20px;
  margin: 5px;
  border-radius: 5px;
  cursor: pointer;
  text-decoration: none;
  font-size: 15px;
  font-family: inherit;
}

</style>
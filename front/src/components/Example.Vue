<template>
  <form @submit="onSubmit" class="add-form">
    <div class="form-control">
    <!-- ovo za photos je samo z apocetak ovako promeniti posle -->
    <div class="form-control">
      <label>Photos</label>
      <input type="text" v-model="photos" name="photos" :placeholder="`${serviceType} photos`" /> 
    </div>

    <div class="form-control">
      <label>Capacity</label>
      <input type="number" v-model="capacity" name="capacity" :placeholder="`${serviceType} capacity`" /> 
    </div>
    <div class="form-control">
      <label>Address</label>
      <input type="text" v-model="address" name="address" placeholder="Address" /> 
    </div>

    </div>


    


    <input type="submit" :value="`Add ${serviceType}`" class="btn btn-block" />
  </form>
</template>
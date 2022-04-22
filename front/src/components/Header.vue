<template>
    <h1>{{ title }}</h1>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <Button
      v-show="rentalServicesPage"
      @btn-click="$emit('toggle-add-rental-service')"
      :text="showAddrentalService ? 'Close' : `Add ${rentalServiceType}`"
      :backgroundColor="showAddrentalService ? '#ff3c3c' : 'rgb(0, 160, 85)'"
    />
</template>

<script>
import Button from './Button'
export default {
  name: 'Header',
  props: {
    title: String,
    showAddrentalService: Boolean,
    rentalServiceType : String,

  },
  components: {
    Button,
  },
  computed: {
    rentalServicesPage() {
      if (this.$route.path === '/InstructorAdventuresPage') { // ovde staviti za stranicu kad je koja da bude ukljuceno dugme
            console.log('uslo u stranicu za usluge.')
        return true
      } else {
        return false
      }
    },
  },
}
</script>

<style scoped>

</style>
<template>
  <v-app>
        <div id="app">
            <Navigation :key="$route.fullPath"></Navigation>
            <router-view />
        </div>
    </v-app>
</template>

<script>
  import Navigation from "./components/Navigation.vue"

  export default {
    name: "App",
    components: {
      Navigation
    }
    
  }

</script>


<style>
body {
  margin:0px;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>

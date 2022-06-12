<template>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  <Navbar></Navbar>
  <router-view></router-view>
</template>

<script>
import Navbar from './components/Navbar.vue'


export default {
  name: 'App',
  components: {
    Navbar
  
  }
}
</script>




<style>

</style>

<template>
<div class="container-fluid" style="height: 500; width:500">    
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <h2>Type in your e-mail address</h2>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
           <input type="text" style="margin-left:29%" v-model="email" >
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <button class="button btn-lg btn-secondary" v-on:click="checkEmail()" style="margin-left:37%">Send</button>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
</div>

</template>

<script>
import axios from 'axios'
import Swal from 'sweetalert2'

export default {
  name: 'PasswordLess',
  data() {
	return {
		email:""
		};
  },
 methods: {
     checkEmail() {
         axios
            .post('https://localhost:8090/registration/checkEmail', this.email)
            .then(response=>{
                console.log(response.data)
                if (response.data == false){
                    Swal.fire('Error', 'Could not find a user with that e-mail address. Please, try again.', 'error')
                } else { 
                    localStorage.setItem('email', this.email)
                    this.$router.push('/passwordlessCode')
                }
			})
            .catch(function (error) {
                console.log(error)
                Swal.fire('Error', 'Something went wrong. Please, try again later.', 'error')
            })
     }
 },
    created() {
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>

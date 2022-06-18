<template>
<form id="registrationForm" @submit.prevent = "checkEmail">
<div class="container-fluid"  style="height: 500; width:500">    
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <h2 style="margin-left:10%">Type in your e-mail address</h2>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-5">
        </div>
        <div class="col-sm-4">
           <input placeholder="E-mail" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" class="col-sm-4  col-form-control" type="email" v-model="email" required>
        </div>
        <div class="col-sm-3">
        </div>
    </div>
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <button class="button btn-lg btn-secondary" type="submit" id="submit" style="margin-left:37%">Send code</button>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
</div>
</form>
</template>

<script>
import axios from 'axios'
import Swal from 'sweetalert2'

export default {
  name: 'PasswordChange',
  data() {
	return {
		email:""
		};
  },
 methods: {
     checkEmail() {
         axios
            .post('https://localhost:8090/registration/checkEmailPassChange', this.email)
            .then(response=>{
                console.log(response.data)
                if (response.data == false){
                    Swal.fire('Error', 'Could not find a user with that e-mail address. Please, try again.', 'error')
                } else { 
                    localStorage.setItem('email', this.email)
                    this.$router.push('/passwordChangeForm')
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

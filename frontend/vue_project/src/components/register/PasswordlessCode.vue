<template>
<div class="container-fluid" style="height: 500; width:500">    
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <h2>Type in your code</h2>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
           <input type="text" style="margin-left:29%" v-model="passwordlessDto.userCode" >
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <button class="button btn-lg btn-secondary" v-on:click="logIn()" style="margin-left:37%">Send</button>
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
  name: 'PasswordlessCode',
  data() {
	return {
		passwordlessDto: {email: "", userCode: ""}
		};
  },
 methods: {
     logIn() {
         this.passwordlessDto.email = localStorage.getItem('email')
         axios
            .post('https://localhost:8090/registration/passwordlessLogin', this.passwordlessDto)
            .then(response=>{
                console.log(response.data)
                if (response.data == null){
                    Swal.fire('Error', 'Could not find a user with that e-mail address. Please, try again.', 'error')
                } else { 
                    localStorage.setItem('accessToken', response.data.accessToken)
                    localStorage.setItem('expiresIn', response.data.expiresIn)
                    localStorage.setItem('role', response.data.role)
                    localStorage.setItem('twoFA', response.data.twoFA)
                    localStorage.setItem('id', response.data.id)
                    localStorage.removeItem('email')
                    location.reload()
                }
			})
            .catch(function (error) {
                console.log(error)
                Swal.fire('Error', 'Something went wrong. Please, try again later.', 'error')
            })
     },
 },
    created() {
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>

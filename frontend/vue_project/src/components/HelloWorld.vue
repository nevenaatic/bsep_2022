<template>
<div class="container-fluid">    
	<form id="registrationForm" @submit.prevent = "signIn">
		<div class="container-fluid" style="margin-left:23%; margin-top:10%">
			<div class="col-lg-12 login-key">
				<i class="bi bi-file-person" aria-hidden="true"></i>
			</div>
      <h1 style="margin-right:40%; margin-top:10%" class="text-center">SIGN IN</h1><br>
			<label class="col-sm-4 col-form-label" for="email"><b>Email</b></label>
			<input placeholder="E-mail" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" class="col-sm-4  col-form-control" type="email" v-model="user.email" required>
			<br>

			<label class="col-sm-4 col-form-label" for="password"><b>Password</b></label>
			<input placeholder="Password" id="password" 
      pattern="(?=.*\d)(?=.*[a-z])(?=.*[!@#$%^(*)~])(?=.*[A-Z]).{8,}" 
      title="Must contain at least one number, one special symbol and one uppercase and lowercase letter, and at least 8 or more characters" 
      class="col-sm-4 col-form-control" type="password" 
      v-model="user.password" required>
			<br>

			<br><br>
			<div class="row">
				<div class="col-sm-3">
				</div>
				<div class="col-sm-2">
					<button id="submit" class="button btn-lg btn-secondary" type="submit">Sign in</button>
				</div>
        <div class="col-sm-7">
				</div>
			</div>
      <br><br>
		</div>
	</form>
  <div class="row">
				<div class="col-sm-4">
				</div>
				<div class="col-sm-3">
					<button  class="button btn-lg btn-danger" v-on:click="redirectToForgotPassword()">Forgot password ?</button>
				</div>
        <div class="col-sm-3">
          <button style="margin-left:5%" id="submit" v-on:click="redirectToPasswordless()" class="button btn-lg btn-primary" type="submit">Passwordless login</button>
				</div>
        <div class="col-sm-2">
				</div>
			</div>
</div>

</template>

<script>

import axios from 'axios'
import Swal from 'sweetalert2'

export default {
  name: 'HelloWorld',
  data() {
	return {
		user: {email:"", password:"" }, 
		};
  },
 methods: {

    signIn: function(){
			if (this.check()){
				axios
				.post('https://localhost:8090/registration/login', this.user)
				.then(response=>{
          console.log(response.data)
					if(response.data != null){
              if(response.data.twoFA == false){
                this.setLocalStorage(response.data)
                //this.$router.push('/certificateadmin')
                     location.reload()

              } else {
                const id = response.data.id
                console.log(id)
                localStorage.setItem('AT', response.data.accessToken)
                localStorage.setItem('EI', response.data.expiresIn)
                localStorage.setItem('R', response.data.role)
                localStorage.setItem('FA', response.data.twoFA)
                localStorage.setItem('ID', response.data.id)
                axios
                  .post('http://localhost:8085/register/' + id)
                .then(response=>{
                  console.log(response.data)
                  if (response.data.qrCode != null){
                      localStorage.setItem('qrCode', response.data.qrCode)
                      this.$router.push('/showQR')
                  } else if (response.data.alreadyRegistered != null){
                      this.$router.push('/confirmCode')
                  }
                  
                })
              }
          }
          else {
            Swal.fire('Error', 'Your credentials are wrong. Please, try again.', 'error')
          }
				})
				.catch(function (error) {
          console.log(error)
						if (error.response.status == 400)
              Swal.fire('Error', 'Your credentials are wrong. Please, try again.', 'error')
            else
              Swal.fire('Error', 'Something went wrong. Please, try again later.', 'error')
					}
				)
			}
		},

    redirectToPasswordless() {
      this.$router.push('/passwordless')
    },

    redirectToForgotPassword() {
      this.$router.push('/forgotPassword')
    },

    setLocalStorage(response) {
      localStorage.setItem('accessToken', response.accessToken)
      localStorage.setItem('expiresIn', response.expiresIn)
      localStorage.setItem('role', response.role)
      localStorage.setItem('twoFA', response.twoFA)
      localStorage.setItem('id', response.id)
    },

    check(){
			if (this.user.email == "" || this.user.password == "") {
        Swal.fire('Error', 'Please, fill both e-mail and password information', 'error')
        return false
      }
      return true;
		}


 }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

.login{
  margin-top: 20rem ;
  margin-left: 40%;
  background-color: rgba(1,1,1,0.5);;
  height: 20rem;
  width: 25rem
}
input{
  margin-left: 1rem;
  width:22rem ;
}
.btn{

  margin-left: 40%;
  margin-top: 2rem;
}
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
} 
</style>

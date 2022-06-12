<template>
<div class="container">    
		<div class="row">
		<div class="col-lg-3 col-md-2"></div>
		<div class="col-lg-6 col-md-8 login-box">
		<form id="registrationForm" @submit.prevent = "submitForm">
			<div class="container">
				<div class="col-lg-12 login-key">
					<i class="bi bi-file-person" aria-hidden="true"></i>
				</div>
				<div class="col-lg-12 login-title">REGISTRACIJA</div><br>
			
				<label class="col-sm-4 col-form-label" for="name"><b>Name</b></label>
				<input name="nameInput" pattern="[a-zA-Z]+[a-zA-Z ]+" title="Enter letters only." class="col-sm-4 col-form-control" type="text" v-model="newUser.name" required>
				<br>
				
				<label class="col-sm-4 col-form-label" for="surname"><b>Surname</b></label>
				<input pattern="[a-zA-Z]+[a-zA-Z ]+" title="Enter letters only." class="col-sm-4 col-form-control" type="text" v-model="newUser.surname" required>
				<br>
							
				<label class="col-sm-4 col-form-label" for="email"><b>Email</b></label>
				<input class="col-sm-4 col-form-control" type="email" v-model="newUser.email" required>
				<br>

				<label class="col-sm-4 col-form-label" for="password"><b>Password</b></label>
				<input id="password" class="col-sm-4 col-form-control" type="password" v-model="newUser.password" @change='check_pass()' required>
				<br>
				
				<label class="col-sm-4 col-form-label" for="password-repeat"><b>Repeat password</b></label>
				<input id="confirmPassword" class="col-sm-4 col-form-control" type="password" @change='check_pass()' required>
				<br>

				<label class="col-sm-4 col-form-label" for="address"><b>Address</b></label>
				<input class="col-sm-4 col-form-control" type="text" v-model="newUser.address" required>
				<br>

				<label class="col-sm-4 col-form-label" for="city"><b>City</b></label>
				<input pattern="[a-zA-Z]+[a-zA-Z ]+" class="col-sm-4 col-form-control" type="text" v-model="newUser.city" required>
				<br>
				
				<label class="col-sm-4 col-form-label" for="country"><b>Country</b></label>
				<input pattern="[a-zA-Z]+[a-zA-Z ]+" title="Enter letters only." class="col-sm-4 col-form-control" type="text" v-model="newUser.country" required>

				<br><br>
				
				<button id="submit" class="button" type="submit" disabled>Register</button>
				<div class="container signin">
					<p>Already have an account? <a href="#/login">Sign in</a>.</p>
				</div>
			</div>
		</form>
		</div></div></div>
</template>

<script>

import axios from 'axios'
import Swal from 'sweetalert2'

export default {
	name: "RegisterComponent",
  data() {
	return {
		newUser: {name:"", surname:"", email:"", password:"", address:"", city:"", country:"", verificationCode:"1", verified:false
		},
		};
  },

  methods:{
		submitForm:function(){
			console.log(this.newUser)
				axios
				.post('http://localhost:8090/registration/registerUser', this.newUser)
				.then(response=>{
					localStorage.setItem('email', this.newUser.email)
					this.bool = response.data
					if(this.bool === true)
					{
						console.log("IT IS TRUE")
						this.$router.push('/emailVerification')
					}
					else
					{
						Swal.fire('Any fool can use a computer')
					}
				})
		},
		check_pass(){
			if (document.getElementById('password').value ==
					document.getElementById('confirmPassword').value) {
				document.getElementById('submit').disabled = false;
			} else {
				document.getElementById('submit').disabled = true;
			}
		}
	},
	created(){
		localStorage.removeItem('email')
	}
}


</script>
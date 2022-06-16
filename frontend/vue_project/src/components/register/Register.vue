<template>
<div class="container-fluid">    
	<form id="registrationForm" @submit.prevent = "submitForm">
		<div class="container-fluid" style="margin-left:14%; margin-top:10%">
			<div class="col-lg-12 login-key">
				<i class="bi bi-file-person" aria-hidden="true"></i>
			</div>
			<h1  class="text-center">REGISTRATION</h1><br>
		
			<label class="col-sm-4 col-form-label" for="name"><b>Name</b></label>
			<input name="nameInput" pattern="[a-zA-Z]+[a-zA-Z ]+" title="Enter letters only." class="col-sm-4 col-form-control" type="text" v-model="newUser.name" required>
			<br>
			
			<label class="col-sm-4 col-form-label" for="surname"><b>Surname</b></label>
			<input pattern="[a-zA-Z]+[a-zA-Z ]+" title="Enter letters only." class="col-sm-4 col-form-control" type="text" v-model="newUser.surname" required>
			<br>
						
			<label class="col-sm-4 col-form-label" for="email"><b>Email</b></label>
			<input pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" class="col-sm-4  col-form-control" type="email" v-model="newUser.email" required>
			<br>

			<label class="col-sm-4 col-form-label" for="password"><b>Password</b></label>
			<input id="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[!@#$%^(*)~])(?=.*[A-Z]).{8,}" title="Must contain at least one number, one special symbol and one uppercase and lowercase letter, and at least 8 or more characters" class="col-sm-4 col-form-control" type="password" v-model="newUser.password" required>
			<br>
			
			<label class="col-sm-4 col-form-label" for="password-repeat"><b>Repeat password</b></label>
			<input id="confirmPassword" pattern="(?=.*\d)(?=.*[a-z])(?=.*[!@#$%^(*)~])(?=.*[A-Z]).{8,}" title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters" class="col-sm-4 col-form-control" type="password" required>
			<br>

			<label class="col-sm-4 col-form-label" for="address"><b>Address</b></label>
			<input class="col-sm-4 col-form-control" type="text" v-model="newUser.address" required>
			<br>

			<label class="col-sm-4 col-form-label" for="city"><b>City</b></label>
			<input pattern="[a-zA-Z]+[a-zA-Z ]+" title="Enter letters only." class="col-sm-4 col-form-control" type="text" v-model="newUser.city" required>
			<br>
			
			<label class="col-sm-4 col-form-label" for="country"><b>Country</b></label>
			<input pattern="[a-zA-Z]+[a-zA-Z ]+" title="Enter letters only." class="col-sm-4 col-form-control" type="text" v-model="newUser.country" required>

			<div class="form-check" style=" margin-top: 1%; margin-left:35%">
                <input class="form-check-input" type="checkbox" value="" id="2FA">
                <label class="form-check-label" for="Key Agreement">
                  Enable 2-Factor-Authentication
                </label>
              </div>

			<br><br>
			<div class="row">
				<div class="col-sm-4">
				</div>
				<div class="col-sm-2">
					<button id="submit" class="button btn-lg btn-secondary" type="submit">Register</button>
				</div>
				<div class="col-sm-2">
					<p>Already have an account ? <a style="margin-left:1%" href="/login">Sign in</a>.</p>
				</div>
			</div>
		</div>
	</form>
</div>
</template>

<script>

import axios from 'axios'
import Swal from 'sweetalert2'

export default {
	name: "RegisterComponent",
  data() {
	return {
		newUser: {name:"", surname:"", email:"", password:"", address:"", city:"", country:"", verificationCode:"1", verified:false, twoFA: false
		},
		};
  },

  methods:{
		submitForm:function(){
			if (document.getElementById("2FA").checked == true)
				this.newUser.twoFA = true;
				console.log(this.newUser)
				axios
				.post('https://localhost:8090/registration/registerUser', this.newUser)
				.then(response=>{
					this.bool = response.data
					if(this.bool === true)
					{
						this.$router.push('/emailVerification')
					}
				})
				.catch(function (error) {
					if (error.response.status == 409) {
						Swal.fire('Error', 'User with this e-mail address already exists. Please, sign in or recover your password.', 'error')
					}
				})
		},
	},
	created(){
		
	}
}


</script>
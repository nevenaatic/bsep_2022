<template>
<div>    		   

			<h1 id="h1-verification">User Verification</h1>
			<form id="registrationForm" method ="POST" @submit.prevent = "submitForm">
				<input type="text" v-model="code" placeholder="Enter Verification code..." name="ime">
				<button class="button" type="submit">Submit</button>
				<button class="button" id = "homeBTN" type="submit" v-on:click="home()">Home Page</button>
			</form>
		</div>		  
</template>

<script>

import Swal from 'sweetalert2'
import axios from 'axios'

export default {
	name: "EmailVerification",
  data() {
	return {
		code: "",
		email: "",
		isAuthenticated : null,
	};
  },

  methods: {
		submitForm: function() {
			console.log(this.code)
			axios
				.post('https://localhost:8090/registration/emailVerification', this.code)
				.then(response => {
					this.isAuthenticated = response.data;
					if(this.isAuthenticated)
					{
						this.$router.push("/")
					}

				})
				.catch(err =>{
					Swal.fire('Your code is wrong or expired. Please, try again')	
					console.log(err);
				})

		},
		home: function(){
			this.$router.push("/")
		}
	},
	mounted(){
		
	},
}


</script>
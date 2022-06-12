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
				.post('http://localhost:8090/registration/emailVerification', this.code)
				.then(response => {
					this.isAuthenticated = response.data;
					if(this.isAuthenticated)
					{
						Swal.fire('Good job!','You clicked the button!','success')
						this.$router.push("/")
					}
					else
					{
						Swal.fire('Any fool can use a computer')
					}
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
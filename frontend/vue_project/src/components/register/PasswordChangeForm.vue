<template>
<div class="container-fluid">    
	<form id="registrationForm" @submit.prevent = "changePass">
		<div class="container-fluid" style="height: 500; width:500">    
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <h2>Type in your e-mail code and new password</h2>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
           <input placeholder="Code" class="col-sm-4  col-form-control" type="text" v-model="passwordChange.code" required>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
           <input id="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[!@#$%^(*)~])(?=.*[A-Z]).{8,}" 
            title="Must contain at least one number, one special symbol and one uppercase and lowercase letter, and at least 8 or more characters" class="col-sm-4 col-form-control" 
            type="password"  placeholder="Password" v-model="passwordChange.password" required>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
          <input id="confirmPassword" pattern="(?=.*\d)(?=.*[a-z])(?=.*[!@#$%^(*)~])(?=.*[A-Z]).{8,}" 
            title="Must contain at least one number and one special symbol and one uppercase and lowercase letter, and at least 8 or more characters" class="col-sm-4 col-form-control" 
            type="password"  placeholder="Confirm password" v-model="confirmPassword" required>
			<br>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <button id="submit" class="button btn-lg btn-secondary" style="margin-left:37%" type="submit">Change Password</button>
        </div>
        <div class="col-sm-4">
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
  name: 'PasswordChangeForm',
  data() {
	return {
		passwordChange: {code: "", password:""}, 
        confirmPassword: "",
		};
  },
 methods: {

    changePass() {
        console.log(this.passwordChange)
        if (this.check()){
        axios
            .post('https://localhost:8090/registration/changePassword', this.passwordChange)
            .then(response=>{
                console.log(response.data)
                if (response.data == false){
                    Swal.fire('Error', 'Your code is expired or incorrect. Please, try again', 'error')
                } else { 
                    Swal.fire('Success', 'Password changed successfully !', 'success')
                    this.$router.push('/')
                }
			})
            .catch(function (error) {
                console.log(error)
                Swal.fire('Error', 'Something went wrong. Please, try again later.', 'error')
            })
        }
    },

    check(){
			if (this.passwordChange.password != this.confirmPassword) {
            Swal.fire('Error', 'Passwords do not match, please try again.', 'error')
            return false
      }
      return true;
	}


 },

 created() {
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

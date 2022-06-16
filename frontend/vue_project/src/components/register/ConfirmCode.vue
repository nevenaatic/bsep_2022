<template>
<div class="container-fluid" style="height: 500; width:500">    
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <h2>Type in your code from Authenticatior App</h2>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
           <input type="text" style="margin-left:29%" v-model="data.token" >
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <button class="button btn-lg btn-secondary" v-on:click="checkCode()" style="margin-left:37%">Sign in</button>
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
  name: 'ConfirmCode',
  data() {
	return {
		data: {token: "", userId: ""},
		};
  },
 methods: {
    setLocalStorage() {
        localStorage.setItem('accessToken', localStorage.getItem('AT'))
        localStorage.setItem('expiresIn', localStorage.getItem('EI'))
        localStorage.setItem('role', localStorage.getItem('R'))
        localStorage.setItem('twoFA', localStorage.getItem('FA'))
        localStorage.setItem('id', localStorage.getItem('ID'))
        localStorage.removeItem('AT')
        localStorage.removeItem('EI')
        localStorage.removeItem('R')
        localStorage.removeItem('FA')
        localStorage.removeItem('ID')
    },

     checkCode() {
         this.data.userId =  localStorage.getItem('ID')
         console.log(this.data)
         axios
            .post('http://localhost:8085/verify', this.data)
            .then(response=>{
                console.log(response.data)
                if (response.data.verified == false){
                    Swal.fire('Error', 'Your code is incorrect. Please, try again.', 'error')
                } else {
                    this.setLocalStorage();
                    //this.$router.push('/certificateadmin')
                     location.reload()

                }
			})
            .catch(function (error) {
                console.log(error)
                Swal.fire('Error', 'Something went wrong. Please, try again later.', 'error')
            })
     }
 },
    created() {
        this.image = localStorage.getItem('qrCode');
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>

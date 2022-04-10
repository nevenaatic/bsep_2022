<template>
  <div class="picture" >
   
   <div class="login"> 

     <div > <h1> Sign in </h1> </div> 
     <form>
       <div class="form-group">
         <div> 
       <label for="name">Email</label>
                <input
                  type="text"
                  class="form-control"
                  placeholder="Enter email"
                  v-model="this.email"
                />
         </div>

         <div style="margin-top: 0.5rem"> 
                 <label for="password">Password</label>
                <input
                  type="password"
                  class="form-control"
                  placeholder="Enter password"
                  v-model="this.password"
                />
       </div>
      <button type="submit" class="btn btn-primary" @click="signIn()"> </button>
        </div>
     </form>
    </div> 
  </div>
  
</template>

<script>
import axios from 'axios'

export default {
 name: 'HelloWorld',
 
 data(){
   return {
      email: "",
      password: ""
   }
 },

 methods: {

  async singIn(){

    const headers ={
      "Content-type": "application/json",
    }; 
    console.log("HEEEEEEJ");
    axios.post("http://localhost:8081/appUser/login",{ email: this.email, password: this.password }, {headers})
          .then( response => {
          localStorage.setItem("userEmail", response.data.email);
          localStorage.setItem("userType", response.data.userType);
          console.log("HEJ");
          if (localStorage.getItem("userType") == "admin"){
            this.$router.go("http://localhost:8082/admin")
           // this.$router.go(0);
          } else if (localStorage.getItem("userType") == "certification_authority") {
            this.$router.push({name: 'MyProfileCA'})
           /// this.$router.go(0);
          } else if (localStorage.getItem("userType") == "end_user") {
            this.$router.push({name: 'MyProfileCA'})
          //  this.$router.go(0);
          }

          })
  }
 }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.picture{
   background:url("../assets/security.png");
    min-height : 50rem;
  min-width : 100%;
  background-size:100% 100%;
  background-repeat:no-repeat;
  overflow-y: hidden;
  overflow-x: hidden;
}
.login{
  margin-top: 20rem ;
  margin-left: 40%;
  background-color: rgba(1,1,1,0.5);;
  height: 20rem;
  width: 25rem
}
label{
  margin-left:1rem;

  color: white;
  font-size: large;
}
input{
  margin-left: 1rem;
  width:22rem ;
}
.btn{

  margin-left: 40%;
  margin-top: 2rem;
}
h1{
  margin-left: 8rem;
  color: white
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

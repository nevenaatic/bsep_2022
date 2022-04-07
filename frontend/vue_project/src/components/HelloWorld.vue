<template>
  <div class="picture" >
   
   <div class="login"> 

     <div > <h1> Sign in </h1> </div> 
     <form>
       <div class="form-group">
         <div> 
       <label for="name">Username</label>
                <input
                  type="text"
                  class="form-control"
                  placeholder="Enter username"
               
                />
         </div>

         <div style="margin-top: 0.5rem"> 
                 <label for="password">Password</label>
                <input
                  type="password"
                  class="form-control"
                  placeholder="Enter password"
               
                />
       </div>
      <button type="button" class="btn btn-primary">Sign in</button>
        </div>
     </form>
    </div> 
  </div>
  
</template>

<script>
export default {
  name: 'HelloWorld',
 
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

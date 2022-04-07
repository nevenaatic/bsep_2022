<template>
<div >

    <div class="row" style="height: 50rem"> 
        <div class="column" style="background: grey" >
            <MenuAdmin/>  
         <!--   
            <nav class="nav flex-column">
                <h3> Welcome admin </h3>
            <a class="nav-link active" href="#">Active</a>
            <a class="nav-link" href="#">Link</a>
            <a class="nav-link" href="#">Link</a>
            <a class="nav-link disabled" href="#">Disabled</a>
            </nav> -->
        </div>
    <div class="column" style="width: 83%">
        <div class="picture" >
           
                
              <div class="login"> 

     <div style="margin-top: 15%; " > <h1 style="margin-top: 5%; margin-left: 10%"> Welcome to your app, admin </h1> </div> 
   
    </div>
     
        
         </div>
             
         </div>
</div>

</div>
</template>

<script>
import MenuAdmin from './MenuAdmin.vue'
export default {
    components: {MenuAdmin},
    name: "AdminProfile"
}
</script>

<style scoped>
h3{
margin-left: 2rem;
margin-right: 2.5rem;

}
.picture{
   background:url("../assets/background2.jpg");
   opacity: 0.9;
    min-height : 100%;
  min-width : 100%;
  background-size:100% 100%;
  background-repeat:no-repeat;
  overflow-y: hidden;
  overflow-x: hidden;
}
.informations{
    width: 70%;
    height: 50%;
}
.login{
  margin-top: 20rem ;
  margin-left: 20%;
  background-color: rgba(1,1,1,0.5);;
  height: 10rem;
  width: 50rem
}
h1{
     color: white; 
     }

</style>
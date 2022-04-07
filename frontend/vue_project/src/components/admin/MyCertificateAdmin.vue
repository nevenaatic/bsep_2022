<template>
<div >

    <div class="row" style="height: 50rem"> 
      <div class="column" style="background: grey" >
       <MenuAdmin/>   
      
        </div>
    <div class="column" style="width: 83%">
        <div class="picture" >
           
                
              <div class="informations"> 

     
   
    </div>
     
        
         </div>
             
         </div>
</div> 


</div>
</template>

<script>
import MenuAdmin from './MenuAdmin.vue'
export default {
  components: { MenuAdmin },
    name: "AdminProfile"
}
</script>

<style scoped>
h3{
margin-left: 2rem;
margin-right: 2.5rem;

}
.picture{
   background:url("../../assets/background2.jpg");
   opacity: 0.9;
    min-height : 100%;
  min-width : 100%;
  background-size:100% 100%;
  background-repeat:no-repeat;
  overflow-y: hidden;
  overflow-x: hidden;
}

.informations{
  margin-top: 10rem ;
  margin-left: 15%;
  background-color: rgba(1,1,1,0.5);;
  height: 25rem;
  width: 55rem
}
h1{
     color: white; 
     }

</style>
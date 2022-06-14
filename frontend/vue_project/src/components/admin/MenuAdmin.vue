<template>
<div >

  
       
            <nav class="nav flex-column">
                <h3> Welcome admin </h3>
            <a class="nav-link active" href="/certificates">All certificates</a>
            <a class="nav-link" href="/rejected">Rejected</a>
            <a class="nav-link" href="/certificateadmin">My certificates</a>
            <div>  <button type="button" class="btn btn-primary" v-on:click="logOut()"> <router-link to="/"> Log out</router-link ></button></div>
            
            </nav>
        
   


</div>
</template>

<script> </script>

<style scoped>
h3{
    margin-top: 2rem;
margin-left: 2rem;
margin-right: 2.5rem;
margin-bottom: 3rem;

}
nav{
    margin-left: 1rem;
  
}
a{
     color: black;
     font-size: larger;
     margin-left: 1rem;
}
a:hover{
    color: rgba(6, 29, 128, 0.74)
}
.btn{
    margin-left: 3rem;
    margin-top: 180%;
}
</style>
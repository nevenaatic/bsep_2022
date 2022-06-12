<template>
<div >

    <div class="row" style="height: 50rem"> 
      <div class="column" style="background: grey" >
       <MenuAdmin/>   
      
        </div>
    <div class="column" style="width: 83%">
        <div class="picture" >
           
                
              <div class="informations"> 

<div class="container-fluid my-container">
    <div class="row my-row  justify-content-around">
        <div class="col-sm-6 my-col">
            <span class="input-group-text" style="margin-top: 2%;" id="basic-addon1">Users:</span>
                <select class="form-select" aria-label="Example select with button addon">
                  <option v-for: = "u in users" :value="u.id">{{u.name}} {{u.surname}} - {{u.address}}, {{u.city}}, {{u.country}} - {{u.email}}</option>
                </select>
                <span class="input-group-text" style="margin-top: 5%" id="basic-addon1">Datum-od</span>
                <input type="date" class="form-control" placeholder="Datum-od">
                <span class="input-group-text" style="margin-top: 5%" id="basic-addon1">Datum-do</span>
                <input type="date" class="form-control" placeholder="Datum-do">
		</div>
            <div class="col-sm-6 my-col">
              <img src="../../assets/certificate-icon.png" height="450" width="600" style="margin-top: 5%; margin-left: 5%">
              <button type="button" style="margin-top: 15%; margin-left: 12%;"  class="btn btn-secondary btn-lg">Issue new certificate</button>
            </div>
          </div>
    </div>
</div>
   
    </div>
     
        
         </div>
             
         </div>
</div> 

</template>

<script>

import MenuAdmin from './MenuAdmin.vue';
//import Swal from 'sweetalert2'
//import axios from 'axios'

export default {
  components: { MenuAdmin },
    name: "IssueCertificate",

  data() {
    return {
      users: [],
    };
  },

  methods: {
    async fetchUsers() {
      const res = await fetch("https://localhost:8090/appUser/getAllUsers");
      const data = await res.json();
      return data;
    },
  },

  async mounted() {
    this.users = await this.fetchUsers();
  }
};


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
  margin-top: 3rem ;
  margin-left: 3rem;
  background-color: rgba(1,1,1,0.5);;
  width: 98rem
}
h1{
     color: white; 
     }

</style>
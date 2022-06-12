<template>
<div >

    <div class="row" style="height: 50rem"> 
      <div class="column" style="background: grey" >
       <MenuAdmin/>   
      
        </div>
    <div class="column" style="width: 83%">
        <div class="picture" >
           
                
              <div class="informations"> 

<div class="row row-cols-1 row-cols-md-4 g-4">
  <div v-for:="c in certificates" class="col">
    <div class="card" style="width: 20rem; margin-top: 2%">
  <img src="../../assets/CA2.png" height="150" width="300" class="card-img-top" alt="...">
    <div class="card-body">
      <h5 class="card-title" v-if="c.certificateType == 'CA'">#{{c.serialCode}} - CA</h5>
      <h5 class="card-title" v-else >#{{c.serialCode}} - END ENTITY</h5>
    </div>
    <ul class="list-group list-group-flush">
      <li class="list-group-item">Subject name: {{c.subjectFullName}}</li>
      <li class="list-group-item">Subject Email: {{c.subjectEmail}}</li>
      <li class="list-group-item">Isser name: {{c.issuerFullName}}</li>
      <li class="list-group-item">Issuer Email: {{c.issuerEmail}}</li>
      <li class="list-group-item">Valid: {{c.validFrom.substring(8,10)}}.{{c.validFrom.substring(5,7)}}.{{c.validFrom.substring(0,4)}} - {{c.validUntil.substring(8,10)}}.{{c.validUntil.substring(5,7)}}.{{c.validUntil.substring(0,4)}}.</li>
      <li v-if="c.revoked" style="color: red" class="list-group-item">Certificate is revoked !</li>
      <li v-if="!c.revoked" style="color: green" class="list-group-item">Certificate is not revoked !</li>
    </ul>
    <div class="card-body">
      <button type="button" v-on:click="check()" class="btn-sm btn-secondary">Download</button>
      <button type="button" class="btn-sm btn-primary" style="margin-left: 2%">Check Validity</button>
      <button v-if="!c.revoked" type="button" style="margin-left: 2%" class="btn-sm btn-danger">Revoke</button>
    </div>
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

export default {
  components: { MenuAdmin },
    name: "AdminProfile",

  data() {
    return {
      certificates: [],
      name: "",
      street: "",
      city: "",
    };
  },

  methods: {
    async fetchCertificates() {
      const res = await fetch("http://localhost:8090/certificate/getAllCertificates");
      const data = await res.json();
      return data;
    },
    check() {
      console.log(this.certificates);
    }
  },

  async created() {
    this.certificates = await this.fetchCertificates();
  },
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
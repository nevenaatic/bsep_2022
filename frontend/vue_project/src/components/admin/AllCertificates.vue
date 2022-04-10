<template>
<div >

    <div class="row" style="height: 50rem"> 
      <div class="column" style="background: grey" >
       <MenuAdmin/>   
      
        </div>
    <div class="column" style="width: 83%">
        <div class="picture" >
           
                
              <div class="informations"> 

                <div class="card" style="width: 18rem;">
  <img src="../../assets/CA2.png" height="200" width="300" class="card-img-top" alt="...">
  <div class="card-body">
    <h5 class="card-title">Certificate</h5>
    <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
  </div>
  <ul class="list-group list-group-flush">
    <li class="list-group-item">An item</li>
    <li class="list-group-item">A second item</li>
    <li class="list-group-item">A third item</li>
  </ul>
  <div class="card-body">
    <a href="#" class="card-link"><button type="button" v-on:click="check()" class="btn btn-secondary">Download</button></a>
    <a href="#" class="card-link"><button type="button" class="btn btn-primary">Revoke</button></a>
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
      const res = await fetch("http://localhost:8081/certificate/getAllCertificates");
      const data = await res.json();
      return data;
    },
    async search() {
      const res = await fetch("http://localhost:8081/api/boats/search", {
        method: "POST",
        headers: {
          "Content-type": "application/json",
        },
        body: JSON.stringify({
          name: this.name,
          street: this.street,
          city: this.city,
        }),
      });
      const data = await res.json();
      this.certificates = data;
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
  margin-top: 5rem ;
  margin-left: 10%;
  background-color: rgba(1,1,1,0.5);;
  height: 50rem;
  width: 80rem
}
h1{
     color: white; 
     }

</style>
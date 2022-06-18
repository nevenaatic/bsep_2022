<template>
<div class="container-fluid" style="margin-left:3%; margin-top:2%">
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
      <li class="list-group-item">Valid: {{new Date(c.validFrom).toLocaleString()}} - {{new Date(c.validUntil).toLocaleString()}}</li>
      <li v-if="c.revoked" style="color: red" class="list-group-item">Certificate is revoked !</li>
      <li v-if="!c.revoked" style="color: green" class="list-group-item">Certificate is not revoked !</li>
    </ul>
    <div class="card-body">
      <button v-if="!c.revoked" type="button" v-on:click="downloadCertificate(c.serialCode)" class="btn-sm btn-secondary">Download</button>
      <button v-if="!c.revoked" type="button" class="btn-sm btn-primary" v-on:click="checkValidity(c.serialCode)" style="margin-left: 2%">Check Validity</button>
      <button v-if="!c.revoked && this.role != 'ROLE_END_ENTITY'" type="button" style="margin-left: 2%" v-on:click="revoke(c.serialCode)" class="btn-sm btn-danger">Revoke</button>
    </div>
</div>
  </div>
</div>
</div>
</template>

<script>

import Swal from 'sweetalert2'
import axios from 'axios'

export default {
  components: {},
    name: "MyIssuedCertificates",

  data() {
    return {
      certificates: [],
      role: "",
      userId: "",
    };
  },

  methods: {
    async fetchCertificates() {
      const res = await fetch("https://localhost:8090/certificate/getCertificatesById/" + this.userId);
      const data = await res.json();
      return data;
    },

    check() {
      console.log(this.certificates);
    },

    checkValidity(serial) {
      const headers = {
        Authorization: "Bearer " + localStorage.getItem("accessToken"),
      };
    axios
				.post('https://localhost:8090/certificate/checkValidity', serial, {headers})
				.then(response => {
					if(response.data)
						Swal.fire('Provera uspešna','Sertifikat ' + serial  + ' je validan !','success')
					else
						Swal.fire('Provera uspešna','Sertifikat ' + serial  + ' nije validan !','error')
				})
        .catch(err =>{
          Swal.fire('Error', 'Something went wrong. Please, try again later.','error')
          console.log(err);
        })
    },

    revoke(serial) {
      const headers = {
        Authorization: "Bearer " + localStorage.getItem("accessToken"),
      };
    axios
				.post('https://localhost:8090/certificate/revokeCertificate', serial, {headers})
				.then(response => {
					if(response.data)
						Swal.fire('Certificate', 'Certificate ' + serial  + ' has been revoked successfully !','success')
            location.reload();
				})
        .catch(err =>{
          Swal.fire('Certificate', 'Certificate ' + serial  + ' has NOT been revoked successfully. Try again later','error')
          console.log(err);
        })
    },

    async downloadCertificate(serial){
      const headers = {
        Authorization: "Bearer " + localStorage.getItem("accessToken"),
      };
      console.log(serial)
      await axios
            .post('https://localhost:8090/certificate/downloadCertificate', serial, {headers})
            .then(response => {
              if(response.data){
                var a = document.createElement('a');
                var blob = new Blob([response.data], {'type': 'application/octet-stream'});
                a.href = window.URL.createObjectURL(blob);
                a.download = `${serial}.crt`;
                a.click();
              }
              }
            )
            .catch(err =>{
                Swal.fire('Certificate', 'Certificate ' + serial  + ' has NOT been downloaded successfully. Try again later','error')
                console.log(err);
              })
    },


  },

  async created() {
    this.userId = localStorage.getItem('id')
    this.certificates = await this.fetchCertificates();
    this.role = localStorage.getItem('role')
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
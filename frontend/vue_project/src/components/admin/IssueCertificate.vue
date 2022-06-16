<template>
<div class="container-fluid my-container" style="margin-top: 1%">
    <div class="row my-row  justify-content-around">
        <div class="col-sm-6 my-col">
            <span class="input-group-text" style="margin-top: 2%;" id="basic-addon1">Users:</span>
                <select class="form-select" v-model="certificateDTO.subjectId" aria-label="Example select with button addon">
                  <option v-for: = "u in users" :value="u.id">{{u.name}} {{u.surname}} - {{u.address}}, {{u.city}}, {{u.country}} - {{u.email}}</option>
                </select>
                <span class="input-group-text" style="margin-top: 5%" id="basic-addon1">Date from</span>
                <input type="date" v-model="certificateDTO.validFrom" class="form-control" placeholder="Datum-od">
                <span class="input-group-text" style="margin-top: 5%" id="basic-addon1">Date until</span>
                <input type="date" v-model="certificateDTO.validUntil" class="form-control" placeholder="Datum-do">
                <div style=" margin-top: 3%; margin-left:35%">
                  <h2> 
                    Key usages:
                  </h2>
                </div>
                <div class="form-check" style=" margin-top: 1%; margin-left:35%">
                <input class="form-check-input" type="checkbox" value="" id="Encipher Only">
                <label class="form-check-label" for="Encipher Only">
                  Encipher Only
                </label>
              </div>
              <div class="form-check" style=" margin-top: 1%; margin-left:35%">
                <input class="form-check-input" type="checkbox" value="" id="CRL Signing">
                <label class="form-check-label" for="CRL Signing">
                  CRL Signing
                </label>
              </div>
              <div class="form-check" style=" margin-top: 1%; margin-left:35%">
                <input class="form-check-input" type="checkbox" value="" id="Key Certificate Signing">
                <label class="form-check-label" for="Key Certificate Signing">
                  Key Certificate Signing
                </label>
              </div>
              <div class="form-check" style=" margin-top: 1%; margin-left:35%">
                <input class="form-check-input" type="checkbox" value="" id="Key Agreement">
                <label class="form-check-label" for="Key Agreement">
                  Key Agreement
                </label>
              </div>
              <div class="form-check" style=" margin-top: 1%; margin-left:35%">
                <input class="form-check-input" type="checkbox" value="" id="Data Encipherment">
                <label class="form-check-label" for="Data Encipherment">
                  Data Encipherment
                </label>
              </div>
              <div class="form-check" style=" margin-top: 1%; margin-left:35%">
                <input class="form-check-input" type="checkbox" value="" id="Key Encipherment">
                <label class="form-check-label" for="Key Encipherment">
                  Key Encipherment
                </label>
              </div>
              <div class="form-check" style=" margin-top: 1%; margin-left:35%">
                <input class="form-check-input" type="checkbox" value="" id="Non-repudiation">
                <label class="form-check-label" for="Non-repudiation">
                  Non-repudiation
                </label>
              </div>
              <div class="form-check" style=" margin-top: 1%; margin-left:35%">
                <input class="form-check-input" type="checkbox" value="" id="Digital Signature">
                <label class="form-check-label" for="Digital Signature">
                  Digital Signature
                </label>
              </div>
              <div class="form-check" style=" margin-top: 1%; margin-left:35%">
                <input class="form-check-input" type="checkbox" value="" id="Decipher Only">
                <label class="form-check-label" for="Decipher Only">
                  Decipher Only
                </label>
              </div>
                  <div class="form-check" style=" margin-top: 7%; margin-left:35%">
                <input class="form-check-input" type="checkbox" value="" id="CA certificate">
                <label class="form-check-label" for="CA certificate">
                  CA certificate ?
                </label>
              </div>
		</div>
            <div class="col-sm-6 my-col">
              <img src="../../assets/certificate-icon.png" height="450" width="600" style="margin-top: 10%; margin-left: 15%">
              <button type="button" style="margin-top: 15%; margin-left: 38%;" v-on:click="issueCertificate()"  class="btn btn-secondary btn-lg">Issue new certificate</button>
            </div>
          </div>
    </div>
</template>

<script>
import Swal from 'sweetalert2'
import axios from 'axios'

export default {
    name: "IssueCertificate",

  data() {
    return {
      users: [],
      certificateDTO : {issuerId:1, subjectId:1, validFrom: null, validUntil: null,
			purposes: [], extensions: [], isCA: false},
      userId: "",
    };
  },

  methods: {
    async fetchUsers() {
      const res = await fetch("https://localhost:8090/appUser/getAllUsers");
      const data = await res.json();
      return data;
    },

    checkValidity() {
      console.log(this.certificateDTO)
    if (this.certificateDTO.validFrom === null || this.certificateDTO.validUntil === null || this.certificateDTO.subjectId === null){
      Swal.fire('Error','Please, fill out all necessary data !','error')
      return false;
    }
    if (new Date(this.certificateDTO.validUntil) <= new Date() || new Date(this.certificateDTO.validFrom) > new Date (this.certificateDTO.validUntil)){
      Swal.fire('Error','Please, pick valid date inputs !','error')
      return false
    }
    return true;
  },

  async issueCertificate() {
    this.issuerId = this.userId;
    await this.formatKeyUsages()
    console.log(this.certificateDTO.extensions)
    if (this.checkValidity() === true){
      axios
          .post('https://localhost:8090/certificate/createCertificate', this.certificateDTO)
          .then(response=>{
            if(response.data === true)
            {
              Swal.fire('Success', 'Certificate generated successfully !', 'success')
            }
          })
          .catch(function (error) {
            if (error.response.status == 500) {
              Swal.fire('Error', 'Something went wrong. Please, try again later', 'error')
            }
          })
    }
  },

  async formatKeyUsages() {
    this.certificateDTO.extensions = []
    if (document.getElementById("Encipher Only").checked == true)
      this.certificateDTO.extensions.push(1);
    if (document.getElementById("CRL Signing").checked == true) 
      this.certificateDTO.extensions.push(2);
    if (document.getElementById("Key Certificate Signing").checked == true)
      this.certificateDTO.extensions.push(4);
    if (document.getElementById("Key Agreement").checked == true)
      this.certificateDTO.extensions.push(8);
    if (document.getElementById("Data Encipherment").checked == true) 
      this.certificateDTO.extensions.push(16);
    if (document.getElementById("Key Encipherment").checked == true) 
      this.certificateDTO.extensions.push(32);
    if (document.getElementById("Non-repudiation").checked == true) 
      this.certificateDTO.extensions.push(64);
    if (document.getElementById("Digital Signature").checked == true)  
      this.certificateDTO.extensions.push(128);
    if (document.getElementById("Decipher Only").checked == true)
      this.certificateDTO.extensions.push(32768);
    if (document.getElementById("CA certificate").checked == true)  {
      this.certificateDTO.isCA = true;
    } else {
        this.certificateDTO.isCA = false;
    }
  },

  },

  async mounted() {
    this.users = await this.fetchUsers();
    this.userId = localStorage.getItem("id")
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
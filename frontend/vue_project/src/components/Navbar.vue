<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
      <ul class="navbar-nav">
        <li class="nav-item">
          
          <a class="nav-link" v-if="activeUser.accessToken == null && 
                                    activeUser.id == null" 
                                    href="/register">
                                    Register</a>
        </li>
        <li class="nav-item">
          
          <a class="nav-link" v-if="activeUser.accessToken != null && 
                                    activeUser.id != null && 
                                    activeUser.role == 'ROLE_ADMIN'" 
                                    href="/certificates">
                                    All certificates</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" v-if="activeUser.accessToken != null && 
                                    activeUser.id != null && 
                                    activeUser.role != null" 
                                    href="/certificateadmin">
                                    My certificates</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" v-if="activeUser.accessToken != null && 
                                    activeUser.id != null && 
                                    activeUser.role != null &&
                                    (activeUser.role == 'ROLE_ADMIN' || activeUser.role == 'ROLE_CA')" 
                                    href="/issueCertificate">
                                    Issue new certificate</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" v-if="activeUser.accessToken != null && 
                                    activeUser.id != null && 
                                    activeUser.role != null  &&
                                    (activeUser.role == 'ROLE_ADMIN' || activeUser.role == 'ROLE_CA')"
                                    href="/myIssuedCertificates">
                                    My issued certificates</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" v-if="activeUser.accessToken != null && 
                                    activeUser.id != null && 
                                    activeUser.role != null" 
                                    v-on:click="logOut()">
                                    Log out</a>
        </li>
      </ul>
    </div>
  </div>
</nav>
</template>

<script>
export default {
  name: 'NavBar',
  data() {
    return {
      activeUser: {accessToken: null, id: null, role: null, twoFA: null, expiresIn: null},
    };
  },
 methods: {
   logOut() {
     localStorage.removeItem('accessToken')
     localStorage.removeItem('id')
     localStorage.removeItem('role')
     localStorage.removeItem('twoFA')
     localStorage.removeItem('expiresIn')
     //this.$router.push('/')
     location.reload()
   }
},

mounted(){
      this.activeUser.accessToken=localStorage.getItem('accessToken');
      this.activeUser.id=localStorage.getItem('id');
      this.activeUser.role=localStorage.getItem('role');
      this.activeUser.twoFA=localStorage.getItem('twoFA');
      this.activeUser.expiresIn=localStorage.getItem('expiresIn');
      
    },
}
</script>
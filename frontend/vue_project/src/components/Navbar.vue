<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
      <ul class="navbar-nav">
        <li class="nav-item">
          <a class="nav-link" href="/certificates">All certificates</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/certificateadmin">My certificates</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/issueCertificate">Issue new certificate</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/myIssuedCertificates">My issued certificates</a>
        </li>
      </ul>
    </div>
  </div>
</nav>
</template>

<script>
export default {
  name: 'NavBar',
 methods: {

}
}
</script>
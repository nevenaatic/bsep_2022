<template>
<div class="container-fluid" style="height: 500; width:500">    
	<div class="row">
        
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <br><br>
            <img :src="image" style="margin-left:30%">
            <br><br>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <h2>Scan this QR code using your Authentication App. After you do that, click the button below to type in generated password.</h2>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
    <br><br><br><br>
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <button class="button btn-lg btn-secondary" v-on:click="redirect()" style="margin-left:37%">Next</button>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
</div>

</template>

<script>

export default {
  name: 'ShowQR',
  data() {
	return {
		image: null,
		};
  },
 methods: {
     redirect() {
         this.$router.push("/confirmCode")
     }
 },
    created() {
        this.image = localStorage.getItem('qrCode');
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>

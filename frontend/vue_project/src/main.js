import { createApp } from 'vue';
import App from './App.vue';

//dodato za ruter
import router from "./router";
import "jquery/src/jquery.js";
createApp(App).use(router).mount('#app');
 

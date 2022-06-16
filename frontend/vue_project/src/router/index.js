import { createRouter, createWebHistory } from "vue-router";

import HelloWorld from "../components/HelloWorld";
import AdminProfile from "../components/admin/AdminProfile";
import AllCertificates from "../components/admin/AllCertificates";
import MyCertificateAdmin from "../components/admin/MyCertificateAdmin";
import RegisterComponent from "../components/register/Register";
import EmailVerification from "../components/register/EmailVerification";
import IssueCertificate from "../components/admin/IssueCertificate";
import MyIssuedCertificates from "../components/admin/MyIssuedCertificates";
import ShowQR from "../components/register/ShowQR";
import ConfirmCode from "../components/register/ConfirmCode";

const routes =[
    {
        path: "/",
        name: "HelloWorld",
        component: HelloWorld
    },
    {
        path: "/admin",
        name: "AdminProfile",
        component: AdminProfile
    },
    {
        path: "/certificates",
        name: "AllCertificates",
        component: AllCertificates
    },
    {
        path: "/certificateadmin",
        name: "MyCertificateAdmin",
        component:   MyCertificateAdmin

    },
    {
        path: "/register",
        name: "Register",
        component:   RegisterComponent

    },
    {
        path: "/emailVerification",
        name: "EmailVerification",
        component:   EmailVerification

    },
    {
        path: "/issueCertificate",
        name: "IssueCertificate",
        component:   IssueCertificate

    },
    {
        path: "/myIssuedCertificates",
        name: "MyIssuedCertificates",
        component:   MyIssuedCertificates

    },
    {
        path: "/showQR",
        name: "ShowQR",
        component:   ShowQR

    },
    {
        path: "/confirmCode",
        name: "ConfirmCode",
        component:   ConfirmCode

    },
    
];
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
  });

  router.beforeEach((to, from) => {
    console.log(to.path)
    console.log(from.path)
    if (to.path != "/" && to.path != "/register" && to.path != "/emailVerification" && to.path != "/showQR" && to.path != "/confirmCode") {
        if (localStorage.getItem('id') != null &&
            localStorage.getItem('role') != null &&
            localStorage.getItem('expiresIn') != null &&
            localStorage.getItem('accessToken') != null &&
            localStorage.getItem('twoFA') != null)
            return true
        else
            router.push('/')
    }
    else {
        if (localStorage.getItem('id') == null ||
            localStorage.getItem('role') == null ||
            localStorage.getItem('expiresIn') == null ||
            localStorage.getItem('accessToken') == null ||
            localStorage.getItem('twoFA') == null)
            return true
        else
            router.push('/certificateadmin')
        }
  })
  
  export default router;
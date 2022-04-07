import { createRouter, createWebHistory } from "vue-router";

import HelloWorld from "../components/HelloWorld";
import AdminProfile from "../components/admin/AdminProfile";
import AllCertificates from "../components/admin/AllCertificates";
import RejectedCertificates from "../components/admin/RejectedCertificates";
import MyCertificateAdmin from "../components/admin/MyCertificateAdmin";
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
        path: "/rejected",
        name: "RejectedCertificates",
        component:   RejectedCertificates

    },
    {
        path: "/certificateadmin",
        name: "MyCertificateAdmin",
        component:   MyCertificateAdmin

    }
];
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
  });
  
  export default router;
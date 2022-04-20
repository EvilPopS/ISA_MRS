import AllCottagesView from '../views/AllCottagesView.vue'
import { createRouter, createWebHistory } from 'vue-router';
import CottageOwnerHomePage from '../views/CottageOwnerHomePage.vue';
import ClientProfilePage from '../views/ClientProfilePage.vue';
import InstructorProfilePage from '../views/InstructorProfilePage.vue';
import MainHomePage from '../views/MainHomePage.vue';
import RegistrationPage from '../views/RegistrationPage.vue'
import LoginPage from '../views/LoginPage.vue'
import SearchPage from '../views/SearchPage.vue'

const routes = [
  {
    path: '/CottageOwnerHomePage',
    name: 'CottageOwnerHomePage',
    component: CottageOwnerHomePage
  },
  {
    path: '/InstructorProfilePage',
    name: 'InstructorProfilePage',
    component: InstructorProfilePage
  },
  {
    path: '/ClientProfilePage',
    name: 'ClientProfilePage',
    component: ClientProfilePage
  },
  {
    path: '/AllCottagesView',
    name: 'AllCottagesView',
    component: AllCottagesView
  },
  {
    path: '/',
    name: 'MainHomePage',
    component: MainHomePage
  },
  {
    path: '/Registration',
    name: 'RegistrationPage',
    component: RegistrationPage
  },
  {
    path: '/Login',
    name: 'LoginPage',
    component: LoginPage
  },
  {
    path: '/Search',
    name: 'SearchPage',
    component: SearchPage
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
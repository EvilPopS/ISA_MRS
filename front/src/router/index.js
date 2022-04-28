import { createRouter, createWebHistory } from 'vue-router'
import CottageOwnerHomePage from '../views/CottageOwnerHomePage.vue'
import ClientProfilePage from '../views/ClientProfilePage.vue'
import InstructorProfilePage from '../views/InstructorProfilePage.vue'
import AllCottagesView from '../views/AllCottagesView.vue'
import MainHomePage from '../views/MainHomePage.vue';
import RegistrationPage from '../views/RegistrationPage.vue'
import LoginPage from '../views/LoginPage.vue'
import SearchPage from '../views/SearchPage.vue'
import AdventuresView from '../views/AdventuresView'
import AllReservations from '../views/AllReservations'
import AdventureSearchView from  '../views/AdventureSearchView'
import EntityBasicView from '../views/EntityBasicView.vue'

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
    path: '/AdventuresView',
    name : 'AdventuresView',
    component : AdventuresView
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
  },
  {
    path: '/AllReservations',
    name: 'AllReservations',
    component: AllReservations
  },
  {
    path: '/AdventureSearch',
    name: 'AdventureSearchView',
    component: AdventureSearchView
  },
  {
    path: '/EntityBasicView',
    name: 'EntityBasicView',
    component: EntityBasicView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
import { createRouter, createWebHistory } from 'vue-router';
import CottageOwnerHomePage from '../views/CottageOwnerHomePage.vue';
import ClientProfilePage from '../views/ClientProfilePage.vue';
import InstructorProfilePage from '../views/InstructorProfilePage.vue';
import AllCottagesView from '../views/AllCottagesView.vue';
import MainHomePage from '../views/MainHomePage.vue';
import RegistrationPage from '../views/RegistrationPage.vue';
import LoginPage from '../views/LoginPage.vue';
import SearchPage from '../views/SearchPage.vue';
import AdventuresView from '../views/AdventuresView';
import AllReservations from '../views/AllReservations';
import AdventureSearchView from  '../views/AdventureSearchView';
import AdminProfilePage from '../views/AdminProfileView.vue';
import AdminNotifications from '../views/AdminNotifications.vue';
import OwnersSearch from '../views/OwnersSearch.vue';
import ClientReservationHistory from '../views/ClientReservationHistory';
import ClientUpcomingReservations from '@/views/ClientUpcomingReservations';

const routes = [
  {
    path: '/CottageOwnerHomePage',
    name: 'CottageOwnerHomePage',
    component: CottageOwnerHomePage
  },
  {
    path : '/adminNotifications',
    name : 'AdminNotifications',
    component : AdminNotifications
  },
  {
    path: '/InstructorProfilePage',
    name: 'InstructorProfilePage',
    component: InstructorProfilePage
  },

  {
    path : '/AdminProfilePage',
    name : 'AdminProfilePage',
    component : AdminProfilePage
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
    path: '/OwnersSearch',
    name: 'OwnersSearch',
    component: OwnersSearch
  },
  {
    path: '/ClientReservationHistory',
    name: 'ClientReservationHistory',
    component: ClientReservationHistory
  },
  {
    path: '/ClientUpcomingReservations',
    name: 'ClientUpcomingReservations',
    component: ClientUpcomingReservations
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
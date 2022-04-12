import { createRouter, createWebHistory } from 'vue-router'
import CottageOwnerHomePage from '../views/CottageOwnerHomePage.vue'
import HomeView from '../views/HomeView.vue'
import AboutView from '../views/AboutView.vue'
import CottageOwnerEditProfile from '../components/CottageOwnerEditProfile.vue'

const routes = [
  {
    path: '/CottageOwnerHome',
    name: 'CottageOwnerHome',
    component: CottageOwnerHomePage
  },
  {
    path: '/CottageOwnerEditProfile',
    name: 'CottageOwnerEditProfile',
    component: CottageOwnerEditProfile
  },
  {
    path: '/',
    name: 'HomeView',
    component: HomeView
  },
  {
    path: '/about',
    name: 'AboutView',
    component: AboutView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

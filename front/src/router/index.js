import { createRouter, createWebHistory } from 'vue-router'
import CottageOwnerHomePage from '../views/CottageOwnerHomePage.vue'

const routes = [
  {
    path: '/CottageOwnerHome',
    name: 'CottageOwnerHome',
    component: CottageOwnerHomePage
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

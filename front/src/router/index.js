import { createRouter, createWebHistory } from 'vue-router'
import CottageOwnerHomePage from '../views/CottageOwnerHomePage.vue'
import ClientProfilePage from '../views/ClientProfilePage.vue'
import InstructorProfilePage from '../views/InstructorProfilePage.vue'

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
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
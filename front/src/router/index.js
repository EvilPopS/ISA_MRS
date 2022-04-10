import { createRouter, createWebHistory } from 'vue-router'
import CottageOwnerHomePage from '../views/CottageOwnerHomePage.vue'
import ClientHomePage from '../views/ClientHomePage.vue'
import InstructorHomePage from '../views/InstructorHomePage.vue'

const routes = [
  {
    path: '/CottageOwnerHomePage',
    name: 'CottageOwnerHomePage',
    component: CottageOwnerHomePage
  },
  {
    path: '/InstructorHomePage',
    name: 'InstructorHomePage',
    component: InstructorHomePage
  },
  {
    path: '/ClientHomePage',
    name: 'ClientHomePage',
    component: ClientHomePage
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

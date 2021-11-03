import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../components/Login.vue'
import ElementUI from '../components/ElementUI.vue'
import Home from '../components/Home.vue'
import User from '../components/user/user.vue'
import ItemCat from '../components/items/ItemCat.vue'
import Item from '../components/items/Item.vue'
import AddItem from '../components/items/addItem.vue'

//使用路由机制
Vue.use(VueRouter)
const routes = [
  {path: '/', redirect: '/login'},
  {path: '/login', component: Login},
  {path: '/elementUI', component: ElementUI},
  //实现父子组件嵌套
  {path: '/home',component: Home, children:[
      {path: '/user', component: User},
      {path: '/itemCat', component: ItemCat},
      {path: '/item', component: Item},
      {path: '/item/addItem', component: AddItem}
    ],
  }
]


const router = new VueRouter({
  routes
})

/**
 * 定义路由导航守卫
 * 参数1. to    路由跳转的网址
 * 参数2. from  路由从哪里来
 * 参数3. next  是一个函数,表示放行或重定向
 *              next() 放行
 *              next("/login") 重定向
 * 业务实现:
 *    核心逻辑: 检查是否有token.
 *        如果访问login页面 直接放行.
 *        有token 表示已经登录,放行请求
 *        没有token 表示用户没有登录,重定向到登录页面
 */
router.beforeEach((to,from,next) => {
   if(to.path === "/login"){
     return next()
   }
    //说明用户访问的页面不是login 请求需要校验
    //获取token数据.
    let token = window.sessionStorage.getItem("token")
    //if(token !==null && token.length>0)
    //下列if 解释为: 如果token不为null
    if(token){
      return next()
    }

    next("/login")
})







export default router

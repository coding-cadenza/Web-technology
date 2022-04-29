import { createRouter,createWebHashHistory } from "vue-router";
import Cookies from "js-cookie";
const routes = [
    {
        path:'/',
        redirect:'/login',
    },
    {
        path:'/login',
        name:'login',
        component:()=>import ('@/components/login.vue')
    },
    {
        path:'/register',
        name:'register',
        component:()=>import ('@/components/register.vue')
    },{
        path:'/changePass',
        name:'changePass',
        component:()=>import('@/components/changePass.vue')
    },
    {
        path:'/menu',
        name:'menu',
        redirect:'/menu/home',
        component:()=>import('@/components/menu.vue'),
        children:[
            {
                path:'home',
                name:'home',
                component:()=>import('@/components/home.vue'),
                meta: { menuId: 1 }
            },
            {
                path:'personal',
                name:'personal',
                component:()=>import('@/components/personal.vue'),
                meta: { menuId: 2 }

            },
            {
                path:'userManage',
                name:'userManage',
                component:()=>import('@/components/userManage.vue'),
                meta: { menuId: 3 }
            },
            {
                path:'articleManage',
                name:'articleManage',
                component:()=>import('@/components/articleManage.vue'),
                meta: { menuId: 4 }
            }
        ]
    }
]


const router = createRouter({
    history:createWebHashHistory(),
    routes
})

// 判断token进行拦截
router.beforeEach((to,from,next)=>{
    if(to.fullPath=='/login'||to.fullPath=='/register'||to.fullPath=='/changePass'){
        if(to.fullPath=='/login'){
            if(Cookies.get('token')&&Cookies.get('username')){
                next('/menu')

            }
        }
        next()
        
    }
    if(!Cookies.get('token')){
        next('/login')
    }else{
        next()
    }

})


export default router
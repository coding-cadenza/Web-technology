import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import store from './store'
import VueCookies from 'vue-cookies'
import './assets/css/common.css'
import './assets/iocn/iconfont.css'
import 'element-plus/dist/index.css'
import './assets/iocn/iconfont'
import eventBus from 'vue3-eventbus'


// 创建Vue实例并挂载
const app = createApp(App)
// 将api挂载到原型
app.use(eventBus)
app.use(VueCookies)
app.use(ElementPlus)
app.use(store)
app.use(router)
app.mount('#app')



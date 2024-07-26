import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from "axios";

import '@/assert/css/element.less'
import "flag-icons/css/flag-icons.min.css"
import 'element-plus/theme-chalk/dark/css-vars.css'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import {createPinia} from "pinia";

axios.defaults.baseURL = 'http://localhost:8080'

const app = createApp(App)
const pinia=createPinia()
app.use(pinia)
pinia.use(piniaPluginPersistedstate)
app.use(router)

app.mount('#app')

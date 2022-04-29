import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate"
import page from './module/page'
export default createStore({
    modules:{
        page
    },
    plugins:[createPersistedState()]
})

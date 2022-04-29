const state = () => ({
    bgStatus: 0,
    bgColors:
        [{
            bgHeader: '#409EFF',
            bgMenu: '#337ecc',
            bgHover:'#d9ecff',
            bgFoot: '#409EFF',
            
        },
        {
            bgHeader: '#67C23A',
            bgMenu: '#529b2e',
            bgHover:'#e1f3d8',
            bgFoot: '#67C23A',
           
        },
        {
            bgHeader: '#b1b3b8',
            bgMenu: '#909399',
            bgHover:'#e9e9eb',
            bgFoot: '#b1b3b8',
            
        }
        ]
})

const getters = {
    bgColors: (state) => {
        return state.bgColors[state.bgStatus]
    }
}
const mutations = {
    changeBg(state, bgStatus) {
        state.bgStatus = bgStatus
    }
}

export default {
    state,
    getters,
    mutations
}

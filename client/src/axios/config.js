import axios from 'axios';
import { ElMessage } from 'element-plus';
import { processExpression } from '@vue/compiler-core';
import router from '../router/index'
import Cookies from 'js-cookie';


axios.defaults.crossDomain = true; // 允许跨域
axios.defaults.baseURL = 'http://localhost:5000'
axios.defaults.timeout = 2000
axios.defaults.headers["Content-Type"] = "application/json";
axios.defaults.headers.post["Content-Type"] = "application/json";
axios.defaults.withCredentials = true
/**
 * 封装get方法
 * 封装后直接返回数据就行，不用返回整个res
 * @param {String} url [请求的url地址]
 * @param {Object} data [请求时携带的参数]
 * */
export function get(url, params) {
    return new Promise((resolve, reject) => {
        axios.get(url, { params }
        ).then(res => {
            resolve(res.data)
        })
            .catch(err => {
                if (err.data) {
                    ElMessage.error(err.data.msg)
                } else {
                    console.log(err)
                    ElMessage.error(err.message)
                }
            })
    })
}


/**
 * 封装post方法
 * @param {String} url [请求的url地址]
 * @param {Object} data [请求时携带的参数]
 */

export function post(url, params) {
    return new Promise((resolve, reject) => {
        axios.post(url, params).then(res => {
            resolve(res.data)
        })
            .catch(err => {
                if (err.data) {
                    ElMessage.error(err.data.msg)
                } else {
                    ElMessage.error(err.message)
                }
            })
    })
}


// 请求拦截器
axios.interceptors.request.use(
    (config) => {
        // 如果有token则把token加上
        const token = Cookies.get('token')
        if (token) {
            config.headers.token = token
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);


// 响应拦截器
axios.interceptors.response.use(
    //2xx触发该拦截器
    response => {
        //拦截响应，做统一处理
        if (response.status === 200) {
            return Promise.resolve(response)
        }
        return process.reject(response)
    },
    //接口错误状态处理，也就是说无响应时的处理
    error => {
        if (error.response) {
            if (error.response.status === 401) {
                router.push('/login')
                // 删除cookie中的用户名和token
                Cookies.remove('username')
                Cookies.remove('token')
            }
            return Promise.reject(error.response)
        }
        
        return Promise.reject(error)
    })

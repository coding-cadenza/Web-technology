import { post,get } from "./config";

// 用户登录
export const userLogin = (params)=>post('/login',params)

// 用户注册
export const userRegister = (params)=>post('/register',params)

// 重置密码
export const userChangePass = (params)=>post('/changePass',params)

// 获取所有用户
export const getUserList = (params)=>get('/getUserList',params)

// 删除用户
export const deleteUserById =(params)=>post('/deleteUserById',params)
// 更新用户
export const updateUser = (params)=>post('/updateUser',params)

// 获当前登录用户信息
export const getUserInfo = ()=>get('/getUserInfo')
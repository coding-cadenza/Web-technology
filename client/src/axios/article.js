import {get,post} from './config'

// 获取所有用户的文章数量
export const getArticleOverview = (params)=>get('/getArticleOverview',params)

// 获取文章详细
export const getArticleList = (params)=>get('/getArticleList',params)

// 删除文章
export const deleteArticleById = (params)=>post('/deleteArticle',params)
// 增加文章
export const addArticle = (params)=>post('/addArticle',params)

// 更新文章
export const updateArticle = (params)=>post('/updateArticle',params)
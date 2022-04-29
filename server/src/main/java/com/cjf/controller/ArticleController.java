package com.cjf.controller;


import com.cjf.model.ArticleT;
import com.cjf.service.Impl.ArticletService;
import com.cjf.service.Impl.UsertService;
import com.cjf.utils.ResponseData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class ArticleController {
    @Resource
    private UsertService usertService;

    @Resource
    private ArticletService articletService;

    /**
     * 获取文章总览
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getArticleOverview")
    @ResponseBody
    public ResponseData getArticleOverview(HttpServletRequest request, HttpServletResponse response){
        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));


        ResponseData responseData;
        // 获取所用用户以及其文章数组成的一个list的一个map
        Map<String,Object> data = usertService.getArticleOverview(page,pageSize);
        if(data!=null){
            responseData = new ResponseData(200,"获取文章数量成功");
            responseData.setData(data);
        }else{
            response.setStatus(500);
            responseData = new ResponseData(500,"获取文章数量失败");
        }
        return responseData;
    }

    /**
     * 获取文章列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/getArticleList",method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getUserList(HttpServletRequest request, HttpServletResponse response) {
        int page =Integer.parseInt(request.getParameter("page")); // 获取当前页码
        int pageSize =Integer.parseInt(request.getParameter("pageSize")); // 获取每页最大数量
        String searchValue = request.getParameter("searchValue"); // 搜索条件
        int authorId = Integer.parseInt(request.getParameter("authorId")); // 用户
        System.out.println("转化成功");
        ResponseData responseData = new ResponseData(200,"获取成功");
        Map<String,Object> res =articletService.getArtilceDetail(page,pageSize,searchValue,authorId);
        responseData.setData(res); // 直接重载返回的data
        return responseData;
    }


    /**
     * 删除一篇文章
     * @param params
     * @param response
     * @return
     */
    @RequestMapping(value="/deleteArticle",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData deleteUserById(@RequestBody Map<String,String> params , HttpServletResponse response) {
        int articleId = Integer.parseInt(params.get("articleId"));
        int authorId = Integer.parseInt(params.get("authorId"));
        ResponseData responseData;
        if(articletService.deleteArticleById(articleId,authorId)==true){
            responseData = new ResponseData(200,"删除用户成功");
        }else{
            responseData = new ResponseData(500,"删除用户失败");
        }
        return  responseData;
    }

    /**
     * 创建一篇文章
     * @param articleT
     * @param response
     * @return
     */
    @RequestMapping(value="/addArticle",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData addArticle(@RequestBody ArticleT articleT, HttpServletResponse response) {
        ResponseData responseData;
        if(articletService.addArticle(articleT)==true){
            responseData = new ResponseData(200,"添加文章成功");
        }else{
            responseData = new ResponseData(500,"添加文章失败");
        }
        return  responseData;
    }

    @RequestMapping(value="/updateArticle",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData updateArticle(@RequestBody ArticleT articleT, HttpServletResponse response) {
        ResponseData responseData;
        if(articletService.updateArticle(articleT)==true){
            responseData = new ResponseData(200,"更新文章成功");
        }else{
            responseData = new ResponseData(500,"更新文章失败");
        }
        return  responseData;
    }








}

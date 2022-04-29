package com.cjf.controller;


import com.cjf.model.UserT;
import com.cjf.service.Impl.UsertService;
import com.cjf.utils.ResponseData;
import com.cjf.utils.Token;
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
public class UserController {
    @Resource
    UsertService usertService;

    /**
     * 获取用户数据
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/getUserList",method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getUserList(HttpServletRequest request, HttpServletResponse response) {
        int page =Integer.parseInt(request.getParameter("page")); // 获取当前页码
        int pageSize =Integer.parseInt(request.getParameter("pageSize")); // 获取每页最大数量
        String searchValue = request.getParameter("searchValue"); // 搜索条件
        ResponseData responseData = new ResponseData(200,"获取成功");
        Map<String,Object> res = usertService.getUserList(page,pageSize,searchValue);
        responseData.setData(res); // 直接重载返回的data
        return responseData;
    }
    @RequestMapping(value="/deleteUserById",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData deleteUserById(@RequestBody Map<String,String> params , HttpServletResponse response) {
        int id = Integer.parseInt(params.get("id"));
        ResponseData responseData;
        if(usertService.deleteUserById(id)==true){
            responseData = new ResponseData(200,"删除用户成功");
        }else{
            responseData = new ResponseData(500,"删除用户失败");
        }
        return  responseData;
    }

    /**
     * 更新用户
     * @param userT
     * @param response
     * @return
     */
    @RequestMapping(value="/updateUser",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData deleteUserById(@RequestBody UserT userT , HttpServletResponse response) {
        ResponseData responseData;
        if(usertService.updateUser(userT)){
            responseData = new ResponseData(200,"更新用户成功");
        }else{
            responseData = new ResponseData(500,"服务器出错了");
        }
        return  responseData;

    }
    @RequestMapping(value="/getUserInfo")
    @ResponseBody
    public ResponseData getUseInfo(HttpServletRequest request,HttpServletResponse response){
        String headerToken = request.getHeader("token");  // 获取token
        int id = Token.getIdByToken(headerToken);
        UserT userT = usertService.getUserById(id);
        ResponseData responseData = new ResponseData(200,"获取用户信息成功");
        responseData.putDataValue("userInfo",userT);
        return responseData;

    }





}

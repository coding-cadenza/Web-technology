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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class LoginController {
    @Resource
    private UsertService usertService;

    /**
     * 登录业务
     * @param userT
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/login",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData Login(@RequestBody UserT userT, HttpServletRequest request,HttpServletResponse response)   {
        // 处理登录业务,登录成功返回用户实体
        UserT u = usertService.userLogin(userT);
        ResponseData responseData;
        //登录成功
        if(u!=null){
            // 生成token
            String token = Token.generToken(""+u.getId(),"Jersey-Security-Basic",u.getEmail());
            // 将cookie放到前端
            Cookie cookie = new Cookie("token",token);
            Cookie cookie1 = new Cookie("username",u.getUsername());
            cookie.isHttpOnly(); // 防止被窃取
            cookie1.isHttpOnly();
            cookie.setPath("/");
            cookie1.setPath("/");
            cookie.setMaxAge(60*60*24);
            cookie1.setMaxAge(60*60*24);

            response.addCookie(cookie);
            response.addCookie(cookie1);
            // 用户名一起返回到前端
            responseData = new ResponseData(200,"登录成功");
            responseData.putDataValue("username",u.getUsername());
        }else{ // 登录失败
            response.setStatus(401);
            responseData = new ResponseData(401,"用户名或密码错误");
        }
        return responseData;
    }

    /**
     * 用户注册
     * @param userT
     * @param response
     * @return
     */
    @RequestMapping(value="/register",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData register(@RequestBody UserT userT,HttpServletResponse response) {
        ResponseData responseData; // 返回体
        try{
            usertService.userRegister(userT); // 尝试登录
            responseData = new ResponseData(200,"注册成功");

        }catch (Exception e){
            response.setStatus(409);
            responseData = new ResponseData(409,e.getLocalizedMessage());
        }
        return responseData;
    }

    /**
     * 登录的时候重置密码
     * @param userT
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/changePass",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData changePass(@RequestBody UserT userT, HttpServletRequest request,HttpServletResponse response) {
        ResponseData responseData; // 返回体
        try{
            usertService.userChangePass(userT);
            responseData = new ResponseData(200,"修改密码成功");

        }catch (Exception e){
            response.setStatus(403);
            responseData = new ResponseData(403,e.getLocalizedMessage());
        }
        return responseData;
    }




}


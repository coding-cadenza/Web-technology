package com.cjf.utils;


import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *    拦截器
 * token鉴定
 * */
public class HeaderTokenInterceptor implements HandlerInterceptor {

    private static final Logger LOG = Logger.getLogger(HeaderTokenInterceptor.class);

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object handler) throws Exception {

        ResponseData responseData = null;
        // 获取我们请求头中的token验证字符
        String headerToken = httpServletRequest.getHeader("token");

        if (headerToken == null) {
            // 如果token不存在的话,返回错误信息。
            responseData=new ResponseData(401,"请先登录");
        }
        try {
            // 对token进行更新与验证
            headerToken = Token.updateToken(headerToken);
            LOG.debug("token验证通过,并续期了");
        } catch (Exception e) {
            LOG.debug("token验证出现异常!");
            // 当token验证出现异常返回错误信息,token不匹配
            responseData=new ResponseData(401,"非法的token");
        }

        if(responseData!=null) {
            httpServletResponse.getWriter().write(JSON.toJSONString(responseData));
            return false;
        }else {
            // 刷新前端的cookie
            Cookie cookie = new Cookie("token",headerToken);
            cookie.isHttpOnly(); // 防止被窃取
            httpServletResponse.addCookie(cookie);
            return true;
        }
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
    }
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }


}
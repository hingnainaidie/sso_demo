package com.sso.login.controller;

import com.sso.login.demin.User;
import com.sso.login.utils.LoginCacheUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 页面跳转逻辑
 */
@Controller
@RequestMapping("/view")
public class ViewController {
    /**
     * 跳转到登陆界面
     * @return
     */
    @GetMapping("/login")
    public String toLogin(@RequestParam(required = false,defaultValue = "") String target,
                          HttpSession session, @CookieValue(required = false,value = "TOKEN")Cookie cookie){

        if(StringUtils.isEmpty(target)){
            target="http://www.codeshop.com:9010";
        }
        //如果是已经登陆的用户再次访问登陆界面时，就要重定向
        if(cookie!=null){
            String value=cookie.getValue();
            User user= LoginCacheUtil.loginUser.get(value);
            if(user!=null){
                return "redirect:"+target;
            }
        }
        //TODO:要做target地址是否合法校验
        //重定向地址
        session.setAttribute("target",target);
        return "login";
    }
    @GetMapping("/unlogin")
    public String outLogin(@RequestParam(required = false,defaultValue = "") String target,
                           HttpServletResponse response,
                           @CookieValue(required = false,value = "TOKEN")Cookie cookie){

        if(StringUtils.isEmpty(target)){
            target="http://www.codeshop.com:9010";
        }
        if(cookie!=null){
            String token=cookie.getValue();
            LoginCacheUtil.loginUser.remove(token);
            String token1= UUID.randomUUID().toString();
            Cookie cookie1=new Cookie("TOKEN",token1);
            cookie1.setDomain("codeshop.com");
            cookie1.setMaxAge(0);
            response.addCookie(cookie1);
            return "redirect:"+target;
        }
        return "login";
    }
}

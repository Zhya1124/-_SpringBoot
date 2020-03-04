package com.wldemo.demo.controller;

import com.wldemo.demo.mapper.UserMapper;
import com.wldemo.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/")
    public String index(HttpServletRequest request){
        try{
            Cookie[] cookies = request.getCookies();//从客户端的请求里拿cookies
            if(cookies != null){
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equals("token")){//找一个叫token 的cookie
                        String token = cookie.getValue();
                        User user = userMapper.findByToken(token);
                        if(user != null){
                            request.getSession().setAttribute("user", user);
                        }
                        break;//找到了user后退出
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return "index";
    }
}

package com.wldemo.demo.controller;

import com.wldemo.demo.dto.PaginationDTO;
import com.wldemo.demo.dto.QuestionDTO;
import com.wldemo.demo.mapper.QuestionMapper;
import com.wldemo.demo.mapper.UserMapper;
import com.wldemo.demo.model.Question;
import com.wldemo.demo.model.User;
import com.wldemo.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(value = "page",defaultValue = "1")Integer page,
                        @RequestParam(value = "size",defaultValue = "5")Integer size){
        try{//利用cookie保持首页登录的代码
            Cookie[] cookies = request.getCookies();//从客户端的请求里拿cookies
            if(cookies != null && cookies.length!=0){ //不判空的话，如果清除cookie会出现问题
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

            PaginationDTO pagination = questionService.list(page,size);
            model.addAttribute("pagination",pagination);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "index";
    }
}

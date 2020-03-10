package com.wldemo.demo.controller;

import com.wldemo.demo.dto.PaginationDTO;
import com.wldemo.demo.mapper.UserMapper;
import com.wldemo.demo.model.User;
import com.wldemo.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(value = "page",defaultValue = "1")Integer page,
                          @RequestParam(value = "size",defaultValue = "5")Integer size) {
        Cookie[] cookies = request.getCookies();//从客户端的请求里拿cookies
        User user = null;
        if(cookies != null && cookies.length!=0){ //不判空的话，如果清除cookie会出现问题
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){//找一个叫token 的cookie
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if(user != null){
                        request.getSession().setAttribute("user", user);//拿到user的cookie后放进session里
                    }
                    break;//找到了user后退出
                }
            }
        }
        if (user==null){    //没有用户就回到主页
            return "redirect:/";
        }
        if ("questions".contains(action)) { //通过路由变化和model控制标签的active
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        }else if("replies".contains(action)){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination",paginationDTO);
        return "profile";
    }
}

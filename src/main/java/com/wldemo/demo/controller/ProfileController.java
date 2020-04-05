package com.wldemo.demo.controller;

import com.wldemo.demo.dto.PaginationDTO;
import com.wldemo.demo.model.User;
import com.wldemo.demo.service.NotificationService;
import com.wldemo.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(value = "page",defaultValue = "1")Integer page,
                          @RequestParam(value = "size",defaultValue = "5")Integer size) {
        User user = (User) request.getSession().getAttribute("user");//因为cookie已经放在拦截器里做了从session里获取user
        if (user==null){    //没有用户就回到主页
            return "redirect:/";
        }
        if ("questions".contains(action)) { //通过路由变化和model控制标签的active
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);//我的问题页
            model.addAttribute("pagination",paginationDTO);
        }else if("replies".contains(action)){
            PaginationDTO paginationDTO = notificationService.list(user.getId(),page,size);//查出我的通知页
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            model.addAttribute("pagination",paginationDTO);

        }

        return "profile";
    }
}

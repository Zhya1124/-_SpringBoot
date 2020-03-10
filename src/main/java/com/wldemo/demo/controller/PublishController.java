package com.wldemo.demo.controller;

import com.wldemo.demo.mapper.QuestionMapper;
import com.wldemo.demo.mapper.UserMapper;
import com.wldemo.demo.model.Question;
import com.wldemo.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            HttpServletRequest request,
                            Model model){
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag); //做三个不为空验证，并显示到前端，就是出错了也不能删掉之前编辑的内容
        if(title == null||title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description == null||description==""){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if(tag == null||tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        User user = null;
        Cookie[] cookies = request.getCookies();//从客户端的请求里拿cookies
        if(cookies != null && cookies.length!=0){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){//找一个叫token 的cookie
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if(user != null){
                        request.getSession().setAttribute("user", user);
                    }
                    break;//找到了user后退出
                }
            }
        }
        if(user == null){
            model.addAttribute("error","用户未登录");
            return "publish";//如果寻找用户失败，则跳转回发布页面不变
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        return "redirect:/";
    }
}

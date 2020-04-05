package com.wldemo.demo.controller;

import com.wldemo.demo.dto.NotificationDTO;
import com.wldemo.demo.enums.NotificationTypeEnum;
import com.wldemo.demo.mapper.NotificationMapper;
import com.wldemo.demo.model.Notification;
import com.wldemo.demo.model.User;
import com.wldemo.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String notifier(HttpServletRequest request,
                           @PathVariable(name="id")Long id){
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);//传入通知id和用户，不传用户那么谁都可以read喽
        if(NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()
                || NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()){
            return "redirect:/question/" + notificationDTO.getOuterid();//这里有问题：outerid是根据评论的parentid来设置的，那么二级评论的parentid就是评论而不是问题，这样问题跳转就不对了
        }else{
            return "redirect:/";
        }

    }
}

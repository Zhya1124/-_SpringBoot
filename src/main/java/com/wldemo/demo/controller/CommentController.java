package com.wldemo.demo.controller;

import com.wldemo.demo.dto.CommentCreateDTO;
import com.wldemo.demo.dto.ResultDTO;
import com.wldemo.demo.exception.CustomizeErrorCode;
import com.wldemo.demo.model.Comment;
import com.wldemo.demo.model.User;
import com.wldemo.demo.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @ResponseBody   //把下面转为json
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){ //拿到json去映射在dto
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){   //登录错误处理，用结果返回dto
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);//让自定义的异常错误码在这里也可以用
        }
        if(commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }
}

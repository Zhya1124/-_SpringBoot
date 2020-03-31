package com.wldemo.demo.controller;

import com.wldemo.demo.dto.CommentCreateDTO;
import com.wldemo.demo.dto.CommentDTO;
import com.wldemo.demo.dto.ResultDTO;
import com.wldemo.demo.enums.CommentTypeEnum;
import com.wldemo.demo.exception.CustomizeErrorCode;
import com.wldemo.demo.model.Comment;
import com.wldemo.demo.model.User;
import com.wldemo.demo.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        comment.setCommentCount(0);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }
    @ResponseBody   //把下面转为json
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);//传入类型是评论下的评论
        return ResultDTO.okOf(commentDTOS);//返回包含评论列表的状态json
    }
}

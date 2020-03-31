package com.wldemo.demo.mapper;

import com.wldemo.demo.model.Comment;
import com.wldemo.demo.model.CommentExample;
import com.wldemo.demo.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}
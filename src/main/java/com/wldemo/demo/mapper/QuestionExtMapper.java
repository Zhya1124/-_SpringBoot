package com.wldemo.demo.mapper;

import com.wldemo.demo.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
}
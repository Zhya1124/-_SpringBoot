package com.wldemo.demo.mapper;

import com.wldemo.demo.dto.QuestionDTO;
import com.wldemo.demo.dto.QuestionQueryDTO;
import com.wldemo.demo.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
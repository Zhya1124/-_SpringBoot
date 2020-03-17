package com.wldemo.demo.service;

import com.wldemo.demo.dto.PaginationDTO;
import com.wldemo.demo.dto.QuestionDTO;
import com.wldemo.demo.mapper.QuestionMapper;
import com.wldemo.demo.mapper.UserMapper;
import com.wldemo.demo.model.Question;
import com.wldemo.demo.model.QuestionExample;
import com.wldemo.demo.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {  //组装user和question
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();//分页DTO
        Integer totalPage;
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());//查询获取问题总数
        //该用户总页数(之前在paginationService里现在单独拿出来)
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //传入page越界处理
        if(page < 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);//计算好对应的标志和页数
        Integer offset = size * (page - 1);//计算offset
        //List<Question> questionList = questionMapper.list(offset, size);//查询出的是一页需要的量
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {//查每个问题的user并加入到dto中
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);//拷贝一个对象的全部属性到另一个对象，避免写太多set
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);//给DTO所需的查询量

        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {//查出某个id对应作者的问题
        PaginationDTO paginationDTO = new PaginationDTO();//分页DTO
        Integer totalPage;
        //Integer totalCount = questionMapper.countByUserId(userId);//查询某用户的问题总数
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(example);
        //该用户总页数(之前在paginationService里现在单独拿出来)
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //传入page越界处理
        if(page < 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);//计算好对应的标志和页数
        //5*(i-1)
        Integer offset = size * (page - 1);//计算offset
        //List<Question> questionList = questionMapper.listByUserId(userId,offset, size);//查询出的是一页需要的量
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {//查每个问题的user并加入到dto中
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);//拷贝一个对象的全部属性到另一个对象，避免写太多set
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);//给DTO所需的查询量

        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);//insert插入有点问题，浏览回复点赞三个数量为null，改成这样就好了
        }else{
            //更新
            //question.setGmtModified(System.currentTimeMillis());
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());//更新原问题同id的问题
            questionMapper.updateByExampleSelective(updateQuestion, example);
        }
    }
}

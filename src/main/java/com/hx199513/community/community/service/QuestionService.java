package com.hx199513.community.community.service;

import com.hx199513.community.community.dto.PaginationDTO;
import com.hx199513.community.community.dto.QuestionDTO;
import com.hx199513.community.community.exception.CustomizeErrorCode;
import com.hx199513.community.community.exception.CustomizeException;
import com.hx199513.community.community.mapper.QuestionMapper;
import com.hx199513.community.community.mapper.UserMapper;
import com.hx199513.community.community.model.Question;
import com.hx199513.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO=new PaginationDTO();
        Integer totalPage;
        Integer totalCount=questionMapper.count();
        if(totalCount%size==0){
            totalPage=totalCount/size;
        }else {
            totalPage=totalCount/size+1;
        }

        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }
        paginationDTO.setPagenation(totalPage,page);


        //size*(page-1)
        Integer offset=size*(page-1);
        List<Question> questions=questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList=new ArrayList<>();

        for (Question question:questions){
           User user= userMapper.findById(question.getCreator());
           QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
            PaginationDTO paginationDTO=new PaginationDTO();
            Integer totalPage;
            Integer totalCount=questionMapper.countByUserId(userId);
            if(totalCount%size==0){
                totalPage=totalCount/size;
            }else {
                totalPage=totalCount/size+1;
            }

            if(page<1){
                page=1;
            }
            if(page>totalPage){
                page=totalPage;
            }

            paginationDTO.setPagenation(totalPage,page);

            //size*(page-1)
            Integer offset=size*(page-1);
            List<Question> questions=questionMapper.listByUserId(userId,offset,size);
            List<QuestionDTO> questionDTOList=new ArrayList<>();

            for (Question question:questions){
                User user= userMapper.findById(question.getCreator());
                QuestionDTO questionDTO=new QuestionDTO();
                BeanUtils.copyProperties(question,questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
            paginationDTO.setQuestions(questionDTOList);
            return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question=questionMapper.getById(id);
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESSTION_NOT_FOUND);
        }
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user= userMapper.findById(question.getCreator());
        questionDTO.setUser(user);

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            //如果id==null,说明第一次创建问题，
            // 则调用questionMapper.create（）
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else {
            //更新
            question.setGmtModified(question.getGmtCreate());
            questionMapper.update(question);
        }
    }


    public synchronized void incView(Integer id) {
        Question question=new Question();
        question.setId(id);
        question.setViewCount(1);
        questionMapper.updateView(question);
    }
}

package com.hx199513.community.community.service;

import com.hx199513.community.community.dto.PaginationDTO;
import com.hx199513.community.community.dto.QuestionDTO;
import com.hx199513.community.community.exception.CustomizeErrorCode;
import com.hx199513.community.community.exception.CustomizeException;
import com.hx199513.community.community.mapper.QuestionMapper;
import com.hx199513.community.community.mapper.UserMapper;
import com.hx199513.community.community.model.Question;
import com.hx199513.community.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(long userId, Integer page, Integer size) {
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
            paginationDTO.setData(questionDTOList);
            return paginationDTO;
    }

    //根据id获取QuestionDTO
    public QuestionDTO getById(Long id) {
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

    //创建或者更新问题
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

    //阅读数增加
    public synchronized void incView(Long id) {
        Question question=new Question();
        question.setId(id);
        question.setViewCount((long) 1);
        questionMapper.updateView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags=StringUtils.split(queryDTO.getTag(),",");
        String regexpTag= Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question=new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions=questionMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS=questions.stream().map(q->{
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return  questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}

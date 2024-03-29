package com.hx199513.community.community.service;

import com.hx199513.community.community.dto.CommentDTO;
import com.hx199513.community.community.enums.CommentTypeEnum;
import com.hx199513.community.community.enums.NotificationStatusEnum;
import com.hx199513.community.community.enums.NotificationTypeEnum;
import com.hx199513.community.community.exception.CustomizeErrorCode;
import com.hx199513.community.community.exception.CustomizeException;
import com.hx199513.community.community.mapper.CommentMapper;
import com.hx199513.community.community.mapper.NotificationMapper;
import com.hx199513.community.community.mapper.QuestionMapper;
import com.hx199513.community.community.mapper.UserMapper;
import com.hx199513.community.community.model.Comment;
import com.hx199513.community.community.model.Notification;
import com.hx199513.community.community.model.Question;
import com.hx199513.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment,User commentator) {
        if(comment.getParentId()==null||comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType()==null|| CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        Question question=new Question();
            if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
                //回复评论
                //判断所回复的评论是否还在
                Comment dbcomment=commentMapper.selectById(comment.getParentId());
                question=questionMapper.getById(dbcomment.getParentId());
                if(dbcomment==null){
                    throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
                    //判断所回复的评论所在的问题是否还在
                }else if(question==null){
                    throw new CustomizeException(CustomizeErrorCode.QUESSTION_NOT_FOUND);
                    }
                commentMapper.insert(comment);
                //创建通知
                createNotify(comment, dbcomment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT);

                //增加评论数
                dbcomment.setCommentCount((long) 1);
                commentMapper.updateCommentCount(dbcomment);
            }else if (comment.getType()==CommentTypeEnum.QUESTION.getType()){
                //回复问题
                //判断所评论的问题是否还在
                question=questionMapper.getById(comment.getParentId());
                if(question==null){
                    throw new CustomizeException(CustomizeErrorCode.QUESSTION_NOT_FOUND);
                }
                commentMapper.insert(comment);
                //创建通知
                createNotify(comment,question.getCreator(),commentator.getName(),question.getTitle(),NotificationTypeEnum.REPLY_QUESTION);
            }
            question.setCommentCount((long) 1);
            questionMapper.updateComment(question);
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType) {
        Notification notification=new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterId(comment.getParentId());
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByType(Long id, Integer type) {
        List<Comment> comments=commentMapper.selectByType(id,type);
        if(comments.size()==0){
            return  new ArrayList<>();
        }
        //获取去重的评论人
        Set<Long> commentators=comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
       //获取评论人并转换成Map
        List<User> users=new ArrayList<>();
        for(Long commentator:commentators){
            User user=userMapper.findById(commentator);
            users.add(user);
        }
        Map<Long,User> userMap=users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
       //转换comment为commentDTO
        List<CommentDTO> commentDTOS=comments.stream().map(comment ->{
            CommentDTO commentDTO=new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}

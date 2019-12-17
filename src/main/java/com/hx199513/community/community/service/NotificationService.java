package com.hx199513.community.community.service;

import com.hx199513.community.community.dto.NotificationDTO;
import com.hx199513.community.community.dto.PaginationDTO;
import com.hx199513.community.community.enums.NotificationTypeEnum;
import com.hx199513.community.community.mapper.NotificationMapper;
import com.hx199513.community.community.mapper.UserMapper;
import com.hx199513.community.community.model.Notification;
import com.hx199513.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO=new PaginationDTO<>();
        Integer totalPage;
        Integer totalCount=notificationMapper.countByReceiver(userId);
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
        List<Notification> notifications=notificationMapper.listByUserId(userId,offset,size);

        if(notifications.size()==0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOS=new ArrayList<>();
        for(Notification notification:notifications){
            NotificationDTO notificationDTO=new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setType(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    public Integer unreadCount(Long userId) {
        return notificationMapper.countByReceiver(userId);
    }
}

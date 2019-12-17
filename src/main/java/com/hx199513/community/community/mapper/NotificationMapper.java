package com.hx199513.community.community.mapper;

import com.hx199513.community.community.model.Notification;
import com.hx199513.community.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotificationMapper {
    @Insert("insert into notification(notifier,receiver,outerId,type,gmt_create,status,notifier_name,outer_title) values (#{notifier},#{receiver},#{outerId},#{type},#{gmtCreate},#{status},#{notifierName},#{outerTitle})")
    void insert(Notification notification);

    @Select("select count(1)  from notification where receiver=#{userId}")
    Integer countByReceiver(@Param("userId")Long userId);

    @Select("select * from notification where receiver = #{userId} order by gmt_create desc limit #{offset},#{size}")
    List<Notification> listByUserId(@Param("userId") Long userId,@Param(value = "offset") Integer offset, @Param(value = "size")Integer size);
}

package com.hx199513.community.community.mapper;

import com.hx199513.community.community.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("Insert into comment(parent_id,content,type,gmt_create,gmt_modified,commentator,) values (#{parentId},#{content},#{type},#{gmtCreate},#{gmtModified},#{commentator})")
    void insert(Comment comment);
    @Select("select * from comment where id=#{id}")
    Comment selectById(@Param("id") Integer parentId);

    @Select("select * from comment where parent_id=#{id} and type=#{type} order by gmt_create desc")
    List<Comment> selectByType(@Param("id") Integer id,@Param("type") Integer type);
}

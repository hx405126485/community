package com.hx199513.community.community.mapper;

import com.hx199513.community.community.model.Comment;
import com.hx199513.community.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("Insert into comment(parent_id,content,type,gmt_create,gmt_modified,commentator,) values (#{parentId},#{content},#{type},#{gmtCreate},#{gmtModified},#{commentator})")
    void insert(Comment comment);
    @Select("select * from comment where id=#{id}")
    Comment selectById(@Param("id") Long parentId);

    @Select("select * from comment where parent_id=#{id} and type=#{type} order by gmt_create desc")
    List<Comment> selectByType(@Param("id") Long id,@Param("type") Integer type);

    @Update("update comment set comment_count=comment_count+#{commentCount,jdbcType=INTEGER} where id=#{id}")
    void updateCommentCount(Comment comment);
}

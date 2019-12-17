package com.hx199513.community.community.mapper;

import com.hx199513.community.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description," +
            "gmt_create,gmt_modified,creator,tag) values(#{title}," +
            "#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);
    @Select("select * from question order by gmt_create desc limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} order by gmt_create desc limit #{offset},#{size}")
    List<Question> listByUserId(@Param("userId") Long userId,@Param(value = "offset") Integer offset, @Param(value = "size")Integer size);

    @Select("select count(1)  from question where creator=#{userId}")
    Integer countByUserId(@Param("userId")Long userId);

    @Select("select * from question where id=#{id}")
    Question getById(@Param("id") Long id);

    @Update("update question set title=#{title},description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id=#{id}")
    void update(Question question);

    @Update("update question set view_count=view_count+#{viewCount,jdbcType=INTEGER} where id=#{id}")
    void updateView(Question question);

    @Update("update question set comment_count=comment_count+#{commentCount,jdbcType=INTEGER} where id=#{id}")
    void updateComment(Question question);

    @Select("select id,TITLE,TAG from QUESTION where id != #{id} and tag REGEXP #{tag}")
    List<Question> selectRelated(Question question);
}

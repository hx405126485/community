package com.hx199513.community.community.model;

import lombok.Data;

/**
 * @Data lombo依赖支持（自动添加get，set，toString，hashCode方法）
 */
@Data
public class Question {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
}

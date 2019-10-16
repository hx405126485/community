package com.hx199513.community.community.dto;

import lombok.Data;

/**
 * 从accesstoken获取的个人信息里所需要的三个信息
 * name 名字
 * id   序号
 * bio  描述
 *
 * @Data lombo依赖支持（自动添加get，set，toString，hashCode方法）
 * **/
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;
}

package com.hx199513.community.community.dto;

import lombok.Data;

/**
 * 获取AccessToken所需要的五个参数
 *
 * @Data lombo依赖支持（自动添加get，set，toString，hashCode方法）
 * **/
@Data
public class AcesstokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}

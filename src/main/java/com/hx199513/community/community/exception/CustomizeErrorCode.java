package com.hx199513.community.community.exception;

public enum CustomizeErrorCode implements ICutomizeErrorCode {
    QUESSTION_NOT_FOUND(2001,"您找的问题不在了"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"未登陆"),
    SYS_ERROR(2004,"服务器出错"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"回复的评论不存在"),
    CONTENT_IS_EMPTY(2007,"输入内容不能为空");

    @Override
    public String getMessage(){return message;}
    @Override
    public Integer getCode(){return code;}

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}

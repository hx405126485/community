package com.hx199513.community.community.enums;

public enum NotificationStatusEnum {
    UNREAD(0),RED(1)
    ;
    private int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

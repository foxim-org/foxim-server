package com.hnzz.commons.base.enums.userenums;

import lombok.Getter;

@Getter
public enum ContactStatus {
//    private String status;              //PENDING | ACCEPTED | REJECTED | BLOCKED

    //等待
    PENDING("等待","PENDING"),
    //接受
    ACCEPTED("接受","ACCEPTED"),
    //拒绝
    REJECTED("拒绝","REJECTED"),
    //拉黑
    BLOCKED("拉黑","BLOCKED"),
    //非好友
    NOTAFRIEND("非好友","NOTAFRIEND");

    private final String msg;

    private final String code;

    ContactStatus(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }
}
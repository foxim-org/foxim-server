package com.hnzz.common;

import lombok.Getter;

/**
 * @author HB
 * @Classname UserStatusText
 * @Date 2023/1/4 12:37
 * @Description TODO
 */
@Getter
public enum UserStatusText {

//
//    public static final String LINE_ON = "在线";
//    public static final String LINE_OFF = "下线";
    LINE_ON("在线"),
    LINE_OFF("下线");

    private String code;
    private UserStatusText(String code){
        this.code=code;
    }

}

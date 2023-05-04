package com.hnzz.commons.base.enums.userenums;

import lombok.Getter;

/**
 * @author HB
 * @Classname UserStatusText
 * @Date 2023/1/4 12:37
 * @Description TODO
 */
@Getter
public enum UserStatusText {

    /**
     * 用户状态
     */
    LINE_ON("在线"),
    LINE_OFF("下线");

    private final String code;
    private UserStatusText(String code){
        this.code=code;
    }

}

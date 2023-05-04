package com.hnzz.commons.base.result;

import com.hnzz.commons.base.enums.AppEnums;


/**
 * @author HB
 * @Classname ResultCodes
 * @Date 2023/1/3 15:30
 * @Description TODO
 */
public enum ResultCodes implements AppEnums {
    /**
     * 暂时只有这些
     */
    SUCCESS(200,"操作成功"),
    ERROR(500,"操作失败"),
    AUTH_ERROR(501,"用户未登录")
    ;

    private final Integer code;

    private final String msg;

    ResultCodes(Integer code,String msg){
        this.code=code;
        this.msg =msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}

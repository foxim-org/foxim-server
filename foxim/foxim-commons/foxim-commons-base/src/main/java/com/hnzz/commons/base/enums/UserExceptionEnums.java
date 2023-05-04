package com.hnzz.commons.base.enums;

/**
 * @author HB
 * @Classname UserExceptionEnums
 * @Date 2023/1/10 14:28
 * @Description TODO
 */
public enum UserExceptionEnums implements AppEnums{
    // 用户不存在
    USER_NOTEXIST_ERROR(301,"该用户不存在"),
    // 用户名或密码错误
    USER_NAMEORPWD_ERROR(302,"用户名或密码有误"),
    // 用户验证码输入有误
    USER_CHECK_CODE_ERROR(303,"用户验证码输入有误")
    ;

    private final Integer code;
    private final String msg;
    private UserExceptionEnums(Integer code,String msg){
        this.code=code;
        this.msg=msg;
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

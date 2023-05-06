package com.hnzz.commons.base.enums.system;

/**
 * @author HB on 2023/5/5
 * TODO 用户校验方式枚举
 */
public enum UserValidTypeEnum {
    // 手机号+密码
    MOBILE_PASSWORD,
    // 用户名+密码
    USERNAME_PASSWORD,
    // 邮箱+密码
    EMAIL_PASSWORD,
    // 手机号+验证码
    MOBILE_CODE,
    // 邮箱+验证码
    EMAIL_CODE;
}

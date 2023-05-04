package com.hnzz.commons.base.enums;

/**
 * @author HB on 2023/3/2
 * TODO
 */

public enum AdminRoleEnum {

    /**
     * 超级管理员
     */
    TERMINAL_SUPER_ADMIN("终端超级管理员"),
    SUPER_ADMIN("超级管理员"),
    PLATFORM_SUPER_ADMIN("平台超级管理员"),
    PLATFORM_ADMIN("平台管理员");

    private final String roleName;

    AdminRoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

}

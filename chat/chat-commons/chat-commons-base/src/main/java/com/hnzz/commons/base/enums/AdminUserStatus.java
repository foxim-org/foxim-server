package com.hnzz.commons.base.enums;


/**
 * @author HB on 2023/3/2
 * TODO 管理员状态
 */
public enum AdminUserStatus {
    /**
     * 管理员注册后的状态
     */
    PENDING_REVIEW("待审核"),
    APPROVED("审核通过"),
    DISABLED("禁用");

    private final String status;

    AdminUserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

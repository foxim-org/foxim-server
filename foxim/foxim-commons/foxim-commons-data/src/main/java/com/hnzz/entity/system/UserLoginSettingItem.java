package com.hnzz.entity.system;

import lombok.Data;

/**
 * @author HB on 2023/5/5
 * TODO 用户登录选项
 */
@Data
public class UserLoginSettingItem {
    /**
     * @see com.hnzz.commons.base.enums.system.UserValidTypeEnum
     */
    private String userLoginType;
    private Boolean used;
}

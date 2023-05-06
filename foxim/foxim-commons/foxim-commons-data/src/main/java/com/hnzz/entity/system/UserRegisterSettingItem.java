package com.hnzz.entity.system;

import lombok.Data;

/**
 * @author HB on 2023/5/5
 * TODO
 */
@Data
public class UserRegisterSettingItem {
    /**
     * @see com.hnzz.commons.base.enums.system.UserValidTypeEnum
     */
    private String userRegisterType;
    private boolean used;
}

package com.hnzz.entity.system;

import com.hnzz.commons.base.enums.system.UserValidTypeEnum;
import lombok.Data;

import java.util.Map;

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

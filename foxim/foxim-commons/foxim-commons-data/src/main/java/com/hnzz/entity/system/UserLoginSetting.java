package com.hnzz.entity.system;

import lombok.Data;

import java.util.List;

/**
 * @author HB on 2023/5/5
 * TODO 用户登录选项集合
 */
@Data
public class UserLoginSetting {

    private List<UserLoginSettingItem> userLoginType;
}

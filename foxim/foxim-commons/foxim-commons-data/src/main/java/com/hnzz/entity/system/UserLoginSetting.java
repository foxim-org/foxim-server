package com.hnzz.entity.system;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author HB on 2023/5/5
 * TODO 用户登录选项集合
 */
@Data
public class UserLoginSetting {

    private Map<String,Boolean> userLoginType;
}

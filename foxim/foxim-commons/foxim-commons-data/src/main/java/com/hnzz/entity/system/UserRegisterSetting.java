package com.hnzz.entity.system;

import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author HB on 2023/5/5
 * TODO
 */
@Data
public class UserRegisterSetting {

    private List<UserRegisterSettingItem> userRegisterType;

    public boolean canRegister(String registerType){
        if (userRegisterType!=null && !userRegisterType.isEmpty()){
            for (UserRegisterSettingItem item : userRegisterType){
                if (Objects.equals(item.getUserRegisterType(), registerType) && item.isUsed()) {
                    return true;
                }
            }
        }
        return false;
    }

}

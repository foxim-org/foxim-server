package com.hnzz.service;

import com.hnzz.entity.AboutWith;
import com.hnzz.entity.system.Setting;
import com.hnzz.entity.system.UserRegisterSetting;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author HB on 2023/5/5
 * TODO 系统设置业务抽象
 */
public interface SettingService {

    Setting saveSetting(String settingName, String settingValue);

    ResponseEntity getSetting(String name);

    AboutWith saveAboutWith(String value);

    Object findAboutWith(String aboutWithId);

    ResponseEntity getSet(String name);

    void saveLogoAvatarUrl(String userId,MultipartFile file);

    Setting lookLogoAvatarUrl();
}

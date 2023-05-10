package com.hnzz.service;

import com.hnzz.entity.AboutWith;
import com.hnzz.entity.Navigation;
import com.hnzz.entity.system.Setting;
import com.hnzz.entity.system.UserRegisterSetting;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author HB on 2023/5/5
 * TODO 系统设置业务抽象
 */
public interface SettingService {

    Setting saveSetting(String settingName, String settingValue);

    ResponseEntity getSetting(String name);

    AboutWith saveAboutWith(String userId,String value);

    Object findAboutWith();

//    ResponseEntity getSet(String name);

    void saveLogoAvatarUrl(String userId,MultipartFile file);

    Setting lookLogoAvatarUrl();

    void saveUserAvatarUrl(String userId, MultipartFile file);

    Setting lookUserAvatarUrl();

    void saveNavigation(MultipartFile img, MultipartFile imgBright, String routing, String name);

    List<Navigation> lookNavigation();

    Navigation updateNavigation(String id,MultipartFile img, MultipartFile imgBright, String routing, String name);
}

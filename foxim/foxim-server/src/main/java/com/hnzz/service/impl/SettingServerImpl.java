package com.hnzz.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.hnzz.commons.base.enums.system.SettingEnum;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.dao.SettingDao;
import com.hnzz.entity.AboutWith;
import com.hnzz.entity.User;
import com.hnzz.entity.system.Setting;
import com.hnzz.entity.system.UserLoginSetting;
import com.hnzz.entity.system.UserRegisterSetting;
import com.hnzz.service.SettingService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author HB on 2023/5/5
 * TODO 系统设置业务实现
 */
@Service
public class SettingServerImpl implements SettingService {

    @Resource
    private MongoTemplate template;

    @Resource
    private SettingDao settingDao;

    @Override
    public Setting saveSetting(String settingName , String settingValue){
        Setting setting = settingDao.getSettingByName(settingName);
        if (setting==null){
            setting = new Setting();
            SettingEnum settingEnum = SettingEnum.valueOf(settingName);
            setting.setName(settingEnum.name());
            setting.setValue(settingValue);
            return settingDao.addSetting(setting);
        }else {
            setting.setValue(settingValue);
            return settingDao.getMongoTemplate().save(setting);
        }
    }


    @Override
    public ResponseEntity getSetting(String name) {
        SettingEnum settingEnum = SettingEnum.valueOf(name);
        Setting setting = settingDao.getSettingByName(name);
        switch (settingEnum) {
            case LOGIN_SETTING:
                return setting==null ?
                    ResponseEntity.ok(new UserLoginSetting()) :
                    ResponseEntity.ok(JSONUtil.toBean(setting.getValue() , UserLoginSetting.class));
            case REGISTER_SETTING:
                return setting==null ?
                        ResponseEntity.ok(new UserRegisterSetting()) :
                        ResponseEntity.ok(JSONUtil.toBean(setting.getValue() , UserRegisterSetting.class));
            case WITH_USER_SETTING:
                return setting==null ?
                        ResponseEntity.ok("") :
                        ResponseEntity.ok(setting.getValue());
            default:
                throw new AppException("不存在名为"+name+"的配置");
        }
    }

    @Override
    public AboutWith saveAboutWith(String value) {
        AboutWith aboutWith=new AboutWith();
        aboutWith.setCreatedAt(new Date())
                 .setUpdateAt(new Date())
                 .setValue(value);

        return template.save(aboutWith);
    }

    @Override
    public Object findAboutWith(String aboutWithId) {
        return template.find(new Query(Criteria.where("Id").is(aboutWithId)), AboutWith.class);
    }
}

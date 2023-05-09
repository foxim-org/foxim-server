package com.hnzz.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.hnzz.common.SeaweedFSUtil;
import com.hnzz.commons.base.enums.system.SettingEnum;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.dao.SettingDao;
import com.hnzz.entity.AboutWith;
import com.hnzz.entity.FileInfo;
import com.hnzz.entity.User;
import com.hnzz.entity.system.*;
import com.hnzz.service.SettingService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Resource
    private SeaweedFSUtil seaweedFSUtil;

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
    public AboutWith saveAboutWith(String userId,String value) {
        AboutWith aboutWith = template.findOne(new Query(Criteria.where("name").is("关于我们")), AboutWith.class);
        if (aboutWith!=null){
            aboutWith.setValue(value)
                    .setUpdateAt(new Date());
            return template.save(aboutWith);
        }else {
            AboutWith aboutWith1=new AboutWith();
            aboutWith1.setCreatedAt(new Date())
                    .setName("关于我们")
                    .setUpdateAt(new Date())
                    .setValue(value);

            return template.save(aboutWith1);
        }
    }

    @Override
    public Object findAboutWith() {
        return template.find(new Query(Criteria.where("name").is("关于我们")), AboutWith.class);
    }

    @Override
    public ResponseEntity getSet(String name) {
        SettingEnum settingEnum = SettingEnum.valueOf(name);
        Setting setting = settingDao.getSettingByName(name);
        switch (settingEnum) {
            case LOGIN_SETTING:
                if (setting==null){
                    ResponseEntity.ok(new UserLoginSetting());
                }else {
                    UserLoginSetting userLoginSetting = JSONUtil.toBean(setting.getValue(), UserLoginSetting.class);
                    List<UserLoginSettingItem> userLoginType = userLoginSetting.getUserLoginType();
                    List<UserLoginSettingItem> userLoginSettingItems=new ArrayList<>();
                    for (int i = 0; i < userLoginType.size(); i++) {
                        if (userLoginType.get(i).getUsed()){
                            userLoginSettingItems.add(userLoginType.get(i));
                        }
                    }
                    return ResponseEntity.ok(userLoginSettingItems);
                }

            case REGISTER_SETTING:
                if (setting==null){
                    ResponseEntity.ok(new UserRegisterSetting());
                }else {
                    UserRegisterSetting userRegisterSetting = JSONUtil.toBean(setting.getValue(), UserRegisterSetting.class);

                    List<UserRegisterSettingItem> userRegisterType = userRegisterSetting.getUserRegisterType();
                    List<UserRegisterSettingItem> userRegisterSettingItems=new ArrayList<>();
                    for (int i = 0; i < userRegisterType.size(); i++) {
                        if (userRegisterType.get(i).isUsed()){
                            userRegisterSettingItems.add(userRegisterType.get(i));
                        }
                    }
                    return ResponseEntity.ok(userRegisterSettingItems);
                }
            case WITH_USER_SETTING:
                return setting==null ?
                        ResponseEntity.ok("") :
                        ResponseEntity.ok(setting.getValue());
            default:
                throw new AppException("不存在名为"+name+"的配置");
        }
    }

    @Override
    public void saveLogoAvatarUrl(String userId,MultipartFile file) {

        Setting operator = template.findOne(new Query(Criteria.where("operator").is(userId)), Setting.class);

        if (operator==null){
            Setting setting = new Setting();
            ResponseEntity<FileInfo> response = seaweedFSUtil.uploadFile(file);
            FileInfo body = response.getBody();
            if (body == null) {
                throw new AppException("头像上传失败");
            }
            setting.setName("启动页Logo图片");
            setting.setValue(body.getFileUrl());
            setting.setOperator(userId);
            template.save(setting);
        }else {
            ResponseEntity<FileInfo> response = seaweedFSUtil.uploadFile(file);
            FileInfo body = response.getBody();
            if (body == null) {
                throw new AppException("头像上传失败");
            }
            operator.setValue(body.getFileUrl());
            template.save(operator);
        }

    }

    @Override
    public Setting lookLogoAvatarUrl() {
        Setting operator = template.findOne(new Query(Criteria.where("name").is("启动页Logo图片")), Setting.class);
        return operator;
    }
}

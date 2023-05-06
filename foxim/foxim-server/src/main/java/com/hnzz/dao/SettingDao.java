package com.hnzz.dao;

import com.hnzz.entity.system.Setting;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author HB on 2023/5/5
 * TODO 系统设置数据交互抽象
 */
public interface SettingDao {
    MongoTemplate getMongoTemplate();

    Setting getSettingByName(String name);

    Setting addSetting(Setting setting);

    void updateSetting(Setting setting);
}

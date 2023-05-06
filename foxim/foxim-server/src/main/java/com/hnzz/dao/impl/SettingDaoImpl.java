package com.hnzz.dao.impl;

/**
 * @author HB on 2023/5/5
 * TODO
 */

import com.hnzz.dao.SettingDao;
import com.hnzz.entity.system.Setting;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author HB on 2023/5/5
 * TODO 系统设置数据交互实现类
 */
@Repository
public class SettingDaoImpl implements SettingDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public MongoTemplate getMongoTemplate() {
        return this.mongoTemplate;
    }

    @Override
    public Setting getSettingByName(String name) {
        return mongoTemplate.findOne(Query.query(Criteria.where("name").is(name)), Setting.class);
    }


    @Override
    public Setting addSetting(Setting setting) {
        return mongoTemplate.insert(setting);
    }

    @Override
    public void updateSetting(Setting setting) {
        mongoTemplate.upsert(Query.query(Criteria.where("name").is(setting.getName())),
                Update.update("value",setting.getValue()).set("operator",setting.getOperator()).set("updateAt",setting.getUpdateAt()),
                Setting.class);
    }

}


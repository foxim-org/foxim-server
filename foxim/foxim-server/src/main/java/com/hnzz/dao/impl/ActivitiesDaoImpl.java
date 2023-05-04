package com.hnzz.dao.impl;

import com.hnzz.commons.base.enums.activity.ActivitiesStatus;
import com.hnzz.dao.ActivitiesDao;
import com.hnzz.entity.Activities;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HB on 2023/2/1
 * TODO 消息推送dao层实现
 */
@Repository
public class ActivitiesDaoImpl implements ActivitiesDao {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public Activities saveActivities(Activities activities) {
        return  mongoTemplate.save(activities);
    }

    @Override
    public Activities setActivitiesStatus(Activities activities){
        Update update = new Update();
        update.set("activities",activities.getStatus());
        Query query = new Query(Criteria.where("_id").is(activities.getId()));
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
        return mongoTemplate.findAndModify(query, update,options, Activities.class);
    }

    @Override
    public Activities findActivitiesById(String id) {
        return mongoTemplate.findById(id,Activities.class);
    }

    @Override
    public List<Activities> findActivitiesByConditions(Activities activities) {
        return null;
    }
}

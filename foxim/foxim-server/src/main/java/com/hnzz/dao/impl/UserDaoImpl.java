package com.hnzz.dao.impl;

import com.hnzz.commons.base.exception.AppException;
import com.hnzz.dao.UserDao;
import com.hnzz.entity.User;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @PackageName:com.zzkj.dao.impl
 * @ClassName:GroupDaoImpl
 * @Author 周俊
 * @Date 2023/1/4 10:56
 * @Version 1.0
 **/
@Repository
public class UserDaoImpl implements UserDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public User friendAllowviewpro(String id) {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("id",id);
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("avatarUrl",true);
        fieldsObject.put("username",true);
        fieldsObject.put("statusText",true);
        String fields = fieldsObject.toJson();
        System.out.println(fields);
        Query query = new BasicQuery(dbObject.toJson(), fields);
        return mongoTemplate.findOne(query, User.class);
    }


    @Override
    public User friendSimple(String id) {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("id",id);
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("avatarUrl",true);
        fieldsObject.put("username",true);
        fieldsObject.put("foxCode",true);
        String fields = fieldsObject.toJson();
        System.out.println(fields);
        Query query = new BasicQuery(dbObject.toJson(), fields);
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public User selectBySearch(String search) {
        if (search.length()==11){
            return mongoTemplate.findOne(new Query(Criteria.where("mobile").is(search)), User.class);
        }else {
            return mongoTemplate.findOne(new Query(Criteria.where("foxCode").is(Integer.valueOf(search))), User.class);
        }
    }


    @Override
    public User findUserById(String userId) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("Id").is(userId));
        return mongoTemplate.findOne(new Query(criteria),User.class);
    }

    @Override
    public User setUserInfo(User user) {
        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update update = new Update();
        Optional.ofNullable(user.getUsername()).ifPresent(v->update.set("username",v));
        Optional.ofNullable(user.getBio()).ifPresent(v->update.set("bio",v));
        Optional.ofNullable(user.getSex()).ifPresent(v->update.set("sex",v));
        Optional.ofNullable(user.getBirth()).ifPresent(v->update.set("birth",v));
        Optional.ofNullable(user.getRegion()).ifPresent(v->update.set("region",v));
        Optional.ofNullable(user.getLoginIp()).ifPresent(v->update.set("loginIp",v));
        Optional.ofNullable(user.getLastLoginAt()).ifPresent(v->update.set("lastLoginAt",v));
        Optional.ofNullable(user.getStatusText()).ifPresent(v->update.set("statusText",v));
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
        return mongoTemplate.findAndModify(query,update,options, User.class);
    }

    @Override
    public Page<User> findAll(Pageable pageable, String search) {
        Criteria criteria = new Criteria();
        if (search!=null){
            criteria.orOperator(Criteria.where("username").regex(".*"+search+".*"));
        }
        Query query = new Query(criteria).with(pageable);
        query.fields().exclude("password");
        List<User> users = mongoTemplate.find(query, User.class);
        long count = mongoTemplate.count(new Query(criteria), User.class);
        return new PageImpl<>(users, pageable, count);
    }

    @Override
    public User setUserAvatarUrl(String userId, String fileUrl) {
        boolean exists = mongoTemplate.exists(new Query(Criteria.where("id").is(userId)), User.class);
        if (!exists){
            throw new AppException("用户id不存在");
        }
        Update update = new Update();
        update.set("avatarUrl", fileUrl);
        Query query = new Query(Criteria.where("id").is(userId));
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
        return mongoTemplate.findAndModify(query, update, options, User.class);
    }

    @Override
    public boolean userLogout(String id) {
        Query query = new Query(Criteria.where("username").is(id));
        DeleteResult remove = mongoTemplate.remove(query, User.class);
        return remove.wasAcknowledged();
    }

    @Override
    public Page<User> userAll(Pageable pageable) {
        Criteria criteria = new Criteria();
        Query query = new Query(criteria).with(pageable);
        query.fields().exclude("password");
        List<User> users = mongoTemplate.find(query, User.class);
        long count = mongoTemplate.count(new Query(criteria), User.class);
        return new PageImpl<>(users, pageable, count);
    }

    @Override
    public Boolean formUserByMobile(String mobile) {
        return mongoTemplate.count(Query.query(Criteria.where("mobile").is(mobile)), User.class)>0;
    }

}

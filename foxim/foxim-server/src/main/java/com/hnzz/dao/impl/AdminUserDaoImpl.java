package com.hnzz.dao.impl;

import com.hnzz.commons.base.exception.AppException;
import com.hnzz.dao.AdminUserDao;
import com.hnzz.entity.*;
import com.hnzz.form.UserAbleForm;
import com.hnzz.form.UserAutoForm;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @author HB on 2023/3/2
 * TODO 管理员数据层
 */
@Repository
public class AdminUserDaoImpl implements AdminUserDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public AdminUser register(AdminUser adminUser, Platform platform){
        String id = new ObjectId().toString();
        adminUser.setPlatformId(id);
        AdminUser user = mongoTemplate.insert(adminUser);

        platform.setSuperAdminId(user.getId());
        platform.setId(id);
        Platform insert = mongoTemplate.insert(platform);
        return user;
    }

    @Override
    public AdminUser findAdmin(String searchInfo) {

        int mobileLength = 11;

        Criteria criteria = new Criteria();
        Query query = new Query();
        if (searchInfo.length()<mobileLength){
            criteria.andOperator(Criteria.where("userName").is(searchInfo));
        }else if (searchInfo.length()==mobileLength){
            criteria.orOperator(Criteria.where("userName").is(searchInfo),Criteria.where("mobile").is(searchInfo));
        }else {
            criteria.orOperator(Criteria.where("userName").is(searchInfo),Criteria.where("id").is(searchInfo));
        }

        AdminUser user = mongoTemplate.findOne(query, AdminUser.class);

        if (user==null||user.getId()==null){
            throw new AppException("用户信息输入有误");
        }
        return user;
    }

    @Override
    public AdminUser findAdminById(String adminId){
        return mongoTemplate.findById(adminId, AdminUser.class);
    }

    @Override
    public User setUserAutoAdd(UserAutoForm form) {
        User id = mongoTemplate.findOne(new Query(Criteria.where("id").is(form.getUserId())), User.class);
        assert id != null;
        assert id.getAutoAdd()!= null;
        id.setAutoAdd(form.getAutoAdd());
        return mongoTemplate.save(id);
    }

    @Override
    public AdminUser findAdminByMobile(String mobile){
        return mongoTemplate.findOne(new Query(Criteria.where("mobile").is(mobile)), AdminUser.class);
    }

    @Override
    public AdminUser findAdminFromPlatformId(String platformId){
        Query query = new Query(Criteria.where("platformId").is(platformId));
        return mongoTemplate.findOne(query, AdminUser.class);
    }

    @Override
    public Platform findPlatform(String platformId) {
        return mongoTemplate.findById(platformId, Platform.class);
    }

    @Override
    public Page<PrivateMessage> getMessageListToPrivate(Pageable pageable, LocalDate start, LocalDate end, String search) {
        Criteria criteria = new Criteria();
        if (search != null && !search.isEmpty()) {
            criteria.orOperator(
                    Criteria.where("username").regex(".*" + search + ".*")
            );
        }
        if (start != null) {
            criteria.andOperator(Criteria.where("updatedAt").gte(start.atStartOfDay()));
        }
        if (end != null) {
            criteria.andOperator(Criteria.where("updatedAt").lte(end.plusDays(1).atStartOfDay()));
        }
        Query query = new Query(criteria);
        long count = mongoTemplate.count(query, PrivateMessage.class);
        List<PrivateMessage> privateMessages = mongoTemplate.find(query.with(pageable), PrivateMessage.class);
        System.out.println("总数为: " + count);
        return  new PageImpl<>(privateMessages, pageable, count);

    }
    @Override
    public Page<GroupMessage> getMessageListToGroup(Pageable pageable, LocalDate start, LocalDate end, String search){
        Criteria criteria = new Criteria();
        if (search != null && !search.isEmpty()) {
            criteria.andOperator(Criteria.where("username").regex(".*" + search + ".*"));
        }
        if (start != null) {
            criteria.andOperator(Criteria.where("updatedAt").gte(start.atStartOfDay()));
        }
        if (end != null) {
            criteria.andOperator(Criteria.where("updatedAt").lte(end.plusDays(1).atStartOfDay()));
        }
        Query query = new Query(criteria);
        long count = mongoTemplate.count(query, GroupMessage.class);
        List<GroupMessage> groupMessages = mongoTemplate.find(query.with(pageable), GroupMessage.class);
        return new PageImpl<>(groupMessages, pageable, count);
    }



    @Override
    public boolean setUserPwd(String account, String password) {
        Update update = new Update().set("password", password);
        UpdateResult result = mongoTemplate.updateFirst(new Query(Criteria.where("id").is(account)), update, User.class);
        if (result.getMatchedCount()!=result.getModifiedCount() && result.getModifiedCount()!=1){
            return false;
        }
        return true;
    }

    @Override
    public AdminUser addAdmin(AdminUser adminUser) {
        return mongoTemplate.insert(adminUser);
    }

    @Override
    public User setUserAble(UserAbleForm form) {
        User id = mongoTemplate.findOne(new Query(Criteria.where("id").is(form.getUserId())), User.class);
        assert id != null;
        id.setIsDisabled(form.getIsDisabled());
        return mongoTemplate.save(id);
    }

}

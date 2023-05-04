package com.hnzz.dao.impl;

import com.hnzz.dao.GroupUserDao;
import com.hnzz.entity.Contacts;
import com.hnzz.entity.GroupUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.zzkj.dao.impl
 * @ClassName:GroupUserDaoImpl
 * @Author 冼大丰
 * @Date 2023/1/5 14:04
 * @Version 1.0
 **/
@Repository
public class GroupUserDaoImpl implements GroupUserDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveGroupUser(ArrayList<GroupUsers> groupUsersArrayList) {
        mongoTemplate.insertAll(groupUsersArrayList);
    }

    @Override
    public void saveGroupUser(GroupUsers groupUsers) {
        mongoTemplate.save(groupUsers);
    }

    @Override
    public void deleteGroupUserByGroupId(String gid) {
        Query query = new Query(Criteria.where("groupId").is(gid));
        mongoTemplate.remove(query,GroupUsers.class);
    }

    @Override
    public Long getGroupUserSize(String gid) {
        Query query = new Query(Criteria.where("groupId").is(gid));
        return mongoTemplate.count(query,GroupUsers.class);
    }

    @Override
    public void deteleGroupUserByUserId(String userId, String groupId) {
        Criteria criteria=new Criteria();
        criteria.andOperator(Criteria.where("userId").is(userId),Criteria.where("groupId").is(groupId));
        Query query = new Query(criteria);
        mongoTemplate.remove(query,GroupUsers.class);
    }


    @Override
    public GroupUsers updateGroupUserByUsers(GroupUsers groupUsers) {
        return mongoTemplate.save(groupUsers);
    }

    @Override
    public GroupUsers getGroupUserByUserId(String ownerId,String groupId) {
        Criteria criteria=new Criteria();
        criteria.andOperator(Criteria.where("userId").is(ownerId),Criteria.where("groupId").is(groupId));
        return mongoTemplate.findOne(new Query(criteria).with(Sort.by(Sort.Direction.ASC,"createdAt")),GroupUsers.class);
    }

    @Override
    public List<GroupUsers> getGroupUserByUserId(List<String> ownerIds, String groupId) {
        Criteria criteria=new Criteria();
        criteria.andOperator(Criteria.where("userId").in(ownerIds),Criteria.where("groupId").is(groupId));
        return mongoTemplate.find(new Query(criteria),GroupUsers.class);
    }

    @Override
    public List<GroupUsers> getAllGroupUsers(String gid) {
        return mongoTemplate.find(Query.query(Criteria.where("groupId").is(gid)), GroupUsers.class);
    }

    @Override
    public void kickGroup(List<String> uids,String gid) {
        Criteria criteria=new Criteria();
        criteria.andOperator(Criteria.where("userId").in(uids),Criteria.where("groupId").is(gid));
        Query query=new Query(criteria);
        mongoTemplate.findAllAndRemove(query, GroupUsers.class);
    }

    @Override
    public void setForwardTime(List<String> groupIds, String userId) {
        Query query = new Query(Criteria.where("userId").is(userId).and("contactId").in(groupIds));
        Update update = new Update();
        update.set("lastForwardTime", new Date());
        mongoTemplate.updateMulti(query, update, GroupUsers.class);
    }

    @Override
    public Boolean isGroupUser(String groupId, String userId) {
        return mongoTemplate.exists(Query.query(Criteria.where("groupId").is(groupId).and("userId").is(userId)),GroupUsers.class);
    }
}

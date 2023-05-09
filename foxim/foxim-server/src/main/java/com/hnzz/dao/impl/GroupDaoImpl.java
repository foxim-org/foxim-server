package com.hnzz.dao.impl;

import com.hnzz.commons.base.exception.AppException;
import com.hnzz.dao.GroupDao;
import com.hnzz.entity.Group;
import com.hnzz.entity.GroupUsers;
import com.hnzz.entity.User;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @PackageName:com.zzkj.dao.impl
 * @ClassName:GroupDaoImpl
 * @Author 冼大丰
 * @Date 2023/1/4 10:56
 * @Version 1.0
 **/
@Repository
public class GroupDaoImpl implements GroupDao {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public Group addGroup(Group group) {
       return  mongoTemplate.save(group);
    }


    @Override
    public Group getGroupById(String gid) {
        return mongoTemplate.findById(gid,Group.class);
    }

    @Override
    public void deleteGroupById(String gid) {
        Query query = new Query(Criteria.where("_id").is(gid));
        mongoTemplate.remove(query,Group.class);
    }

    @Override
    public Group updateGroupDataById(Group groupById) {
        return mongoTemplate.save(groupById);
    }

    @Override
    public List<GroupUsers> getGroupUsersByUserId(String userId) {
        return mongoTemplate.find(new Query(Criteria.where("userId").is(userId)).with(Sort.by(Sort.Direction.DESC,"createdAt")), GroupUsers.class);
    }

    @Override
    public List<Group> getGroupByIds(List<String> groupIds){
        return mongoTemplate.find(new Query(Criteria.where("_id").in(groupIds)),Group.class);
    }

    @Override
    public List<Group> getGroupUserList(List<String> userList) {
        return mongoTemplate.find(new Query(Criteria.where("ownerId").in(userList)),Group.class);
    }

    @Override
    public List<Group> pageGroup(Pageable pageable, String search) {
        Query query = new Query().with(pageable);
        if (search!=null){
            query.addCriteria(Criteria.where("name").regex(".*"+search+".*"));
        }
        List<Group> groupList = mongoTemplate.find(query, Group.class);
        return groupList;
    }

    @Override
    public Group AdminUpdateGroup(Group newGroup) {
        return mongoTemplate.save(newGroup);
    }

    @Override
    public long getCount(String search) {
        Query query = new Query();
        if (search!=null){
            query.addCriteria(Criteria.where("name").regex(".*"+search+".*"));
        }
        return  mongoTemplate.count(query, Group.class);
    }

    @Override
    public Group setGroupAvatarUrl(String groupId, String fileUrl) {
        boolean exists = mongoTemplate.exists(new Query(Criteria.where("id").is(groupId)),Group.class);
        if (!exists){
            throw new AppException("群聊id不存在");
        }
        Update update = new Update();
        update.set("groupHead", fileUrl);
        Query query = new Query(Criteria.where("id").is(groupId));
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
        return mongoTemplate.findAndModify(query, update, options, Group.class);
    }

    @Override
    public List<Group> groupList(String search) {
        Query query = new Query();
        if (search!=null){
            query.addCriteria(Criteria.where("name").regex(".*"+search+".*"));
        }
        return  mongoTemplate.find(query, Group.class);
    }

    @Override
    public void save(Group groupById) {
        mongoTemplate.save(groupById);
    }

    @Override
    public List<Group> findGroupBySearch(String search) {
        if (search.length()==5){
            Integer integer = Integer.valueOf(search);
            return mongoTemplate.find(new Query(Criteria.where("foxCode").is(integer)), Group.class);
        }else {
            return mongoTemplate.find(new Query(Criteria.where("name").regex(".*" + search + ".*")),Group.class);
        }
    }

}

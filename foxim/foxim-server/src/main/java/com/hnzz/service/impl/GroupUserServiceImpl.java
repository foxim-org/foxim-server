package com.hnzz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hnzz.common.ResultUtil;
import com.hnzz.commons.base.enums.activity.ActivitiesField;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.commons.base.result.Result;
import com.hnzz.dao.BotsDao;
import com.hnzz.dao.GroupDao;
import com.hnzz.dao.GroupUserDao;
import com.hnzz.dto.GroupMembers;
import com.hnzz.dto.UserDTO;
import com.hnzz.entity.Activities;
import com.hnzz.entity.Contacts;
import com.hnzz.entity.Group;
import com.hnzz.entity.GroupUsers;
import com.hnzz.entity.bot.Bots;
import com.hnzz.form.groupform.NewGroup;
import com.hnzz.service.ActivitiesService;
import com.hnzz.service.ContactsService;
import com.hnzz.service.GroupUserService;
import com.hnzz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @PackageName:com.zzkj.service.impl
 * @ClassName:GroupUserServiceImpl
 * @Author 冼大丰
 * @Date 2023/1/5 14:09
 * @Version 1.0
 **/
@Service("groupUserService")
@Slf4j
public class GroupUserServiceImpl implements GroupUserService {

    @Autowired
    private GroupUserDao groupUserDao;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Resource
    private ContactsService contactsService;

    @Autowired
    private GroupDao groupDao;

    @Resource
    private BotsDao botsDao;

    @Resource
    private ActivitiesService activitiesService;


    @Override

    public void saveGroupUser(NewGroup newGroup,List<String> usersId, Group group) {

        List<UserDTO> userById = userService.getUsersById(usersId);
        log.info("用户信息 - {}",userById);
        ArrayList<GroupUsers> groupUsersArrayList =new ArrayList<>();
        for (int i = 0; i < userById.size(); i++) {
            GroupUsers groupUsers =new GroupUsers();
            if (userById.get(i).getId().equals(group.getOwnerId())){
                groupUsers.setGroupId(group.getId())
                        .setAvatarUrl(userById.get(i).getAvatarUrl())
                        .setSilencedTo(new Date())
                        .setIsMuted(false)
                        .setIsSticky(false)
                        .setCreatedAt(group.getCreatedAt())
                        .setMessageTTL(newGroup.getMessageTTL())
                        .setUserId(userById.get(i).getId())
                        .setRecentAt(new Date())
                        .setIsAdmin(true);
            }else {
                groupUsers.setGroupId(group.getId())
                        .setAvatarUrl(userById.get(i).getAvatarUrl())
                        .setSilencedTo(new Date())
                        .setCreatedAt(group.getCreatedAt())
                        .setMessageTTL(newGroup.getMessageTTL())
                        .setUserId(userById.get(i).getId())
                        .setRecentAt(new Date())
                        .setIsMuted(false)
                        .setIsSticky(false)
                        .setIsAdmin(false);
            }
            groupUsersArrayList.add(groupUsers);
        }
        groupUserDao.saveGroupUser(groupUsersArrayList);
    }

    @Override
    public GroupUsers saveGroupUser(GroupUsers groupUsers) {
        return mongoTemplate.save(groupUsers);
    }

    @Override
    public void removeGroupUserByGroupId(String gid) {
        groupUserDao.deleteGroupUserByGroupId(gid);
    }

    @Override
    public Long getGroupUserSize(String gid) {
        return groupUserDao.getGroupUserSize(gid);
    }

    @Override
    public void joinGroupByGid(String gid, List<String> uids) {
        /*//获取已知群成员的数量
        Long groupUserSize = this.getGroupUserSize(gid);

        int size =uids.size();*/
        Group group = mongoTemplate.findById(gid,Group.class);

        if (group==null){
            throw new AppException("未找到该群聊信息");
        }
        // 群聊人数限制
        /*if ((groupUserSize+size) > 2000){
            group.setId(null);
            group.setCreatedAt(new Date());
            uids.add(group.getOwnerId());
        }
        group = mongoTemplate.insert(group);*/

        List<UserDTO> userById = userService.getUsersById(uids);

        ArrayList<GroupUsers> groupUsersArrayList =new ArrayList<>();
        for (int i = 0; i < userById.size(); i++) {
            GroupUsers groupUsers =new GroupUsers()
                    .setAvatarUrl(userById.get(i).getAvatarUrl())
                    .setGroupId(group.getId())
                    .setIsSticky(false)
                    .setIsMuted(false)
                    .setSilencedTo(new Date())
                    .setUsername(userById.get(i).getUsername())
                    .setCreatedAt(new Date())
                    .setUserId(uids.get(i))
                    .setIsAdmin(false)
                    .setRecentAt(new Date());
            groupUsersArrayList.add(groupUsers);
        }
        groupUserDao.saveGroupUser(groupUsersArrayList);

        // 消息推送入群欢迎
        Bots bot = botsDao.getBotById(gid);
        Optional.ofNullable(bot.getJoinGroup()).ifPresent(v->{
            if (v.getIsWelcome()!=null && v.getIsWelcome()){
                String text = v.getWelcomeText();
                if (text !=null&& !text.trim().isEmpty()){
                    for (GroupUsers u:groupUsersArrayList){
                        StringBuilder sb = new StringBuilder();
                        String s = sb.append(text).append(u.getUsername()).toString();
                        HashMap<String, String> map = new HashMap<>();
                        map.put(ActivitiesField.GROUP_ID,gid);
                        map.put(ActivitiesField.TEXT,s);
                        map.put(ActivitiesField.ACTIVITY_CREATEDAT,new Date().toString());
                        String topic = Activities.topic("group", gid, "schedule");
                        Activities activities = new Activities(topic,map);
                        try {
                            activities = activitiesService.saveActivities(activities);
                        } catch (IOException e) {
                            log.warn("编号为{}的消息发送失败",activities.getId());
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void removeGroupUserByUserId(String userId, String groupId) {
        groupUserDao.deteleGroupUserByUserId(userId,groupId);
    }


    @Override
    public List<GroupUsers> SetAdmin(List<GroupUsers> users) {
        ArrayList<GroupUsers> setGroupUsersList =new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {

            users.get(i).setIsAdmin(true);

            GroupUsers groupUsers= groupUserDao.updateGroupUserByUsers(users.get(i));

            setGroupUsersList.add(groupUsers);

        }

        return setGroupUsersList;
    }

    @Override
    public void SetAdmin(GroupUsers users) {
        GroupUsers groupUsers = users.setIsAdmin(true);
        groupUserDao.saveGroupUser(groupUsers);

    }

    @Override
    public Result<List<GroupUsers>> unSetAdmin(List<GroupUsers> users) {
        ArrayList<GroupUsers> unSetGroupUsersList =new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {

            if (!users.get(i).getIsAdmin()){
                return Result.customize(400,"该用户不是管理员，请重试",null);
            }else {
                users.get(i).setIsAdmin(false);

                GroupUsers groupUsers= groupUserDao.updateGroupUserByUsers(users.get(i));

                unSetGroupUsersList.add(groupUsers);
            }


        }

        return Result.success(unSetGroupUsersList);
    }

    @Override
    public void unSetAdmin(GroupUsers users) {
        GroupUsers groupUsers = users.setIsAdmin(false);
        groupUserDao.saveGroupUser(groupUsers);
    }

    @Override
    public GroupUsers getGroupUserByUserId(String ownerId,String groupId) {
        return groupUserDao.getGroupUserByUserId(ownerId,groupId);
    }

    @Override
    public List<GroupUsers> getGroupUserByUserId(List<String> ownerIds, String groupId) {
        return groupUserDao.getGroupUserByUserId(ownerIds,groupId);
    }


    @Override
    public List<GroupMembers> findAllGroupMembers(String groupId , String userId) {
        List<GroupMembers> groupMembers=new ArrayList<>();
        Group group = groupDao.getGroupById(groupId);
        if (group == null){
            throw new AppException("该群不存在!");
        }
        List<GroupUsers> groupUsersList = groupUserDao.getAllGroupUsers(groupId);
        if (groupUsersList!=null && !groupUsersList.isEmpty()) {
            List<String> usersId = groupUsersList.stream().map(GroupUsers::getUserId).filter(id ->!id.equals(userId)).collect(Collectors.toList());
            List<Contacts> groupUserContacts = contactsService.getGroupUserContacts(usersId, userId);
            for (GroupUsers groupUser : groupUsersList) {
                GroupMembers groupMember = new GroupMembers();
                groupMember.setId(groupUser.getUserId());
                groupMember.setAvatarUrl(groupUser.getAvatarUrl());
                groupMember.setIsAdmin(groupUser.getIsAdmin());
                groupMember.setUsername(groupUser.getUsername());
                groupMember.setIsBoss(groupUser.getUserId().equals(group.getOwnerId()));

                if (groupUserContacts!=null && !groupUserContacts.isEmpty()) {
                    for (Contacts contacts : groupUserContacts){
                        if (contacts.getContactId().equals(groupUser.getUserId())) {
                            Optional.ofNullable(contacts.getRemark()).ifPresent(groupMember::setUsername);
                            groupUserContacts.remove(contacts);
                            break;
                        }
                    }
                }
                groupMembers.add(groupMember);
            }


        }
        return groupMembers;
    }

    @Override
    public Result kickGroup(List<String> uids,String gid) {

        for (int i = 0; i < uids.size(); i++) {
            GroupUsers groupUserByUserId = groupUserDao.getGroupUserByUserId(uids.get(i),gid);
            if (groupUserByUserId==null){
                return Result.customize(500,"该用户不存在",null);
            }
        }
        groupUserDao.kickGroup(uids,gid);

        return Result.success("踢出成功");
    }

    @Override
    public List<GroupUsers> getGroupUserByGroupId(String groupId) {
        Query query=new Query(Criteria.where("groupId").is(groupId));
        return mongoTemplate.find(query,GroupUsers.class);
    }

    @Override
    public void updateGroupUser(GroupUsers groupUserByUserId, String displayName) {
        groupUserByUserId.setUsername(displayName==null?groupUserByUserId.getUsername():displayName);
        mongoTemplate.save(groupUserByUserId);
    }

    @Override
    public Boolean isGroupUser(String groupId, String userId) {
        return groupUserDao.isGroupUser(groupId, userId);
    }

    @Override
    public List<GroupUsers> getGroupUserByGroupIds(List<String> groupIds, String userId) {
        Criteria criteria=new Criteria();
        criteria.andOperator(Criteria.where("groupId").in(groupIds),Criteria.where("userId").is(userId));
        return mongoTemplate.find(new Query(criteria),GroupUsers.class);
    }


}

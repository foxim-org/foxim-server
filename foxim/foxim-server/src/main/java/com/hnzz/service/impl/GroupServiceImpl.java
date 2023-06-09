package com.hnzz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hnzz.common.SeaweedFSUtil;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.commons.base.jwt.JWTHelper;
import com.hnzz.dao.GroupDao;
import com.hnzz.dto.GroupAdmin;
import com.hnzz.dto.GroupData;
import com.hnzz.dto.GroupSearchInfo;
import com.hnzz.dto.UserDTO;
import com.hnzz.entity.*;
import com.hnzz.form.GroupOutForm;
import com.hnzz.form.IdsPattern;
import com.hnzz.form.groupform.NewGroup;
import com.hnzz.form.groupform.UpdateGroup;
import com.hnzz.service.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @PackageName:com.zzkj.service.impl
 * @ClassName:GroupServiceimpl
 * @Author 冼大丰
 * @Date 2023/1/4 11:00
 * @Version 1.0
 **/
@Service("groupService")
public class GroupServiceImpl implements GroupService {

    @Resource
    private GroupDao groupDao;
    @Resource
    private IdsService idsService;
    @Resource
    private GroupService groupService;
    @Resource
    private UserService userService;
    @Resource
    private GroupUserService groupUserService;

    @Resource
    private BotsService botsService;

    @Resource
    private JWTHelper jwtHelper;

    @Resource
    private SeaweedFSUtil seaweedFSUtil;

    @Override
    @Transactional
    public Group saveGroup(NewGroup newGroup,String userId) {
        Group group= BeanUtil.copyProperties(newGroup,Group.class);
        group.setOwnerId(userId)
             .setIsSilencedToAll(false);
        List<String> usersId = new ArrayList<>();

        usersId.add(userId);
        group.setGroupHead(null);
        Integer foxCode = idsService.getIdWithRandom(new IdsPattern());
        group.setFoxCode(foxCode);
        Group groups = groupDao.addGroup(group);

        if (newGroup.getUsersId()==null){
            groupUserService.saveGroupUser(newGroup,usersId,group);
            return groups;
        }
        usersId.addAll(newGroup.getUsersId());
        // 新建群成员
        groupUserService.saveGroupUser(newGroup,usersId,group);
        // 新增群机器人
        botsService.createBots(group);

        return groups;
    }

    @Override
    public Group getGroupById(String gid) {
        return groupDao.getGroupById(gid);
    }

    @Override
    public void removeGroupById(String gid) {
        groupDao.deleteGroupById(gid);
    }

    @Override
    public Group updateGroupData(UpdateGroup updateGroup,Group groupById) {
        groupById.setName(updateGroup.getName()==null?groupById.getName():updateGroup.getName())
                .setGroupHead(updateGroup.getGroupHead()==null?groupById.getGroupHead():updateGroup.getGroupHead())
                .setDesc(updateGroup.getDesc()==null?groupById.getDesc():updateGroup.getDesc())
                .setTags(updateGroup.getTags()==null?groupById.getTags():updateGroup.getTags())
                .setNotice(updateGroup.getNotice()==null?groupById.getNotice():updateGroup.getNotice());

        return groupDao.updateGroupDataById(groupById);
    }

    @Override
    public List<GroupData> getGroupByUserId(String userId) {
        List<GroupUsers> groupIdByUserId = groupDao.getGroupUsersByUserId(userId);
        if (groupIdByUserId!=null && !groupIdByUserId.isEmpty()) {
            List<String> groupId = groupIdByUserId.stream().map(GroupUsers::getGroupId).collect(Collectors.toList());
            List<Group> groups = groupDao.getGroupByIds(groupId);
            return BeanUtil.copyToList(groups,GroupData.class);
        }
        return null;
    }

    @Override
    public Group transferGroup(Group groupById, String userId) {
        Group group = groupById.setOwnerId(userId);
        return groupDao.updateGroupDataById(group);
    }

    @Override
    public List<Group> getGroupByUserList(List<String> userList) {
        return groupDao.getGroupUserList(userList);
    }

    @Override
    public Page<GroupAdmin> pageGroup(Integer pageNum, Integer pageSize, String search) {

        Pageable pageable= PageRequest.of(pageNum,pageSize);

        List<Group> groupList = groupDao.pageGroup(pageable, search);

        long count = groupDao.getCount(search);

        List<GroupAdmin> adminGroups=new ArrayList<>();

        for (Group group:groupList) {
            GroupUsers groupUserByUserId = groupUserService.getGroupUserByUserId(group.getOwnerId(), group.getId());
            if (groupUserByUserId!=null){
                GroupAdmin adminGroup= BeanUtil.copyProperties(group,GroupAdmin.class);
                adminGroup.setOwnerName(groupUserByUserId.getUsername());
                adminGroups.add(adminGroup);
            }
        }
        return new PageImpl<>(adminGroups,pageable,count);
    }

    @Override
    public Group AdminUpdateGroup(Group newGroup) {
        return groupDao.AdminUpdateGroup(newGroup);
    }

    @Override
    public String saveGroupInvite(String userId, String groupId) {
        Map<String,Object> map=new HashMap<>();

        ObjectId objectId = new ObjectId();

        String inviteId = objectId.toString();

        map.put("userId",userId);
        map.put("groupId",groupId);
        map.put("inviteId",inviteId);

        return jwtHelper.createJWT(map);
    }

    @Override
    public List<Group> getGroupByIds(List<String> groupIds) {
        return groupDao.getGroupByIds(groupIds);
    }

    @Override
    public Group setGroupAvatarUrl(String groupId, MultipartFile file) {
        ResponseEntity<FileInfo> response = seaweedFSUtil.uploadFile(file);
        FileInfo body = response.getBody();
        if (body==null){
            throw new AppException("头像上传失败");
        }
        return groupDao.setGroupAvatarUrl(groupId, body.getFileUrl());
    }

    @Override
    public List<GroupData> groupList(String search) {
       List<Group> groupList = groupDao.groupList(search);

        return BeanUtil.copyToList(groupList, GroupData.class);
    }

    @Override
    public void save(Group groupById) {
        groupDao.save(groupById);
    }

    @Override
    public List<GroupSearchInfo> search(String search, String userId) {
        List<Group> groupBySearch = groupDao.findGroupBySearch(search);
        System.out.println(groupBySearch);
        List<GroupSearchInfo> groupSearchInfos = new ArrayList<>();
        if (groupBySearch!=null && !groupBySearch.isEmpty()) {
            groupSearchInfos = BeanUtil.copyToList(groupBySearch, GroupSearchInfo.class);
            for (GroupSearchInfo info : groupSearchInfos) {
                info.setIsGroupUser(groupUserService.isGroupUser(info.getId(),userId));
                if (!info.getIsGroupUser()){
                    GroupApplicationForm groupApplicationForm = groupService.findGroupApplicationForm(info.getId(), userId);
                    if (groupApplicationForm!=null&&groupApplicationForm.getStatus().equals("PENDING")){
                        info.setIsJoin("PENDING");
                    }else if (groupApplicationForm==null){
                        info.setIsJoin("INEXISTENCE");
                    }
                }else {
                    info.setIsJoin("ACCEPTED");
                }
            }
        }

        return groupSearchInfos;
    }

    @Override
    public void saveGroupApplicationFrom(Group groupById, String userId) {
        GroupApplicationForm groupApplicationForm=new GroupApplicationForm();
        groupApplicationForm.setGroupId(groupById.getId())
                .setStatus("PENDING")
                .setUserId(userId)
                .setFoxCode(groupById.getFoxCode())
                .setCreatedAt(new Date())
                .setUpdateAt(new Date());
        groupDao.saveGroupApplicationFrom(groupApplicationForm);

    }

    @Override
    public List<GroupOutForm> getGroupApplicationFrom(List<String> groups) {
        List<GroupApplicationForm> groupApplicationFrom = groupDao.getGroupApplicationFrom(groups);
        List<GroupOutForm> groupOutForms=new ArrayList<>();
        if (!groupApplicationFrom.isEmpty()){
            for (GroupApplicationForm groupApplicationForm : groupApplicationFrom) {
                if (groupApplicationForm.getStatus().equals("PENDING")) {
                    GroupOutForm groupOutForm = BeanUtil.copyProperties(groupApplicationForm, GroupOutForm.class);
                    UserDTO userById = userService.findUserById(groupApplicationForm.getUserId());
                    groupOutForm.setUsername(userById.getUsername());
                    if (userById.getAvatarUrl()==null){
                        groupOutForm.setAvatarUrl(userById.getMoRenUrl());
                    }else {
                        groupOutForm.setAvatarUrl(userById.getAvatarUrl());
                    }
                        groupOutForms.add(groupOutForm);
                }
            }
            return groupOutForms;
        }
        return groupOutForms;
    }

    @Override
    public GroupApplicationForm findGroupApplicationForm(String groupId, String joinUserId) {
        return groupDao.findGroupApplicationForm(groupId,joinUserId);
    }

    @Override
    public void saveGroupApplication(GroupApplicationForm groupApplicationForm) {
        groupDao.saveGroupApplication(groupApplicationForm);
    }
}

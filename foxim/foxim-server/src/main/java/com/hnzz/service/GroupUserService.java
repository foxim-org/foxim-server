package com.hnzz.service;


import com.hnzz.commons.base.result.Result;
import com.hnzz.dto.GroupMembers;
import com.hnzz.entity.Group;
import com.hnzz.entity.GroupUsers;
import com.hnzz.form.groupform.NewGroup;

import java.util.List;

/**
 * @PackageName:com.zzkj.service
 * @InterfaceName:GroupUserService
 * @Author 冼大丰
 * @Date 2023/1/5 14:09
 * @Version 1.0
 **/
public interface GroupUserService {

    void saveGroupUser(NewGroup newGroup,List<String> usersId, Group group);

    GroupUsers saveGroupUser(GroupUsers groupUsers);

    void removeGroupUserByGroupId(String gid);

    Long getGroupUserSize(String gid);

    void joinGroupByGid(String gid, List<String> uids);

    void removeGroupUserByUserId(String userId, String groupId);

    GroupUsers getGroupUserByUserId(String ownerId,String groupId);

    List<GroupUsers> getGroupUserByUserId(List<String> ownerIds,String groupId);

    List<GroupUsers> SetAdmin(List<GroupUsers> users);

    void SetAdmin(GroupUsers users);

    Result<List<GroupUsers>> unSetAdmin(List<GroupUsers> users);

    void unSetAdmin(GroupUsers users);

    List<GroupMembers> findAllGroupMembers(String groupId , String userId);

    Result kickGroup(List<String> uids,String gid);

    List<GroupUsers> getGroupUserByGroupId(String groupId);

    void updateGroupUser(GroupUsers groupUserByUserId, String displayName);

    Boolean isGroupUser(String groupId, String userId);
}

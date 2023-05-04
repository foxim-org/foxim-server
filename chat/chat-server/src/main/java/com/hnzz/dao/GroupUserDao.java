package com.hnzz.dao;


import com.hnzz.entity.GroupUsers;

import java.util.ArrayList;
import java.util.List;

/**
 * @PackageName:com.zzkj.dao
 * @InterfaceName:GroupUserDao
 * @Author 冼大丰
 * @Date 2023/1/5 14:04
 * @Version 1.0
 **/
public interface GroupUserDao {


    void saveGroupUser(ArrayList<GroupUsers> groupUsersArrayList);

    void saveGroupUser(GroupUsers groupUsers);

    void deleteGroupUserByGroupId(String gid);

    Long getGroupUserSize(String gid);

    void deteleGroupUserByUserId(String userId, String groupId);

    GroupUsers updateGroupUserByUsers(GroupUsers groupUsers);

    GroupUsers getGroupUserByUserId(String ownerId,String groupId);

    List<GroupUsers> getGroupUserByUserId(List<String> ownerIds,String groupId);

    List<GroupUsers> getAllGroupUsers(String gid);

    void kickGroup(List<String> uids,String gid);

    void setForwardTime(List<String> groupIds, String userId);

    Boolean isGroupUser(String groupId, String userId);
}

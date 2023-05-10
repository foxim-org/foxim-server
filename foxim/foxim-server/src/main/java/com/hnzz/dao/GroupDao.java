package com.hnzz.dao;


import com.hnzz.entity.Group;
import com.hnzz.entity.GroupApplicationForm;
import com.hnzz.entity.GroupUsers;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @PackageName:com.zzkj.dao
 * @ClassName:GroupDao
 * @Author 冼大丰
 * @Date 2023/1/4 10:55
 * @Version 1.0
 **/

public interface GroupDao  {
    Group addGroup(Group group);

    Group getGroupById(String gid);

    void deleteGroupById(String gid);

    Group updateGroupDataById(Group groupById);

    List<GroupUsers> getGroupUsersByUserId(String userId);

    List<Group> getGroupByIds(List<String> groupIds);

    List<Group> getGroupUserList(List<String> userList);

    List<Group> pageGroup(Pageable pageable, String search);

    Group AdminUpdateGroup(Group newGroup);

    long getCount(String search);

    Group setGroupAvatarUrl(String groupId, String fileUrl);

    List<Group> groupList(String search);

    void save(Group groupById);

    List<Group> findGroupBySearch(String search);

    void saveGroupApplicationFrom(GroupApplicationForm groupApplicationForm);

    List<GroupApplicationForm> getGroupApplicationFrom(List<String> groups);

    GroupApplicationForm findGroupApplicationForm(String groupId, String joinUserId);

    void saveGroupApplication(GroupApplicationForm groupApplicationForm);
}

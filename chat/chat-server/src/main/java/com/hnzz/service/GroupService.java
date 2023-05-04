package com.hnzz.service;


import com.hnzz.dto.GroupAdmin;
import com.hnzz.dto.GroupData;
import com.hnzz.dto.GroupSearchInfo;
import com.hnzz.entity.Group;
import com.hnzz.form.groupform.NewGroup;
import com.hnzz.form.groupform.UpdateGroup;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @PackageName:com.zzkj.service
 * @InterfaceName:GroupService
 * @Author 冼大丰
 * @Date 2023/1/4 10:59
 * @Version 1.0
 **/
public interface GroupService {
    Group saveGroup(NewGroup newGroup,String userId);

    Group getGroupById(String gid);

    void removeGroupById(String gid);

    Group updateGroupData(UpdateGroup updateGroup,Group group);

    List<GroupData> getGroupByUserId(String userId);
    
    Group transferGroup(Group groupById, String userId);

    List<Group> getGroupByUserList(List<String> userList);

    Page<GroupAdmin> pageGroup(Integer pageNum, Integer pageSize, String search);

    Group AdminUpdateGroup(Group newGroup);

    String saveGroupInvite(String userId, String groupId);

    List<Group> getGroupByIds(List<String> groupIds);

    Group setGroupAvatarUrl(String groupId, MultipartFile file);

    List<GroupData> groupList(String search);

    void save(Group groupById);

    List<GroupSearchInfo> search(String search, String userId);
}

package com.hnzz.dao;

import com.hnzz.entity.GroupMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @PackageName:com.hnzz.dao
 * @InterfaceName:GroupMessageDao
 * @Author 冼大丰
 * @Date 2023/1/31 16:45
 * @Version 1.0
 **/
public interface GroupMessageDao {

    List<GroupMessage> getAllGroupMessageWithASC(String groupId);

    /**
     * TODO 根据groupId 获取所有群消息并倒序排序
     *
     * @param groupId
     * @return
     */
    Page<GroupMessage> getAllGroupMessageWithASC(String userId, String groupId, Pageable pageable);

    /**
     * TODO 根据群id集合获取每个群的最后发言内容
     * @param groupIds
     * @return
     */
    List<GroupMessage> getLastCroupMessagesByCreateAt(List<String> groupIds);
}

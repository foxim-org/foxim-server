package com.hnzz.dao;

import com.hnzz.entity.PrivateMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author HB on 2023/1/30
 * TODO 私聊信息dao层
 */

public interface PrivateMessageDao {

    List<PrivateMessage> getAllPrivateMessageWithASC(String conversationId);

    List<PrivateMessage> getAllPrivateMessageWithASC(String userId, String contactId, Date delTime);
    List<PrivateMessage> getAllPrivateMessageWithASC(String userId, String contactId);

    Page<PrivateMessage> getAllPrivateMessageWithASC(String userId, String contactId, Date delTime , Pageable pageable);

    /**
     * 查询与我有关的联系人最后的消息内容
     * @param userId 我的用户id
     * @param contactIds 好友的id集合
     * @return 我与各好友最后私聊消息的集合
     */
    List<PrivateMessage> getLastPrivateMessage(String userId,List<String> contactIds);

    void emptyPrivateMessages(String userId, String contactId);
}

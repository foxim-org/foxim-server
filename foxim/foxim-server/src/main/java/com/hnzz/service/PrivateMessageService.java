package com.hnzz.service;

import com.hnzz.dto.PrivateMessageDTO;
import com.hnzz.entity.PrivateMessage;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * @author HB on 2023/1/30
 * TODO 私聊信息业务层
 */
public interface PrivateMessageService {
    List<PrivateMessageDTO> getAllPrivateMessageWithASC(String userId, String contactId);

    Page<PrivateMessage> getAllPrivateMessageWithASC(String userId, String contactId , Integer pageNum, Integer pageSize);

    void emptyPrivateMessages(String userId, String contactId);
}

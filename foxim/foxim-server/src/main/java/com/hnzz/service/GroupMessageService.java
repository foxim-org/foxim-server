package com.hnzz.service;

import com.hnzz.dto.GroupMessageDTO;
import com.hnzz.entity.GroupMessage;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @PackageName:com.hnzz.service
 * @InterfaceName:GroupMessageService
 * @Author 冼大丰
 * @Date 2023/1/31 16:42
 * @Version 1.0
 **/
public interface GroupMessageService {
    List<GroupMessageDTO> getAllGroupMessageWithASC(String groupId);

    Page<GroupMessage> getAllGroupMessageWithASC(String userId, String groupId , Integer pageNum, Integer pageSize);
}

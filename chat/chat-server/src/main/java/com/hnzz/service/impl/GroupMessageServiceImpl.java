package com.hnzz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hnzz.dao.GroupMessageDao;
import com.hnzz.dto.GroupMessageDTO;
import com.hnzz.dto.UserDTO;
import com.hnzz.entity.GroupMessage;
import com.hnzz.service.GroupMessageService;
import com.hnzz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @PackageName:com.hnzz.service.impl
 * @ClassName:GroupMessageServiceImpl
 * @Author 冼大丰
 * @Date 2023/1/31 16:42
 * @Version 1.0
 **/
@Service("groupMessageService")
public class GroupMessageServiceImpl implements GroupMessageService {

    @Autowired
    private GroupMessageDao groupMessageDao;

    @Resource
    private UserService userService;

    @Override
    public List<GroupMessageDTO> getAllGroupMessageWithASC(String groupId) {
        List<GroupMessage> groupMessageList = groupMessageDao.getAllGroupMessageWithASC(groupId);
        if (groupMessageList != null && groupMessageList.size() != 0){
            List<String> userIds = groupMessageList.stream().map(GroupMessage::getUserId).distinct().collect(Collectors.toList());
            List<UserDTO> userDTOS = userService.getUsersById(userIds);
            ArrayList<GroupMessageDTO> groupMessageDTOS = new ArrayList<>();
            for (GroupMessage userMessage : groupMessageList){
                GroupMessageDTO groupMessageDTO = BeanUtil.copyProperties(userMessage, GroupMessageDTO.class);
                for (UserDTO userDTO : userDTOS){
                    if (userDTO.getId().equals(userMessage.getUserId())){
                        groupMessageDTO.setUsername(userDTO.getUsername());
                        groupMessageDTOS.add(groupMessageDTO);
                        break;
                    }
                }
            }
            return groupMessageDTOS;
        }
        return null;
    }

    @Override
    public Page<GroupMessage> getAllGroupMessageWithASC(String userId, String groupId , Integer pageNum, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return groupMessageDao.getAllGroupMessageWithASC(userId, groupId, pageable);

        /*if (groupMessageList != null && groupMessageList.size() != 0){
            List<String> userIds = groupMessageList.stream().map(GroupMessage::getUserId).distinct().collect(Collectors.toList());
            List<UserDTO> userDTOS = userService.getUsersById(userIds);
            ArrayList<GroupMessageDTO> groupMessageDTOS = new ArrayList<>();
            for (GroupMessage userMessage : groupMessageList){
                GroupMessageDTO groupMessageDTO = BeanUtil.copyProperties(userMessage, GroupMessageDTO.class);
                for (UserDTO userDTO : userDTOS){
                    if (userDTO.getId().equals(userMessage.getUserId())){
                        groupMessageDTO.setUsername(userDTO.getUsername());
                        groupMessageDTOS.add(groupMessageDTO);
                        break;
                    }
                }
            }
            return groupMessageDTOS;
        }
        return null;*/
    }
}

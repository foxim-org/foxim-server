package com.hnzz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.commons.base.log.Log;
import com.hnzz.dao.ContactsDao;
import com.hnzz.dao.PrivateMessageDao;
import com.hnzz.dto.PrivateMessageDTO;
import com.hnzz.dto.UserDTO;
import com.hnzz.entity.Contacts;
import com.hnzz.entity.Message;
import com.hnzz.entity.PrivateMessage;
import com.hnzz.service.PrivateMessageService;
import com.hnzz.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author HB on 2023/1/30
 * TODO 私聊信息业务实现类
 */
@Service
public class PrivateMessageServiceImpl implements PrivateMessageService {
    @Resource
    private PrivateMessageDao privateMessageDao;

    @Resource
    private ContactsDao contactsDao;

    @Resource
    private UserService userService;


    @Override
    @Log("获取所有私聊信息并排序输出业务")
    public List<PrivateMessageDTO> getAllPrivateMessageWithASC(String userId, String contactId) {
        Contacts contacts = contactsDao.selectByUserId(userId, contactId);
        if (contacts==null){
            throw new AppException("您的这位好友不存在");
        }
        List<PrivateMessageDTO> privateMessageDTOS = new ArrayList<>();
        List<PrivateMessageDTO> privateMessageDTOSa = new ArrayList<>();
        List<PrivateMessage> message = privateMessageDao.getAllPrivateMessageWithASC(userId, contactId, contacts.getDelTime());
        List<String> messageId=new ArrayList<>();
        List<PrivateMessage> privateMessages=new ArrayList<>();
        for (PrivateMessage p:message) {
            if (p.getMsgId()!=null){
                messageId.add(p.getMsgId());
            }
        }
        List<Message> messages = privateMessageDao.getMessageByMsgId(messageId);

        for (int j = 0; j < messages.size(); j++) {
            if (j==(messages.size()-1)){
                if (messages.get(j).getMsgStatus()!=null&&messages.get(j).getMsgStatus().equals("1")){
                    for (PrivateMessage privateMessage : message) {
                        if (privateMessage.getMsgStatus()==0) {
                            privateMessage.setMsgStatus(1);
                            privateMessages.add(privateMessage);
                        }
                    }
                }
            }
        }
        for (PrivateMessage p: privateMessages) {
            privateMessageDao.savePrivateMessage(p);
        }

        List<PrivateMessage> newMessage = privateMessageDao.getAllPrivateMessageWithASC(userId, contactId, contacts.getDelTime());

        List<UserDTO> users = userService.getUsersById(Arrays.asList(userId, contactId));
        newMessage.forEach(m->{
            UserDTO user = users.stream().filter(f -> f.getId().equals(m.getUserId())).findFirst().orElse(null);
            if (user!=null){
                PrivateMessageDTO pr = BeanUtil.copyProperties(m, PrivateMessageDTO.class);
                pr.setAvatarUrl(user.getAvatarUrl());
                pr.setAudio(m.getAudio());
                pr.setAudioTime(m.getAudioTime());
                pr.setPlaying(m.isPlaying());
                pr.setWidth(m.getWidth());
                privateMessageDTOS.add(pr);
            }
        });

        for (int i = 0; i < privateMessageDTOS.size(); i++) {
            if (privateMessageDTOS.get(i).getText()!=null){
                privateMessageDTOSa.add(privateMessageDTOS.get(i));
            }
        }

        return privateMessageDTOSa;
    }

    @Override
    @Log("分页获取私聊信息并排序输出业务")
    public Page<PrivateMessage> getAllPrivateMessageWithASC(String userId, String contactId , Integer pageNum, Integer pageSize) {

        Contacts contacts = contactsDao.selectByUserId(userId, contactId);
        if (contacts==null){
            throw new AppException("您的这位好友不存在");
        }
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return privateMessageDao.getAllPrivateMessageWithASC(userId, contactId, contacts.getDelTime(), pageable);


    }

    @Override
    public void emptyPrivateMessages(String userId, String contactId) {
        Contacts contacts = contactsDao.selectByUserId(userId, contactId);
        if (contacts==null){
            throw new AppException("您的这位好友不存在");
        }
        privateMessageDao.emptyPrivateMessages(userId,contactId);
    }
}

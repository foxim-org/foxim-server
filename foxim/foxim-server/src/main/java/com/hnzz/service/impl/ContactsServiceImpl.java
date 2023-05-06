package com.hnzz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hnzz.commons.base.log.Log;
import com.hnzz.dao.ContactsDao;
import com.hnzz.dao.GroupDao;
import com.hnzz.dao.GroupMessageDao;
import com.hnzz.dao.GroupUserDao;
import com.hnzz.dto.*;
import com.hnzz.entity.*;
import com.hnzz.commons.base.enums.userenums.ContactStatus;
import com.hnzz.form.ContactsForm;
import com.hnzz.form.ForwardForm;
import com.hnzz.service.ContactsService;
import com.hnzz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import java.util.stream.Collectors;

/**
 * @PackageName:com.zzkj.service.impl
 * @ClassName:ContactsServiceImpl
 * @Author 周俊
 * @Date 2023/1/4 11:00
 * @Version 1.0
 **/
@Slf4j
@Service("contactsService")
public class ContactsServiceImpl implements ContactsService {

    @Resource
    private ContactsDao contactsDao;
    @Resource
    private GroupMessageDao groupMessageDao;
    @Resource
    private GroupDao groupDao;
    @Resource
    private GroupUserDao groupUserDao;
    @Resource
    private UserService userService;


    @Override
    @Log("查看最近联系人列表")
    public List<ContactSort> getContactSortByUserId(String userId) {
        // 新建一个界面展示的集合
        ArrayList<ContactSort> contactSorts = new ArrayList<>();
        // 找到群关系
        List<GroupUsers> groupUsersByUserId = groupDao.getGroupUsersByUserId(userId);
        if (groupUsersByUserId != null && groupUsersByUserId.size() > 0) {
            // 获取群id
            List<String> groupIds = groupUsersByUserId.stream().map(GroupUsers::getGroupId).collect(Collectors.toList());
            // 找到对应的群
            List<Group> groupByIds = groupDao.getGroupByIds(groupIds);
            // 找到群里的最后发言
            List<GroupMessage> groupMessageList = groupMessageDao.getLastCroupMessagesByCreateAt(groupIds);
            // 开始拼接
            for (String groupId : groupIds) {
                Group group = groupByIds.stream().filter(g -> g.getId().equals(groupId)).findFirst().orElse(null);
                GroupMessage groupMessage = groupMessageList.stream().filter(gm -> gm.getGroupId().equals(groupId)).findFirst().orElse(null);
                GroupUsers groupUsers = groupUsersByUserId.stream().filter(gu -> gu.getGroupId().equals(groupId)).findFirst().orElse(null);
                if (group != null && groupMessage != null && groupUsers != null) {
                    ContactSort contactSort = new ContactSort()
                            .setGroupId(groupId)
                            .setGroupName(group.getName())
                            .setGroupHead(group.getGroupHead())
                            .setText(groupMessage.getText())
                            .setIsSticky(groupUsers.getIsSticky())
                            .setIsMuted(groupUsers.getIsMuted())
                            .setRecentAt(groupMessage.getCreatedAt());

                    contactSorts.add(contactSort);
                }
            }
        }
        // 找到我的好友关系
        List<Contacts> myFriends = contactsDao.findMyFriends(userId);
        // 获取好友id和好友对象
        List<String> contactIds = new ArrayList<>();
        List<UserDTO> userDTOS = new ArrayList<>();
        if (myFriends != null && !myFriends.isEmpty()) {
            contactIds = myFriends.stream().map(Contacts::getContactId).distinct().collect(Collectors.toList());
            userDTOS = userService.getUsersById(contactIds);
        }
        // 获取聊天记录
        if (userDTOS != null && !userDTOS.isEmpty()) {
            for (String contactId : contactIds) {
                Contacts contacts = myFriends.stream().filter(my -> my.getUserId().equals(contactId) || my.getContactId().equals(contactId)).findFirst().orElse(null);
                UserDTO userDTO = userDTOS.stream().filter(u -> u.getId().equals(contactId)).findFirst().orElse(null);
                if (contacts != null && userDTO != null && contacts.getRecentAt() != null) {
                    ContactSort contactSort = new ContactSort();
                    contactSort.setContactId(contactId)
                            .setRecentAt(contacts.getRecentAt())
                            .setText(contacts.getLastText())
                            .setUsername(contacts.getRemark()!=null ? contacts.getRemark():contacts.getFriendName())
                            .setIsSticky(contacts.getIsSticky())
                            .setIsMuted(contacts.getIsMuted())
                            .setAvatarUrl(userDTO.getAvatarUrl());

                    contactSorts.add(contactSort);
                }
            }
        }
        ArrayList<ContactSort> allContactSorts = new ArrayList<>();
        ArrayList<ContactSort> isSticky = new ArrayList<>();
        ArrayList<ContactSort> puTong = new ArrayList<>();

        if (!contactSorts.isEmpty()) {
            for (ContactSort c : contactSorts) {
                if (c.getIsSticky()!=null&&c.getIsSticky()==true){
                    isSticky.add(c);
                }else {
                    puTong.add(c);
                }
            }
        }
        Collections.sort(isSticky);
        Collections.sort(puTong);

        allContactSorts.addAll(isSticky);
        allContactSorts.addAll(puTong);


        return allContactSorts;

    }


    @Override
    public boolean deleteId(String contactId, String userId) {
        return contactsDao.deleteId(contactId, userId);
    }

    @Override
    public List<String> findFriendsInfo(String userId) {
        List<Contacts> myFriends = contactsDao.findMyFriends(userId);
        return myFriends.stream().map(Contacts::getContactId).collect(Collectors.toList());
    }

    @Override
    public Contacts updateById(Contacts contacts) {
        contacts.setStatus(ContactStatus.BLOCKED.getCode());
        return contactsDao.updateById(contacts);
    }

    @Override
    public Contacts selectByUserId(String userId, String friendId) {
        return contactsDao.selectByUserId(userId, friendId);
    }

    @Override
    public Boolean existsContacts(String userId, String friendId){
        return contactsDao.exists(userId, friendId);
    }

    @Override
    public Contacts selectContacts(String userId, String friendId) {
        return contactsDao.selectContacts(userId, friendId);
    }

    @Override
    public Contacts addSave(String userId, String toId) {
        Contacts contacts = new Contacts();
        contacts.setUserId(userId);
        contacts.setContactId(toId);
        contacts.setStatus(ContactStatus.ACCEPTED.getCode());
        contacts.setRecentAt(new Date());
        contacts.setCreatedAt(new Date());
        return contactsDao.addSave(contacts);
    }


    @Override
    public Contacts updateContactId(ContactsForm contacts , String userId) {
        return contactsDao.updateContactId(contacts , userId);
    }

    @Override
    public List<String> blockList(String userId) {
        List<Contacts> myFriends = contactsDao.blockList(userId);
        return myFriends.stream().map(Contacts::getContactId).collect(Collectors.toList());
    }

    @Override
    public ContactsPage addressList(Integer pageNum, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Contacts> page = contactsDao.addressList(pageable, search);
        List<Contacts> content = page.getContent();
        List<ContactsDTO> contactsDTOS = new ArrayList<>();
        if (!content.isEmpty()) {
            List<String> userIds = content.stream().map(Contacts::getUserId).distinct().collect(Collectors.toList());
            List<String> friendIds = content.stream().map(Contacts::getContactId).distinct().collect(Collectors.toList());
            List<UserDTO> users = userService.getUsersById(userIds);
            List<UserDTO> friends = userService.getUsersById(friendIds);

            for (Contacts contacts : content) {
                UserDTO user = users.stream().filter(u -> u.getId().equals(contacts.getUserId())).findFirst().orElse(null);
                UserDTO friend = friends.stream().filter(f -> f.getId().equals(contacts.getContactId())).findFirst().orElse(null);
                if (user != null && friend != null) {
                    ContactsDTO contactsDTO = BeanUtil.copyProperties(contacts, ContactsDTO.class);
                    contactsDTO.setMobile(user.getMobile());
                    contactsDTO.setIsMyCircle(Boolean.TRUE);
                    contactsDTO.setIsFriendCircle(Boolean.TRUE);
                    contactsDTOS.add(contactsDTO);
                }
            }
        }
        log.info("addressList: {}", contactsDTOS);
        return new ContactsPage(page, contactsDTOS);
    }

    @Override
    public List<ContactsSearch> contactsList(String search) {
        List<Contacts> list = contactsDao.contactsList(search);
        List<ContactsSearch> contactsSearches = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            List<String> friendIds = list.stream().map(Contacts::getContactId).distinct().collect(Collectors.toList());
            List<UserDTO> friends = userService.getUsersById(friendIds);
            for (Contacts contacts : list) {
                UserDTO friend = friends.stream().filter(f -> f.getId().equals(contacts.getContactId())).findFirst().orElse(null);
                if (friend != null) {
                    ContactsSearch contactsSearch = BeanUtil.copyProperties(contacts, ContactsSearch.class);
                    contactsSearch.setId(friend.getId());
                    contactsSearch.setAvatarUrl(friend.getAvatarUrl());
                    contactsSearches.add(contactsSearch);
                }
            }
        }
        return contactsSearches;
    }

    @Override
    public Contacts addContacts(String userId, String contactId) {
        // TODO 判断是否存在好友关系
        boolean existContacts = contactsDao.existContacts(userId, contactId);
        if (!existContacts) {
            UserDTO userDTO = userService.findUserById(contactId);
            Contacts contacts = new Contacts();
            contacts.setUserId(userId);
            contacts.setContactId(contactId);
            contacts.setRemark(userDTO.getRemark());
            contacts.setFriendName(userDTO.getUsername());
            contacts.setCreatedAt(new Date());
            contacts.setHead(userDTO.getAvatarUrl());
            contacts.setStatus(ContactStatus.PENDING.getCode());
            return contactsDao.addContacts(contacts);
        }
        return null;
    }


    @Override
    public List<Contacts> contactsRequests(String userId) {
        List<Contacts> contactsList = contactsDao.contactsRequests(userId);
        if (contactsList!=null && contactsList.size()>0) {
            List<String> contactIds = contactsList.stream().map(Contacts::getUserId).collect(Collectors.toList());
            List<UserDTO> usersById = userService.getUsersById(contactIds);
            for (Contacts contacts : contactsList) {
                for (UserDTO user : usersById) {
                    if (user.getId().equals(contacts.getUserId())) {
                        contacts.setFriendName(user.getUsername());
                        usersById.remove(user);
                        break;
                    }
                }
            }
        }
        return contactsList;
    }

    @Override
    public Contacts agreeContacts(String userId, String contactId) {
        // 寻找 申请人 与 我 的关系是否存在
        Contacts thisContacts = contactsDao.selectContacts(contactId, userId);
        if (thisContacts!=null) {
            if (Objects.equals(ContactStatus.PENDING.getCode() , thisContacts.getStatus())){
                // 修改对方好友状态为同意
                contactsDao.agreeContacts(userId, contactId);
                UserDTO userDTO = userService.findUserById(contactId);
                Contacts contacts = new Contacts();
                contacts.setUserId(userId);
                contacts.setContactId(contactId);
                contacts.setRemark(userDTO.getRemark());
                contacts.setFriendName(userDTO.getUsername());
                contacts.setCreatedAt(new Date());
                contacts.setHead(userDTO.getAvatarUrl());
                contacts.setStatus(ContactStatus.ACCEPTED.getCode());
                return contactsDao.addContacts(contacts);
            }
        }
        return null;
    }

    @Override
    public boolean deleteContacts(String userId, String contactId) {
        return contactsDao.deleteContacts(userId, contactId);
    }

    @Override
    public List<ForwardDTO> getForwardList(String userId) {
        List<Contacts> myForwardFriends = contactsDao.findMyForwardFriends(userId);
        ArrayList<ForwardDTO> forwardDTOS = new ArrayList<>();
        if (myForwardFriends != null && !myForwardFriends.isEmpty()) {
            for (Contacts contact : myForwardFriends) {
                ForwardDTO forwardDTO = new ForwardDTO();
                forwardDTO.setId(contact.getContactId())
                        .setRemark(contact.getRemark())
                        .setAvatarUrl(contact.getHead())
                        .setLastForwardTime(contact.getLastForwardTime())
                        .setForwardTo("private");
                forwardDTOS.add(forwardDTO);
            }
        }

        List<GroupUsers> groupUsersByUserId = groupDao.getGroupUsersByUserId(userId);
        if (groupUsersByUserId!=null && !groupUsersByUserId.isEmpty()) {
            List<GroupUsers> groupUsersList = groupUsersByUserId.stream().filter(v -> v.getLastForwardTime() != null).collect(Collectors.toList());
            if (groupUsersList.size()>0){
                List<String> groupIds = groupUsersList.stream().map(GroupUsers::getGroupId).collect(Collectors.toList());
                List<Group> groupList = groupDao.getGroupByIds(groupIds);
                for (GroupUsers groupUsers : groupUsersList){
                    ForwardDTO forwardDTO = new ForwardDTO();
                    forwardDTO.setForwardTo("group");
                    for (Group group : groupList){
                        if (group.getId().equals(groupUsers.getGroupId()) && groupUsers.getLastForwardTime()!=null){
                            forwardDTO.setRemark(group.getName());
                            forwardDTO.setId(groupUsers.getGroupId());
                            forwardDTO.setAvatarUrl(group.getGroupHead());
                            forwardDTO.setLastForwardTime(groupUsers.getLastForwardTime());
                            forwardDTOS.add(forwardDTO);
                            break;
                        }
                    }
                }
            }
        }
        forwardDTOS.sort((o1, o2) -> o2.getLastForwardTime().compareTo(o1.getLastForwardTime()));
        return forwardDTOS;
    }

    @Override
    public void setForwardTime(ForwardForm forwardForm, String userId) {
        if (forwardForm.getId()!=null && !forwardForm.getId().isEmpty()) {
            contactsDao.setForwardTime(forwardForm.getId() , userId);
        }
        if (forwardForm.getGroupId()!=null && !forwardForm.getGroupId().isEmpty()) {
            groupUserDao.setForwardTime(forwardForm.getGroupId() , userId);
        }
    }

    @Override
    public List<Contacts> getGroupUserContacts(List<String> usersId, String userId) {
        return contactsDao.getGroupUserContacts(usersId, userId);
    }

    @Override
    public List<Contacts> findMyFriends(String userId) {
        return contactsDao.findMyFriends(userId);
    }

}

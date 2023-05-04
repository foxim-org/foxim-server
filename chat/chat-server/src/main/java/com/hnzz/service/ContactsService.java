package com.hnzz.service;

import com.hnzz.dto.*;

import com.hnzz.entity.Contacts;
import com.hnzz.form.ContactsForm;
import com.hnzz.form.ForwardForm;

import java.util.List;

/**
 * @PackageName:com.zzkj.service
 * @InterfaceName:ContactsService
 * @Author 周俊
 * @Date 2023/1/4 10:59
 * @Version 1.0
 **/
public interface ContactsService {

    Contacts addSave(String userId, String toId);

    /**
     * @param userId
     * @return 返回关系的有序排列
     * @Author HB
     */
    List<ContactSort> getContactSortByUserId(String userId);

    boolean deleteId(String contactId,String userId);

    List<String> findFriendsInfo(String userId);

    Contacts updateById(Contacts contacts);

    Contacts selectByUserId(String userId,String friendId);

    Boolean existsContacts(String userId, String friendId);

    Contacts selectContacts(String userId, String friendId);

    Contacts updateContactId(ContactsForm contacts , String userId);

    List<String> blockList(String userId);

    ContactsPage addressList(Integer pageNum, Integer pageSize, String search);

    List<ContactsSearch> contactsList(String search);

    Contacts addContacts(String userId, String contactId);

    List<Contacts> contactsRequests(String userId);

    Contacts agreeContacts(String userId, String contactId);

    boolean deleteContacts(String userId, String contactId);

    List<ForwardDTO> getForwardList(String userId);

    void setForwardTime(ForwardForm forwardForm, String userId);

    List<Contacts> getGroupUserContacts(List<String> usersId, String userId);

    List<Contacts> findMyFriends(String userId);
}

package com.hnzz.dao;

import com.hnzz.entity.ContactsManager;
import com.hnzz.entity.Contacts;
import com.hnzz.form.ContactsForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @PackageName:com.zzkj.dao
 * @ClassName:ContactsDao
 * @Author 周俊
 * @Date 2023/1/4 10:55
 * @Version 1.0
 **/

public interface ContactsDao {

    List<Contacts> findMyFriends(String userId);

    boolean deleteId(String contactId,String userId);

    Contacts updateById(Contacts contacts);

    Contacts selectByUserId(String userId, String friendId);

    Contacts selectContacts(String userId, String friendId);

    Contacts addSave(Contacts contacts);

    Contacts updateContactId(ContactsForm contacts , String userId);

    List<Contacts> blockList(String userId);

    Page<Contacts> addressList(Pageable pageable,String search);

    List<Contacts> contactsList(String search);

    Contacts addContacts(Contacts contacts);

    boolean existContacts(String userId, String contactId);

    List<Contacts> contactsRequests(String userId);

    void agreeContacts(String userId, String contactId);

    boolean deleteContacts(String userId, String contactId);

    ContactsManager selectContactsId(String userId, String contactId);

    List<Contacts> findMyForwardFriends(String userId);

    void setForwardTime(List<String> friendIds, String userId);

    Boolean exists(String userId, String friendId);

    List<Contacts> getGroupUserContacts(List<String> usersId, String userId);

    Contacts findThisContact(String userId, String contactId);
}


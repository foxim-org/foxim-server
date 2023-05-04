package com.hnzz.dao.impl;

import com.hnzz.commons.base.enums.userenums.ContactStatus;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.dao.ContactsDao;
import com.hnzz.entity.ContactsManager;
import com.hnzz.entity.Contacts;
import com.hnzz.entity.User;
import com.hnzz.form.ContactsForm;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * @PackageName:com.zzkj.dao.impl
 * @ClassName:GroupDaoImpl
 * @Author 周俊
 * @Date 2023/1/4 10:56
 * @Version 1.0
 **/
@Repository
public class ContactsDaolmpl implements ContactsDao {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<Contacts> findMyFriends(String userId) {
        return mongoTemplate.find(new Query(Criteria.where("userId").is(userId).and("status").is(ContactStatus.ACCEPTED.getCode())), Contacts.class);
    }

    @Override

    public boolean deleteId(String contactId, String userId) {
        DeleteResult result = mongoTemplate.remove(new Query(Criteria.where("contactId").is(contactId).and("userId").is(userId)), Contacts.class);
        return result.wasAcknowledged();
    }


    @Override
    public Contacts updateById(Contacts contacts) {
        return mongoTemplate.save(contacts);
    }

    @Override
    public Contacts selectByUserId(String userId, String friendId) {
        Query query = new Query(Criteria.where("userId").is(userId)
                .and("contactId").is(friendId)
                .and("status").is(ContactStatus.ACCEPTED.getCode()));

        return mongoTemplate.findOne(query, Contacts.class);
    }
    @Override
    public Contacts selectContacts(String userId, String friendId) {
        Query query = new Query(Criteria.where("userId").is(userId)
                .and("contactId").is(friendId));

        return mongoTemplate.findOne(query, Contacts.class);
    }

    @Override
    public Contacts addSave(Contacts contacts) {
        return mongoTemplate.save(contacts);
    }

    @Override
    public Contacts updateContactId(ContactsForm contacts ,String userId) {

        Query query = new Query(Criteria.where("contactId").is(contacts.getContactId()).and("userId").is(userId));
        Update update = new Update();

        Optional.ofNullable(contacts.getTags()).ifPresent(v -> update.set("tags", v));
        Optional.ofNullable(contacts.getRemark()).ifPresent(v -> update.set("remark", v));
        Optional.ofNullable(contacts.getBgUrl()).ifPresent(v -> update.set("bgUrl", v));
        Optional.ofNullable(contacts.getIsSticky()).ifPresent(v -> update.set("isSticky", v));
        Optional.ofNullable(contacts.getIsMuted()).ifPresent(v -> update.set("isMuted", v));
        Optional.ofNullable(contacts.getEmpty()).ifPresent(v -> update.set("empty", v));

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
        return mongoTemplate.findAndModify(query, update, options, Contacts.class);
    }

    @Override
    public List<Contacts> blockList(String userId) {
        return mongoTemplate.find(new Query(Criteria.where("userId").is(userId).and("status").is(ContactStatus.BLOCKED.getCode())), Contacts.class);
    }

    @Override
    public Page<Contacts> addressList(Pageable pageable, String search) {
        Criteria criteria = new Criteria();
        if (search != null) {
            User user = mongoTemplate.findOne(new Query(Criteria.where("username").regex(".*" + search + ".*")), User.class);
            Optional.ofNullable(user).ifPresent(u -> {
                criteria.orOperator(Criteria.where("userId").is(u.getId()));
            });
        }
        Query query = new Query(criteria);
        long count = mongoTemplate.count(query, Contacts.class);
        List<Contacts> groupList = mongoTemplate.find(query.with(pageable), Contacts.class);
        return new PageImpl<>(groupList, pageable, count);
    }

    @Override
    public List<Contacts> contactsList(String search) {
        Criteria criteria = new Criteria();
        if (search != null) {
            User user = mongoTemplate.findOne(new Query(Criteria.where("username").regex(".*" + search + ".*")), User.class);
            if (user == null) {
                throw new AppException("未查到信息");
            }
            criteria.orOperator(Criteria.where("contactId").is(user.getId()));
        }
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Contacts.class);
    }

    @Override
    public Contacts addContacts(Contacts contact) {
        return mongoTemplate.save(contact);
    }

    @Override
    public boolean existContacts(String userId, String contactId){
        Query query = Query.query(Criteria.where("userId").is(userId)
                .and("contactId").is(contactId).and("status").is(ContactStatus.ACCEPTED.getCode()));
        return mongoTemplate.exists(query, Contacts.class);
    }

    @Override
    public List<Contacts> contactsRequests(String userId) {
        Criteria criteria = Criteria.where("contactId").is(userId).and("status").is(ContactStatus.PENDING);
        Query query =new  Query(criteria).with(Sort.by(Sort.Direction.ASC,"createAt"));
        return mongoTemplate.find(query, Contacts.class);
    }

    @Override
    public void agreeContacts(String userId, String contactId) {
        Query query = Query.query(Criteria.where("userId").is(contactId)
                .and("contactId").is(userId).and("status").is(ContactStatus.PENDING));
        Update update = new Update();
        update.set("status", ContactStatus.ACCEPTED.getCode());
        mongoTemplate.updateFirst(query, update, Contacts.class);
    }

    @Override
    public boolean deleteContacts(String userId, String contactId) {
        Query query = Query.query(Criteria.where("userId").is(userId)
                .and("contactId").is(contactId).and("status").is(ContactStatus.PENDING));
        DeleteResult remove = mongoTemplate.remove(query, Contacts.class);
        return remove.wasAcknowledged();
    }

    @Override
    public ContactsManager selectContactsId(String userId, String contactId) {
        Query query = new Query(Criteria.where("userId").is(userId)
                .and("contactId").is(contactId)
                .and("status").is(ContactStatus.ACCEPTED.getCode()));
        return mongoTemplate.findOne(query, ContactsManager.class);
    }

    @Override
    public List<Contacts> findMyForwardFriends(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId).and("lastForwardTime").ne(null)).with(Sort.by(Sort.Direction.ASC,"lastForwardTime"));
        return mongoTemplate.find(query, Contacts.class);
    }

    @Override
    public void setForwardTime(List<String> friendIds, String userId) {
        Query query = new Query(Criteria.where("userId").is(userId).and("contactId").in(friendIds));
        Update update = new Update();
        update.set("lastForwardTime", new Date());
        mongoTemplate.updateMulti(query, update, Contacts.class);
    }

    @Override
    public Boolean exists(String userId, String friendId) {
        Query query = new Query(Criteria.where("userId").is(userId).and("contactId").is(friendId));
        return mongoTemplate.exists(query , Contacts.class);
    }

    @Override
    public List<Contacts> getGroupUserContacts(List<String> usersId, String userId) {
        return mongoTemplate.find(Query.query(Criteria.where("contactId").in(usersId).and("userId").is(userId)), Contacts.class);
    }

    @Override
    public Contacts findThisContact(String userId, String contactId) {
        return mongoTemplate.findOne(Query.query(Criteria.where("contactId").is(contactId).and("userId").is(userId)), Contacts.class);
    }

}

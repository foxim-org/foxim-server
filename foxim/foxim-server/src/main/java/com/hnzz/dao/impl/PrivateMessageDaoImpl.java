package com.hnzz.dao.impl;

import com.hnzz.dao.PrivateMessageDao;
import com.hnzz.entity.PrivateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author HB on 2023/1/30
 * TODO 私聊信息dao层实现
 */
@Slf4j
@Repository
public class PrivateMessageDaoImpl implements PrivateMessageDao {

    @Resource
    MongoTemplate mongoTemplate;

    @Override
    public List<PrivateMessage> getAllPrivateMessageWithASC(String conversationId){


        Query query = new Query(Criteria.where("conversationId").is(conversationId))
                .with(Sort.by(Sort.Direction.ASC,"createdAt"));

        return mongoTemplate.find(query, PrivateMessage.class);
    }
    @Override
    public List<PrivateMessage> getAllPrivateMessageWithASC(String userId, String contactId, Date delTime) {
        Criteria in = Criteria.where("userId").in(userId, contactId)
                .and("contactId").in(userId, contactId);
        if (delTime!=null){
            in.and("delTime").gt(delTime);
        }
        Query query = new Query(in).with(Sort.by(Sort.Direction.ASC, "createdAt"));
        return mongoTemplate.find(query,PrivateMessage.class);
    }

    @Override
    public List<PrivateMessage> getAllPrivateMessageWithASC(String userId, String contactId) {
        Criteria in = Criteria.where("userId").in(userId, contactId).and("contactId").in(userId, contactId);
        Query query = new Query(in).with(Sort.by(Sort.Direction.ASC, "createdAt"));
        return mongoTemplate.find(query,PrivateMessage.class);
    }
    @Override
    public Page<PrivateMessage> getAllPrivateMessageWithASC(String userId, String contactId, Date delTime , Pageable pageable) {
        Criteria criteria = Criteria.where("userId").in(userId, contactId)
                .and("contactId").in(userId, contactId);
        if (delTime!=null){
            criteria.and("delTime").gt(delTime);
        }
        Query query = new Query(criteria).with(Sort.by(Sort.Direction.ASC, "createdAt"));
        long count = mongoTemplate.count(query, PrivateMessage.class);
        List<PrivateMessage> privateMessages = mongoTemplate.find(query.with(pageable), PrivateMessage.class);
        return new PageImpl<>(privateMessages, pageable, count);
    }


    @Override
    public List<PrivateMessage> getLastPrivateMessage(String userId, List<String> contactIds) {
        List<String> conversationIds = new ArrayList<>();
        for (String contactId : contactIds) {
            String conversationId = PrivateMessage.createConversationId(userId, contactId);
            conversationIds.add(conversationId);
        }
        MatchOperation match = Aggregation.match(Criteria.where("conversationId").in(conversationIds));
        GroupOperation group = Aggregation.group("$conversationId")
                .last("createdAt").as("createdAt")
                .last("_id").as("id")
                .last("conversationId").as("conversationId")
                .last("topic").as("topic")
                .last("text").as("text")
                .last("userId").as("userId")
                .last("contactId").as("contactId");
        SortOperation sort = Aggregation.sort(Sort.Direction.ASC, "createdAt");
        Aggregation aggregation = Aggregation.newAggregation(match, group, sort);
        log.info("查询语句为: {}",aggregation);
        AggregationResults<PrivateMessage> results = mongoTemplate.aggregate(aggregation, "private-messages", PrivateMessage.class);
        return results.getMappedResults();
    }

    @Override
    public void emptyPrivateMessages(String userId, String contactId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").in(userId, contactId).and("contactId").in(userId, contactId));
        mongoTemplate.findAllAndRemove(query,PrivateMessage.class);
    }
}

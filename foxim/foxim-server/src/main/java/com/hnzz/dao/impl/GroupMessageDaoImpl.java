package com.hnzz.dao.impl;

import com.hnzz.dao.GroupMessageDao;
import com.hnzz.entity.GroupMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @PackageName:com.hnzz.dao.impl
 * @ClassName:GroupMessageDaoImpl
 * @Author 冼大丰
 * @Date 2023/1/31 16:45
 * @Version 1.0
 **/
@Repository
public class GroupMessageDaoImpl implements GroupMessageDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<GroupMessage> getAllGroupMessageWithASC(String groupId) {
        Query query = new Query(Criteria.where("groupId").is(groupId)).with(Sort.by(Sort.Direction.ASC,"createdAt"));
        return mongoTemplate.find(query, GroupMessage.class);
    }

    @Override
    public Page<GroupMessage> getAllGroupMessageWithASC(String userId, String groupId, Pageable pageable) {

        Query query = new Query(Criteria.where("groupId").is(groupId)).with(Sort.by(Sort.Direction.ASC,"createdAt"));
        long count = mongoTemplate.count(query, GroupMessage.class);
        List<GroupMessage> groupMessages = mongoTemplate.find(query.with(pageable), GroupMessage.class);
        return  new PageImpl<>(groupMessages, pageable, count);
    }

    @Override
    public List<GroupMessage> getLastCroupMessagesByCreateAt(List<String> groupIds) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("groupId").in(groupIds)),
                Aggregation.group("groupId").max("createdAt").as("createdAt").
                        last("_id").as("id").
                        last("topic").as("topic").
                        last("userId").as("userId").
                        last("text").as("text").
                        last("username").as("username").
                        last("avatarUrl").as("avatarUrl").
                        last("groupId").as("groupId"),

                Aggregation.project("_id","topic","userId","text","username","avatarUrl","groupId", "createdAt")
        );
        AggregationResults<GroupMessage> result = mongoTemplate.aggregate(aggregation, "group-messages", GroupMessage.class);
        List<GroupMessage> mappedResults = result.getMappedResults();
        return mappedResults;
    }

    @Override
    public Object deleteMessages(String groupId) {
        Query query = new Query(Criteria.where("groupId").is(groupId));
        return mongoTemplate.findAllAndRemove(query,GroupMessage.class);
    }
}

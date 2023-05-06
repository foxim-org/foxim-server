package com.hnzz.config;


import com.hnzz.entity.bot.Bots;
import com.hnzz.entity.PrivateMessage;
import com.hnzz.entity.User;
import com.hnzz.entity.system.Setting;
import com.mongodb.client.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

/**
 * @author HB
 * @Classname MongoDbConfig
 * @Date 2023/1/11 17:04
 * @Description TODO
 */
@Configuration
public class MongodbConfig {
    final MongoTemplate mongoTemplate;

    public MongodbConfig(MongoTemplate mongoTemplate, MongoClient mongoClient) {
        this.mongoTemplate = mongoTemplate;
    }
    @EventListener(ApplicationReadyEvent.class)
    public void initIndicesAfterStartup() {
        // User是实体类，给User集合中的username和mobile字段加索引，并且是唯一索引
        mongoTemplate.indexOps(User.class).ensureIndex(new Index().on("username", Sort.Direction.ASC).unique());

        mongoTemplate.indexOps(Setting.class).ensureIndex(new Index().on("name", Sort.Direction.ASC).unique());

        /*mongoTemplate.indexOps(User.class).ensureIndex(new Index().on("mobile", Sort.Direction.ASC).unique());*/

        mongoTemplate.indexOps(PrivateMessage.class).ensureIndex(new Index().on("conversationId",Sort.Direction.ASC));

    }
}

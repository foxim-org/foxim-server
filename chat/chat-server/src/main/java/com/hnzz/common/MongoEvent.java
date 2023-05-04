//package com.hnzz.common;
//
//import com.mongodb.CursorType;
//import com.mongodb.client.*;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.changestream.ChangeStreamDocument;
//import lombok.extern.slf4j.Slf4j;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;
//
///**
// * @author HB on 2023/2/14
// * TODO mongodb变更流监听
// */
//@Slf4j
//@Component
//public class MongoEvent {
//    @Resource
//    private MongoTemplate mongoTemplate;
//
//    @PostConstruct
//    public void listenToDbChange(){
//        MongoDatabase db = mongoTemplate.getDb();
//        List<String> collectionNames = db.listCollectionNames().into(new ArrayList<>());
//        for (String collectionName : collectionNames){
//            log.info("当前操作集合名:==>{}",collectionName);
//            MongoCollection<Document> collection = db.getCollection(collectionName);
//            Bson filter = Filters.exists("operationType");
//            collection.find(filter).cursorType(CursorType.TailableAwait).forEach(
//                    (Consumer<Document>) document -> {
//                        log.info("数据发生变化，集合名：" + collectionName + "，变化类型：" + document.get("operationType"));
//                    }
//            );
//        }
//    }
//}

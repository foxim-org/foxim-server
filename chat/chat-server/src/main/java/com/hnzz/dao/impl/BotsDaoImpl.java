package com.hnzz.dao.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.dao.BotsDao;
import com.hnzz.dao.GroupDao;
import com.hnzz.entity.Group;
import com.hnzz.entity.bot.AutoReply;
import com.hnzz.entity.bot.Bots;
import com.hnzz.form.bots.BotsAutoReplySetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author HB on 2023/2/15
 * TODO 机器人dao层实现
 */
@Slf4j
@Repository
public class BotsDaoImpl implements BotsDao {
    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private GroupDao groupDao;

    @Override
    public Bots createBot(Bots bots) {
        return mongoTemplate.insert(bots);
    }

    @Override
    public Bots settingBot(Bots bots) {
        Update update = new Update();

        Optional.ofNullable(bots.getUpdatedAt()).ifPresent(v->update.set("updatedAt",v));
        Optional.ofNullable(bots.getIsEnable()).ifPresent(v->update.set("isEnable",v));
        Optional.ofNullable(bots.getUsername()).ifPresent(v->update.set("username",v));
        Optional.ofNullable(bots.getAvatarUrl()).ifPresent(v->update.set("avatarUrl",v));
        Optional.ofNullable(bots.getJoinGroup()).ifPresent(v->{
            try {
                Map<String, Object> emptyFields = getNotEmptyFields(v);
                Set<String> strings = emptyFields.keySet();
                for (String s: strings) {
                    update.set("joinGroup."+s,emptyFields.get(s));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        Optional.ofNullable(bots.getAutoForward()).ifPresent(v->{
            try {
                Map<String, Object> emptyFields = getNotEmptyFields(v);
                Set<String> strings = emptyFields.keySet();
                for (String s: strings) {
                    update.set("autoForward."+s,emptyFields.get(s));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        Optional.ofNullable(bots.getSpeechManage()).ifPresent(v->{
            try {
                Map<String, Object> emptyFields = getNotEmptyFields(v);
                Set<String> strings = emptyFields.keySet();
                for (String s: strings) {
                    update.set("speechManage."+s,emptyFields.get(s));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        Optional.ofNullable(bots.getTimedMessage()).ifPresent(v->{
            try {
                Map<String, Object> emptyFields = getNotEmptyFields(v);
                Set<String> strings = emptyFields.keySet();
                for (String s: strings) {
                    update.set("timedMessage."+s,emptyFields.get(s));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        Query query = new Query(Criteria.where("id").is(bots.getId()));
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
        return mongoTemplate.findAndModify(query,update,options, Bots.class);
    }

    private Map<String, Object> getNotEmptyFields(Object object) throws IllegalAccessException {
        Map<String, Object> resultMap = new HashMap<>(16);
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            if (fieldValue != null && !fieldValue.toString().trim().isEmpty()) {
                resultMap.put(field.getName(), fieldValue);
            }
        }
        return resultMap;
    }

    @Override
    public Bots settingBotsAutoReplies(BotsAutoReplySetting setting){
        Query query = new Query(Criteria.where("_id").is(setting.getId()));

        Update update = new Update();

        Optional.ofNullable(setting.getAutoReplies()).ifPresent(v->{
            String type = setting.getType();
            List<AutoReply> autoReplies = setting.getAutoReplies();
            if (type.equals("add")){
                for (AutoReply a:autoReplies) {
                    Map<String, Object> map = BeanUtil.beanToMap(a, false, true);
                    String s = DigestUtil.md5Hex(JSONUtil.toJsonStr(map));
                    a.setReplyId(s);
                }
                update.push("autoReply").each(v);
            }else if (type.equals("put")){
                for (AutoReply a:autoReplies) {
                    if (a.getReplyId()!=null&& !a.getReplyId().trim().isEmpty()){
                        update.set("autoReply.$[elem]",a).filterArray(Criteria.where("elem.replyId").is(a.getReplyId()));
                    }
                }
            }else {
                for (AutoReply a:autoReplies) {
                    if (a.getReplyId()!=null&& !a.getReplyId().trim().isEmpty()){
                        update.pull("autoReply",Query.query(Criteria.where("replyId").is(a.getReplyId())));
                    }
                }
            }
        });
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
        return mongoTemplate.findAndModify(query,update,options,Bots.class);
    }

    @Override
    public Bots getBotById(String id) {
        Bots bot = mongoTemplate.findById(id, Bots.class);
        if (bot==null){
            Group groupById = groupDao.getGroupById(id);
            if (groupById==null){
                throw new AppException("群"+id+"不存在");
            }
            return this.createBot(new Bots(groupById));
        }
        return bot;
    }
}

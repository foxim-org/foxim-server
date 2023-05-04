package com.hnzz.unit.dao;

import com.hnzz.entity.PrivateMessage;
import com.hnzz.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HB on 2023/2/13
 * TODO 私聊消息dao层测试
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PrivateDaoTest {
    @Resource
    private MongoTemplate template;

    @Test
    public void aaa(){
        List<PrivateMessage> privateMessages = template.find(new Query(), PrivateMessage.class);
        for (PrivateMessage p: privateMessages) {
            if (p.getConversationId()==null){
                p.setConversationId(PrivateMessage.createConversationId(p.getUserId(),p.getContactId()));
            }
            template.save(p,"private-messages");
        }
    }

    @Test
    public void bbb(){
        User byId = template.findById("63bbe0668a05c10c74dc431f", User.class);
        log.info("查询到用户信息为: {}",byId);
    }
}

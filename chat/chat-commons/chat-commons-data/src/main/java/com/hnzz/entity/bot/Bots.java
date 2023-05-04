package com.hnzz.entity.bot;

import com.hnzz.entity.Group;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author HB on 2023/2/15
 * TODO 机器人实体类
 */
@Data
@Document("bots")
@Accessors(chain = true)
@NoArgsConstructor
public class Bots {
    @Id
    private String id;
    @Field
    private Date createdAt;
    @Field
    private Date updatedAt;
    @Field
    private Boolean isEnable;
    @Field
    private String username;
    @Field
    private String avatarUrl;

    /**
     * 入群欢迎设置
     */
    @Field
    private BotsJoinGroup joinGroup;
    /**
     * 自动问答设置
     */
    @Field
    private List<BotsAutoReply> autoReply;
    /**
     * 自动转发设置
     */
    @Field
    private BotsAutoForward autoForward;
    /**
     * 发言管理设置
     */
    @Field
    private BotsSpeechManage speechManage;
    /**
     * 定时消息设置
     */
    @Field
    private BotsTimedMessage timedMessage;

    public Bots (Group group){
        this.setId(group.getId())
                .setAvatarUrl(group.getGroupHead())
                .setIsEnable(group.getOpenBots())
                .setUsername(group.getName()+"机器人")
                .setCreatedAt(group.getCreatedAt())
                .setUpdatedAt(group.getCreatedAt())
                .setJoinGroup(new BotsJoinGroup())
                .setAutoReply(new ArrayList<>())
                .setAutoForward(new BotsAutoForward())
                .setSpeechManage(new BotsSpeechManage())
                .setTimedMessage(new BotsTimedMessage());
    }

}

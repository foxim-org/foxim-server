package com.hnzz.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.zzkj.entity
 * @ClassName:Group
 * @Author 冼大丰
 * @Date 2023/1/4 10:22
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Group implements Serializable {
    /**
     * 群id
     */
    @Id
    private String id;
    /**
     * 群狐狸号
     */
    private Integer foxCode;
    /**
     * 群名称
     */
    @Field
    private String name;
    /**
     * 群头像
     */
    @Field
    private String groupHead;
    /**
     * 群介绍
     */
    @Field
    private String desc;
    /**
     * 群公告
     */
    @Field
    private String notice;
    /**
     * 群的标签
     */
    @Field
    private List<String> tags;
    /**
     * 创建群的用户id
     */
    @Field
    private String ownerId;
    /**
     * 群聊背景
     */
    @Field
    private String backGround="默认背景";
    /**
     * 是否开启机器人功能
     */
    @Field
    private Boolean openBots;
    /**
     * 是否允许通过群添加好友
     */
    @Field
    private Boolean addAble;
    /**
     * 是否允许撤回
     */
    @Field
    private Boolean undoAble;
    /**
     * 是否允许群成员更改群组头像
     */
    @Field
    private Boolean ChangeGroupHead;
    /**
     * 是否允许群成员更改群组名称
     */
    @Field
    private Boolean ChangeGroupName;
    /**
     * 是否允许群成员新增成员
     */
    @Field
    private Boolean addGroupUser;
    /**
     * 是否允许群成员新增或编辑贴文
     */
    @Field
    private Boolean editorialPost;
    /**
     * 是否允许群成员提醒他人
     */
    @Field
    private Boolean remindOthers;
    /**
     * 是否允许群成员群组通话
     */
    @Field
    private Boolean groupCall;
    /**
     * 是否允许群成员成员私聊
     */
    @Field
    private Boolean privateMemberChat;
    /**
     * 是否全体被禁言
     */
    private Boolean isSilencedToAll;
    /**
     * 全局禁言,不禁言
     */
    @Field
    private Date silencedTo;
    /**
     * 群聊是否存在（true为存在，false为解散）
     */
    @Field
    private Boolean isLive=true;
    /**
     * 创建群聊的时间
     */
    @Field
    private Date createdAt;

}

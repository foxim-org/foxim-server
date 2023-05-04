package com.hnzz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.hnzz.entity
 * @ClassName:AdminGroup
 * @Author zj
 * @Date 2023/4/5 16:39
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class GroupAdmin implements Serializable {

    @ApiModelProperty("群id")
    private String id;
    @ApiModelProperty("群狐狸号")
    private Integer foxCode;

    @ApiModelProperty("群名称")
    private String name;

    @ApiModelProperty("群主昵称")
    private String ownerName;

    @ApiModelProperty("群头像")
    private String groupHead="默认头像";

    @ApiModelProperty("群介绍")
    private String desc;

    @ApiModelProperty("群公告")
    private String notice;

    @ApiModelProperty("群的标签")
    private List<String> tags;

    @ApiModelProperty("创建群的用户id")
    private String ownerId;

    @ApiModelProperty("群聊背景")
    private String backGround="默认背景";

    @ApiModelProperty("是否开启机器人功能")
    private Boolean openBots;
    /**
     * 是否允许通过群添加好友
     */
    @ApiModelProperty("是否允许通过群添加好友")
    private Boolean addAble;
    /**
     * 是否允许撤回
     */
    @ApiModelProperty("是否允许撤回")
    private Boolean undoAble;
    /**
     * 是否允许群成员更改群组头像
     */
    @ApiModelProperty("是否允许群成员更改群组头像")
    private Boolean ChangeGroupHead;
    /**
     * 是否允许群成员更改群组名称
     */
    @ApiModelProperty("是否允许群成员更改群组名称")
    private Boolean ChangeGroupName;
    /**
     * 是否允许群成员新增成员
     */
    @ApiModelProperty("是否允许群成员新增成员")
    private Boolean addGroupUser;
    /**
     * 是否允许群成员新增或编辑贴文
     */
    @ApiModelProperty("是否允许群成员新增或编辑贴文")
    private Boolean editorialPost;
    /**
     * 是否允许群成员提醒他人
     */
    @ApiModelProperty("是否允许群成员提醒他人")
    private Boolean remindOthers;
    /**
     * 是否允许群成员群组通话
     */
    @ApiModelProperty("是否允许群成员群组通话")
    private Boolean groupCall;
    /**
     * 是否允许群成员成员私聊
     */
    @ApiModelProperty("是否允许群成员成员私聊")
    private Boolean privateMemberChat;
    /**
     * 全局禁言,不禁言
     */
    @ApiModelProperty("全局禁言,不禁言")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date silencedTo;
    /**
     * 群聊是否存在（true为存在，false为解散）
     */
    @ApiModelProperty("群聊是否存在（true为存在，false为解散）")
    private Boolean isLive=true;
    /**
     * 创建群聊的时间
     */
    @ApiModelProperty("创建群聊的时间")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;

}

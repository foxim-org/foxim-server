package com.hnzz.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnzz.commons.base.enums.userenums.ContactStatus;
import com.hnzz.dto.UserDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhoujun
 */
@Data
@Document(collection = "contacts")
@ApiModel("好友关系信息表")
public class Contacts implements Serializable {
    @Id
    @ApiModelProperty("关系ID")
    private String id;
    @ApiModelProperty("本人用户id")
    private String userId;
    @ApiModelProperty("好友的用户id")
    private String contactId;
    @ApiModelProperty("好友名称")
    private String friendName;
    @ApiModelProperty("好友标签")
    private String tags;
    @ApiModelProperty("好友备注")
    private String remark;
    @ApiModelProperty("好友头像")
    private String head;
    @ApiModelProperty("好友关系状态：PENDING（等待） | ACCEPTED（接受） | REJECTED（拒绝） | BLOCKED（拉黑）")
    private String status;
    @ApiModelProperty("聊天背景")
    private String bgUrl;
    @ApiModelProperty("聊天置顶")
    private Boolean isSticky;
    @ApiModelProperty("消息免打扰")
    private Boolean isMuted;
    @ApiModelProperty("聊天记录保存时间")
    private String empty;
    @ApiModelProperty("加入时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;
    @ApiModelProperty("最后发消息的时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date recentAt;
    @ApiModelProperty("最后一条消息内容")
    private String lastText;
    @ApiModelProperty("最近转发时间")
    private Date lastForwardTime;
    @ApiModelProperty("消息删除时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date delTime;
    @ApiModelProperty("语音图标")
    private String audio;
    @ApiModelProperty("语音时长")
    private String audioTime;
    @ApiModelProperty("播放图标默认为false")
    private boolean playing = false;
    @ApiModelProperty("宽度")
    private Integer width;
    @ApiModelProperty("视频链接")
    private String videoUrl;
    @ApiModelProperty("文档链接")
    private String documentUrl;
    @ApiModelProperty("图片链接")
    private String imgUrl;


    public Contacts() {
        super();
    }
    public Contacts(String userId , User friend , ContactStatus status){
        this.setUserId(userId);
        this.setContactId(friend.getId());
        this.setRemark(friend.getUsername());
        this.setFriendName(friend.getUsername());
        this.setCreatedAt(new Date());
        this.setHead(friend.getAvatarUrl());
        this.setStatus(status.getCode());
    }
    public Contacts(String userId, UserDTO userDTO, ContactStatus status){
        this.setUserId(userId);
        this.setContactId(userDTO.getId());
        this.setRemark(userDTO.getRemark());
        this.setFriendName(userDTO.getUsername());
        this.setCreatedAt(new Date());
        this.setHead(userDTO.getAvatarUrl());
        this.setStatus(status.getCode());
    }
}

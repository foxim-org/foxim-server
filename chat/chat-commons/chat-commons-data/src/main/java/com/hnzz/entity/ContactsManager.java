package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @PackageName:com.hnzz.dto
 * @ClassName:ContactsManager
 * @Author zj
 * @Date 2023/4/6 15:17
 * @Version 1.0
 **/
@Data
@ApiModel("单聊管理展示页面")
@Document(collection = "contactsManager")
public class ContactsManager implements Serializable {
    @Id
    @ApiModelProperty("关系ID")
    private String id;
    @ApiModelProperty("本人用户id")
    private String userId;
    @ApiModelProperty("好友的用户id")
    private String contactId;
    @ApiModelProperty("好友名称")
    private String name;
    @ApiModelProperty("好友标签")
    private String tags;
    @ApiModelProperty("好友备注")
    private boolean remark;
    @ApiModelProperty("好友头像")
    private String head;
    @ApiModelProperty("好友关系状态：PENDING（等待） | ACCEPTED（接受） | REJECTED（拒绝） | BLOCKED（拉黑）")
    private String status;
    @ApiModelProperty("聊天背景")
    private String bgUrl;
    @ApiModelProperty("聊天置顶")
    private boolean isSticky = false;
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
    @ApiModelProperty("消息删除时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date delTime;
    @ApiModelProperty("消息免打扰")
    private boolean isMuted = false;
    @ApiModelProperty("投诉举报")
    private boolean complaint;
}

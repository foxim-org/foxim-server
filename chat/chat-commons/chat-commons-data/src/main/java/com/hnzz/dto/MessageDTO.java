package com.hnzz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author HB on 2023/3/8
 * TODO 消息展示界面参数
 */
@Data
public class MessageDTO {
    @Id
    @ApiModelProperty("聊天信息Id")
    private String id;
    @ApiModelProperty("会话id")
    private String conversationId;
    @ApiModelProperty("订阅路径")
    private String topic;
    @ApiModelProperty("消息发送者Id")
    private String userId;
    @ApiModelProperty("消息发送者用户名")
    private String username;
    @ApiModelProperty("消息内容")
    private String text;
    @ApiModelProperty("消息生成时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;
    @ApiModelProperty("用于清空聊天")
    private String messageTTL;
    @ApiModelProperty("消息群Id")
    private String groupId;
    @ApiModelProperty("消息群名称")
    private String groupName;
    @ApiModelProperty("消息类型")
    private String type;

    @ApiModelProperty("语音图标")
    private String audio;
    @ApiModelProperty("语音时长")
    private String audioTime;
    @ApiModelProperty("播放图标默认为false")
    private Boolean playing = false;
    @ApiModelProperty("宽度")
    private Integer width;
}

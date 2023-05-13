package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author HB on 2023/1/30
 * TODO 私聊消息实体类
 */
@Data
@Document("private-messages")
@ApiModel("私聊关系表")
public class PrivateMessage implements Comparable<PrivateMessage>{
    @Id
    @ApiModelProperty("聊天信息Id")
    private String id;
    @ApiModelProperty("会话id")
    private String conversationId;
    @ApiModelProperty("订阅路径")
    private String topic;
    @ApiModelProperty("消息发送者Id")
    private String userId;
    @ApiModelProperty("消息内容")
    private String text;
    @ApiModelProperty("消息Id")
    private String msgId;
    @ApiModelProperty("消息生成时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;
    @ApiModelProperty("聊天消息状态")
    private Integer msgStatus;
    @ApiModelProperty("消息接收者Id")
    private String contactId;
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
    @ApiModelProperty("文件名")
    private String fileName;

    @Override
    public int compareTo(PrivateMessage o) {
        return this.createdAt.compareTo(o.createdAt);
    }

    public static String createConversationId(String userId,String contactId){
        int i = userId.compareTo(contactId);
        StringBuilder sb = new StringBuilder();
        if (i > 0) {
            sb.append(contactId).append(userId);
        } else {
            sb.append(userId).append(contactId);
        }
        return sb.toString();
    }
}

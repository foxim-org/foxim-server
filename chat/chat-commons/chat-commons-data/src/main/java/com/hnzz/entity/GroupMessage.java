package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @PackageName:com.hnzz.entity
 * @ClassName:GroupMessage
 * @Author 冼大丰
 * @Date 2023/1/31 16:26
 * @Version 1.0
 **/
@Data
@Document("group-messages")
@ApiModel("群聊表")
public class GroupMessage implements Comparable<GroupMessage>{
    @Id
    @ApiModelProperty("聊天信息Id")
    private String id;
    @ApiModelProperty("消息发送者Id")
    private String userId;
    @ApiModelProperty("消息群Id")
    private String groupId;
    @ApiModelProperty("群用户名")
    private String username;
    @ApiModelProperty("群名称")
    private String groupName;
    @ApiModelProperty("群头像")
    private String groupHead;
    @ApiModelProperty("消息内容")
    private String text;
    @ApiModelProperty("消息生成时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;
    @ApiModelProperty("用于清空聊天")
    private String messageTTL;
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
    @ApiModelProperty("图片链接")
    private String imgUrl;
    @ApiModelProperty("文档链接")
    private String documentUrl;
    @ApiModelProperty("文件名")
    private String fileName;

    @Override
    public int compareTo(GroupMessage o) {
        return this.createdAt.compareTo(o.createdAt);
    }
}

package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @PackageName:com.hnzz.entity
 * @ClassName:Favs
 * @Author zj
 * @Date 2023/3/29 14:44
 * @Version 1.0
 **/
@Data
@Document(collection = "favs")
@ApiModel("收藏信息表")
public class Favs {
    @Id
    @ApiModelProperty("收藏信息Id")
    private String id;
    @Field
    @ApiModelProperty("消息发送者Id")
    private String userId;
    @Field
    @ApiModelProperty("好友的用户id")
    private String contactId;
    @Field
    @ApiModelProperty("消息群Id")
    private String groupId;
    @Field
    @ApiModelProperty("消息内容")
    private String text;
    @Field
    @ApiModelProperty("图片")
    private String videoUrls;
    @Field
    @ApiModelProperty("链接")
    private String link;
    @Field
    @ApiModelProperty("消息生成时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;
    @Field
    @ApiModelProperty("语音图标")
    private String audio;
    @Field
    @ApiModelProperty("语音时长")
    private String audioTime;
    @Field
    @ApiModelProperty("播放图标默认为false")
    private boolean playing = false;
    @Field
    @ApiModelProperty("宽度")
    private Integer width;
}

package com.hnzz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnzz.entity.PrivateMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.hnzz.dto
 * @ClassName:PrivateMessageDTO
 * @Author 冼大丰
 * @Date 2023/3/10 16:28
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("私聊历史记录展示界面")
public class PrivateMessageDTO {
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
    @ApiModelProperty("聊天消息状态")
    private Integer msgStatus;
    @ApiModelProperty("消息生成时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;
    @ApiModelProperty("消息接收者Id")
    private String contactId;
    @ApiModelProperty("用户头像")
    private String avatarUrl;
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

}

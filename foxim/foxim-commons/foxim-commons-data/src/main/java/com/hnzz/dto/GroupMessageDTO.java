package com.hnzz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @PackageName:com.hnzz.dto
 * @ClassName:GroupMessageDTO
 * @Author 冼大丰
 * @Date 2023/1/31 16:57
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("群聊的历史记录")
public class GroupMessageDTO implements Comparable<GroupMessageDTO>{
    @Id
    @ApiModelProperty("聊天信息Id")
    private String id;
    @ApiModelProperty("订阅路径")
    private String topic;
    @ApiModelProperty("消息发送者Id")
    private String userId;
    @ApiModelProperty("消息内容")
    private String text;
    @ApiModelProperty("消息生成时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;
    @ApiModelProperty("消息群Id")
    private String groupId;
    @ApiModelProperty("昵称")
    private String username;
    @ApiModelProperty("头像")
    private String avatarUrl;
    @ApiModelProperty("语音图标")
    private String audio;
    @ApiModelProperty("语音时长")
    private String audioTime;
    @ApiModelProperty("播放图标默认为false")
    private Boolean playing;
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
    public int compareTo(GroupMessageDTO o) {
        return this.createdAt.compareTo(o.createdAt);
    }
}

package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @PackageName:com.hnzz.entity
 * @ClassName:Message
 * @Author 冼大丰
 * @Date 2023/5/13 12:47
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document("private-messages")
public class Message implements Serializable {
    @Id
    @ApiModelProperty("聊天信息Id")
    private String id;
    @ApiModelProperty("会话id")
    private String $type;
    @ApiModelProperty("订阅路径")
    private String msgStatus;
    @ApiModelProperty("消息发送者Id")
    private String userId;
    @ApiModelProperty("消息发送者Id")
    private String contactId;
    @ApiModelProperty("消息Id")
    private String msgId;
    @ApiModelProperty("消息生成时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;
    @ApiModelProperty("消息生成时间")
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date updatedAt;

}

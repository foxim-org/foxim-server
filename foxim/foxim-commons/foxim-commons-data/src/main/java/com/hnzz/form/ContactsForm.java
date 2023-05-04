package com.hnzz.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author HB on 2023/4/11
 * TODO
 */
@Data
public class ContactsForm {
    @NotBlank
    @ApiModelProperty("好友的用户id")
    private String contactId;
    @ApiModelProperty("好友标签")
    private String tags;
    @ApiModelProperty("好友备注")
    private String remark;
    @ApiModelProperty("聊天背景")
    private String bgUrl;
    @ApiModelProperty("聊天置顶")
    private Boolean isSticky;
    @ApiModelProperty("消息免打扰")
    private Boolean isMuted;
    @ApiModelProperty("聊天记录保存时间")
    private String empty;
}

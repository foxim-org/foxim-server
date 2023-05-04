package com.hnzz.form.bots;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author HB on 2023/2/22
 * TODO 机器人言论管理设置
 */
@Data
@ApiModel("机器人言论管理设置")
public class BotsSpeechManageSetting {

    @ApiModelProperty("机器人id (必填)")
    @NotBlank
    private String id;

    @ApiModelProperty("禁止发链接")
    private Boolean noLink;

    @ApiModelProperty("禁止发送包含二维码的图片")
    private Boolean noImageContainQRCode;
}

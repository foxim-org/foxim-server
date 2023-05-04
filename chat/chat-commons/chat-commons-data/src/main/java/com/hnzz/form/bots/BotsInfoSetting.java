package com.hnzz.form.bots;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author HB on 2023/2/22
 * TODO 机器人基本信息
 */
@Data
@ApiModel("机器人基本信息设置")
public class BotsInfoSetting {
    @ApiModelProperty("机器人id (必填)")
    @NotBlank
    private String id;

    @ApiModelProperty(value = "是否启用机器人 (默认为false)",dataType = "Boolean")
    private Boolean isEnable;

    @ApiModelProperty("机器人名称,默认(群名+机器人)")
    private String username;

    @ApiModelProperty("机器人头像,默认群头像")
    private String avatarUrl;
}

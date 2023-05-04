package com.hnzz.form.bots;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author HB on 2023/2/22
 * TODO 加入群聊机器人欢迎词设置
 */
@Data
@ApiModel("机器人入群欢迎设置")
public class BotsJoinGroupSetting {

    @ApiModelProperty("机器人id (必填)")
    @NotBlank
    private String id;

    @ApiModelProperty("是否开启入群欢迎 (默认为false)")
    private Boolean isWelcome;

    @ApiModelProperty("欢迎语句")
    private String welcomeText;
}

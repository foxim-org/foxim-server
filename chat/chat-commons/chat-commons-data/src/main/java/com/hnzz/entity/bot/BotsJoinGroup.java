package com.hnzz.entity.bot;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author HB on 2023/2/22
 * TODO 加入群聊机器人欢迎词设置
 */
@Data
public class BotsJoinGroup {

    private Boolean isWelcome;

    private String welcomeText;

    public BotsJoinGroup() {
        this.isWelcome = Boolean.FALSE;
        this.welcomeText = null;
    }

    public BotsJoinGroup(Boolean isWelcome, String welcomeText) {
        this.isWelcome = isWelcome;
        this.welcomeText = welcomeText;
    }
}

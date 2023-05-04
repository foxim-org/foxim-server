package com.hnzz.entity.bot;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author HB on 2023/2/22
 * TODO 机器人自动问答设置
 */
@Data
public class BotsAutoReply {

    private String replyId;

    private String question;

    private String answer;

    private String keyWord;

}

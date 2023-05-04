package com.hnzz.form.bots;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author HB on 2023/2/22
 * TODO 机器人发送定时消息设置
 */
@Data
@ApiModel("机器人发送定时消息设置")
public class BotsTimedMessageSetting{

    @ApiModelProperty("机器人id (必填)")
    @NotBlank
    private String id;

    @ApiModelProperty("消息类型: 普通消息 | 通话预约 (必填)")
    @NotBlank(message = "请选择消息类型")
    private String messageType;

    @ApiModelProperty("消息主题,不可超过500字 (必填)")
    @Length(max = 500 , message = "发送消息不可超过五百字")
    @NotBlank(message = "请输入消息主题")
    private String messageContext;

    @ApiModelProperty("消息包含的图片地址 , 先自行上传后再把地址放入")
    private String imgUrl;

    @ApiModelProperty("发送时间 (界面上是提醒时间, 必填)")
    @NotNull(message = "请输入开始时间")
    private Date reminderTime;

    @ApiModelProperty("重复次数 , 如果不重复为 0")
    private int repeatNum;

    @ApiModelProperty("开始前提醒 (在通话预约界面使用)")
    private Date reminderBeforeStart;


}

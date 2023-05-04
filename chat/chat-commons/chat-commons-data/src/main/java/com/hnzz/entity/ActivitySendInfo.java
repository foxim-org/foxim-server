package com.hnzz.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author HB on 2023/2/2
 * TODO 消息发送所携带的信息
 */
@Data
@ApiModel("消息推送中携带的信息")
public class ActivitySendInfo {
    @ApiModelProperty("消息路径")
    private String topic;
    @ApiModelProperty("消息体")
    private Map<String,String> payload;
    @ApiModelProperty("消息参数设置")
    private ActivitiesOptions options;

}

package com.hnzz.form;

import com.hnzz.commons.base.enums.activity.ActivitiesTopicEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author HB on 2023/2/2
 * TODO
 */
@Data
@ApiModel("消息推送接口参数表")
public class ActivitiesForm {
    @ApiModelProperty("消息推送路径: 只能输入 private私聊路径 或 group群聊路径")
    @NotNull(message = "订阅推送路径不能为空")
    private ActivitiesTopicEnum topic;

    @ApiModelProperty("消息体,以Map格式存储")
    @NotNull(message = "消息体不能为空")
    private Map<String,String> payload;

}

package com.hnzz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author HB on 2023/4/11
 * TODO
 */
@Data
@ApiModel("转发对象")
@Accessors(chain = true)
public class ForwardDTO {
    @ApiModelProperty("转发对象id")
    private String id;
    @ApiModelProperty("转发对象类型 个人 | 群聊")
    private String forwardTo;
    @ApiModelProperty("转发对象名称")
    private String remark;
    @ApiModelProperty("转发对象头像")
    private String avatarUrl;
    @ApiModelProperty("转发时间")
    private Date lastForwardTime;
}

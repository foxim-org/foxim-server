package com.hnzz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author HB on 2023/4/21
 * TODO 群信息在搜索栏的展示
 */
@Data
@ApiModel("群信息在搜索栏的展示")
public class GroupSearchInfo {
    @ApiModelProperty("群id")
    private String id;

    @ApiModelProperty("群狐狸号")
    private Integer foxCode;

    @ApiModelProperty("群名称")
    private String name;

    @ApiModelProperty("群头像")
    private String groupHead;

    @ApiModelProperty("是否是该群成员")
    private Boolean isGroupUser;

    @ApiModelProperty("@ApiModelProperty(申请加入群聊的状态PENDING（等待验证） | ACCEPTED（已加入群聊）| INEXISTENCE(不存在该群))")
    private String isJoin;
}

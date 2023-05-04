package com.hnzz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("存在好友关系时查看用户")
public class UserAllowviewproDTO extends UserDTO{
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("用户状态")
    private String statusText;
    @ApiModelProperty("头像")
    private String avatarUrl;
    @ApiModelProperty("用户存在好友状态")
    private String status;

}

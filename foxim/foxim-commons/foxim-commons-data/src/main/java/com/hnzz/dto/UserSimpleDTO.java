package com.hnzz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("加为好友前能看到的用户信息表")
public class UserSimpleDTO extends UserDTO {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("头像链接")
    private String avatarUrl;
    @ApiModelProperty("用户存在好友状态")
    private String status;
}

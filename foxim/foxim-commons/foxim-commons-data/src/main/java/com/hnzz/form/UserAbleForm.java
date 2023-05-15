package com.hnzz.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HB on 2023/3/7
 * TODO
 */
@Data
@ApiModel("管理员修改用户登录状态")
public class UserAbleForm {
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("是否禁用")
    private Boolean isDisabled;
}

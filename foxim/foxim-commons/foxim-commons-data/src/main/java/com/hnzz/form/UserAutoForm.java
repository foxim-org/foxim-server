package com.hnzz.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HB on 2023/3/7
 * TODO
 */
@Data
@ApiModel("管理员修改自动添加状态")
public class UserAutoForm {
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("是否自动添加")
    private boolean autoAdd;
}

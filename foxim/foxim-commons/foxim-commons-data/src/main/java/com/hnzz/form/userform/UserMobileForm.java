package com.hnzz.form.userform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @author HB on 2023/3/3
 * TODO 用户手机号修改表单
 */
@Data
@ApiModel("用户手机号修改表单")
public class UserMobileForm {

    @ApiModelProperty("修改后的手机号")
    @Pattern(regexp = "^1[3456789][0-9]{9}$", message = "手机号格式有误")
    private String mobile;
    @ApiModelProperty("请求修改手机号所得到的验证码 (暂时不需要)")
    private String checkCode;
}

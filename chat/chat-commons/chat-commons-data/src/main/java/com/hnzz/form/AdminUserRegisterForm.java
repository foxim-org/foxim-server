package com.hnzz.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author HB on 2023/3/2
 * TODO 管理员注册表单
 */
@Data
@ApiModel("管理员注册表单信息")
public class AdminUserRegisterForm {
    @ApiModelProperty("用户名")
    @NotBlank(message = "请输入你的用户名")
    private String username;

    @ApiModelProperty("用户手机号")
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号只能为11位")
    @Pattern(regexp = "^1[3456789][0-9]{9}$", message = "手机号格式有误")
    private String mobile;

    @ApiModelProperty("用户密码")
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 20, message = "密码至少8个字符，最长20个字符")
    private String password;

    @NotBlank(message = "平台名称不能为空")
    @ApiModelProperty("平台名称")
    private String platformName;
}

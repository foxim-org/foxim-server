package com.hnzz.form.userform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author HB on 2023/5/5
 * TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户注册信息表")
public class RegisterForm {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户手机号")
    private String mobile;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户密码")
    private String password;

    @NotBlank(message = "必须选择注册方式")
    @ApiModelProperty(value = "注册方式:  手机号+密码 | 用户名+密码", allowableValues = "MOBILE_PASSWORD,USERNAME_PASSWORD,EMAIL_PASSWORD")
    private String registerType;

}

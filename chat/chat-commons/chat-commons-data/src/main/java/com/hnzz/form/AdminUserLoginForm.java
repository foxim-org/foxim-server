package com.hnzz.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author HB on 2023/3/2
 * TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("管理员登录信息表")
public class AdminUserLoginForm {
    @ApiModelProperty("用户登录凭证,如手机号、用户名")
    @NotBlank(message = "用户名或手机号不能为空")
    private String account;

    @ApiModelProperty("用户密码")
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 20, message = "密码至少8个字符，最长20个字符")
    private String password;
}

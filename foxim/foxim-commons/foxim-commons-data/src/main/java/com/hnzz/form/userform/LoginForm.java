package com.hnzz.form.userform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


/**
 * @author HB
 * @Classname LoginForm
 * @Date 2023/1/4 15:00
 * @Description TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户登录信息表")
public class LoginForm {
    @ApiModelProperty("用户登录凭证,如手机号、狐狸号")
    @NotBlank(message = "狐狸号或手机号不能为空")
    private String account;
    @ApiModelProperty("用户密码")
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 20, message = "密码至少8个字符，最长20个字符")
    private String password;
}

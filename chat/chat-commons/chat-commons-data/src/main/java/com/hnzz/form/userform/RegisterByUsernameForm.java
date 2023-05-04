package com.hnzz.form.userform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author HB on 2023/4/27
 * TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户注册信息表")
public class RegisterByUsernameForm {
    @ApiModelProperty("用户名")
    @NotBlank(message = "请输入你的用户名")
    @Pattern(regexp = "[a-zA-Z0-9\\u4e00-\\u9fa5]+")
    @Length(min = 1,max = 15,message = "用户名长度必须为1-15个字符")
    private String username;

    @ApiModelProperty("用户密码")
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 20, message = "密码至少8个字符，最长20个字符")
    private String password;
}

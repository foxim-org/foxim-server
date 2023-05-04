package com.hnzz.form.userform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author HB
 * @Classname UserPwdForm
 * @Date 2023/1/4 16:39
 * @Description TODO
 */
@Data
@ApiModel("用户修改密码信息表")
public class UserPwdForm {

    @ApiModelProperty("用户原有密码")
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 20, message = "密码至少8个字符，最长20个字符")
    private String oldPwd;

    @ApiModelProperty("用户新的密码")
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 20, message = "密码至少8个字符，最长20个字符")
    private String newPwd;
}

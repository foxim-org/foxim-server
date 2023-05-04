package com.hnzz.form.userform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author HB
 * @Classname UserInformationForm
 * @Date 2023/1/5 11:57
 * @Description TODO 接收用户信息表单
 */
@Data
@ApiModel("用户信息表")
public class UserInfoForm {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("个人简介或签名")
    private String bio;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("生日")
    private Date birth;

    @ApiModelProperty("所在地")
    private String region;

}

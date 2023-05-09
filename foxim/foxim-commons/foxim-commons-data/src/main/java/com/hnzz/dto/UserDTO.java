package com.hnzz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author HB
 * @Classname UserDTO
 * @Date 2023/1/4 15:48
 * @Description TODO
 */
@Data
@ApiModel("用户信息表")
public class
UserDTO implements Comparable<UserDTO>{
    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("狐狸号")
    private Integer foxCode;

    @ApiModelProperty("好友备注")
    private String remark;

    @ApiModelProperty("好友名称的拼音")
    private String pinyinUserName;

    @ApiModelProperty("状态")
    private String statusText;

    @ApiModelProperty("默认头像")
    private String moRenUrl;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("VIP等级通过经验值动态计算显示")
    private Integer exp;

    @ApiModelProperty("createdAt")
    private Date createdAt;

    @ApiModelProperty("禁止登录")
    private Boolean isDisabled;

    @ApiModelProperty("个人简介或签名")
    private String bio;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("生日")
    private LocalDate birth;

    @ApiModelProperty("所在地")
    private String region;

    @ApiModelProperty("token")
    private String token;

    @Override
    public int compareTo(@NotNull UserDTO o) {
        String thisName = this.getPinyinUserName().toLowerCase();
        String oName = o.getPinyinUserName().toLowerCase();
        if (Character.isDigit(thisName.charAt(0)) && !Character.isDigit(oName.charAt(0))) {
            return 1;
        } else if (!Character.isDigit(thisName.charAt(0)) && Character.isDigit(oName.charAt(0))) {
            return -1;
        } else {
            return thisName.compareTo(oName);
        }
    }
}

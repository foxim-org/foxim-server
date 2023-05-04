package com.hnzz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author HB
 * @Classname User
 * @Date 2023/1/4 10:54
 * @Description TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document("User")
@EqualsAndHashCode
public class User {
    @Id
    private String id;
    @Field
    private String username;
    @Field
    private String mobile;
    @Field
    private String password;
    /**
     * 狐狸号
     */
    @Field
    private Integer foxCode;
    /**
     * 用户状态
     */
    @Field
    private String statusText;
    /**
     * 头像
     */
    @Field
    private String avatarUrl;
    /**
     *  VIP等级通过经验值动态计算显示
     */
    @Field
    private Integer exp;
    @Field
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date createdAt;
    /**
     * 禁止登录
     */
    @Field
    private Boolean isDisabled;
    /**
     * 用户签名或简介
     */
    @Field
    private String bio;
    @Field
    private String sex;
    @Field
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date birth;
    /**
     * 用户所在地区
     */
    @Field
    private String region;
    /**
     * 用户邀请码
     */
    private String inviteCode;
    /**
     * 注册ip
     */
    private String createIp;
    /**
     * 最后登录ip
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    @JsonFormat(timezone = "Asia/Shanghai")
    private Date lastLoginAt;

    private Boolean autoAdd;

}

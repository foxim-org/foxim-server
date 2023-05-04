package com.hnzz.entity;

import com.hnzz.commons.base.enums.AdminUserStatus;
import com.hnzz.commons.base.enums.AdminRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author HB on 2023/3/2
 * TODO 管理员登录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document("adminUser")
@EqualsAndHashCode
public class AdminUser {
    @Id
    private String id;
    @Field
    private String username;
    @Field
    private String mobile;
    @Field
    private String password;
    /**
     * 平台id
     */
    @Field
    private String platformId;
    /**
     * 头像
     */
    @Field
    private String avatarUrl;
    /**
     * 权限
     */
    @Field
    private AdminRoleEnum role;
    /**
     * 状态
     */
    @Field
    private AdminUserStatus status;
    /**
     * 注册时间
     */
    private Date createdAt;
}

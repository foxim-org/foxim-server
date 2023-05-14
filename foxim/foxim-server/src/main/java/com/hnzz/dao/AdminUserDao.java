package com.hnzz.dao;

import com.hnzz.entity.*;
import com.hnzz.form.UserAbleForm;
import com.hnzz.form.UserAutoForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * @author HB on 2023/3/2
 * TODO
 */
public interface AdminUserDao {

    /**
     * 管理员注册界面
     *
     * @param adminUser 传入一个半成品admin
     * @param platform
     * @return 返回存入后的值
     */
    AdminUser register(AdminUser adminUser, Platform platform);

    /**
     * 查找 用户名或手机号为xxx的管理员
     * @param searchInfo 传入查询条件 用户名|手机号|id
     * @return 返回一个查询到的用户
     */
    AdminUser findAdmin(String searchInfo);

    AdminUser findAdminByMobile(String mobile);

    /**
     * 根据平台名查找有多少管理员
     */
    AdminUser findAdminFromPlatformId(String platformName);

    Platform findPlatform(String userId);

    Page<PrivateMessage> getMessageListToPrivate(Pageable pageable, LocalDate start, LocalDate end, String search);

    Page<GroupMessage> getMessageListToGroup(Pageable pageable, LocalDate start, LocalDate end, String search);

    boolean setUserPwd(String account, String password);

    AdminUser addAdmin(AdminUser adminUser);

    User setUserAble(UserAbleForm form);

    AdminUser findAdminById(String adminId);

    User setUserAutoAdd(UserAutoForm form);
}

package com.hnzz.service;

import com.hnzz.dto.MessageList;
import com.hnzz.dto.UserDTO;
import com.hnzz.entity.AdminUser;
import com.hnzz.entity.Platform;
import com.hnzz.form.AdminUserLoginForm;
import com.hnzz.form.UserAbleForm;
import com.hnzz.form.UserAutoForm;
import org.springframework.data.domain.Page;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

/**
 * @author HB on 2023/3/2
 * TODO
 */

public interface AdminUserService {
    /**
     * @param adminUser 注册信息
     * @param platform
     */
    void register(AdminUser adminUser, Platform platform);

    /**
     * @param form 用户登录信息
     * @return 返回jwt
     */
    String login(AdminUserLoginForm form);

    /**
     * 返回邀请链接
     * @param userId
     * @return
     */
    String getInviteLink(String userId) throws UnsupportedEncodingException;
    /**
     * 查询管理员是否存在
     * @param userId
     * @return
     */
    AdminUser findById(String userId);

    MessageList getMessageList(Integer pageNum, Integer pageSize, LocalDate start, LocalDate end, String type, String search);

    boolean setUserPwd(String account, String password);

    AdminUser addAdmin(AdminUser adminUser);

    UserDTO setUserAble(UserAbleForm form);

    UserDTO setUserAutoAdd(UserAutoForm form);
}

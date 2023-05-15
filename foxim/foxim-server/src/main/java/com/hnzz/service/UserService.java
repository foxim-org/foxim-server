package com.hnzz.service;


import com.hnzz.commons.base.enums.userenums.UserStatusText;
import com.hnzz.commons.base.log.Log;
import com.hnzz.dto.TimelineDto;
import com.hnzz.dto.UserDTO;
import com.hnzz.entity.Contacts;
import com.hnzz.entity.Timeline;
import com.hnzz.entity.User;
import com.hnzz.form.Timelinefrom.ReplyFrom;
import com.hnzz.form.userform.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author HB
 * @Classname UserService
 * @Date 2023/1/4 11:48
 * @Description TODO
 */
public interface UserService {
    /**
     * 注册
     * @param form 注册表单对象
     */
    UserDTO register(RegisterValidForm form);
    /**
     * 登录
     *
     * @param form 登录表单对象
     */
    String login(LoginForm form , String ipAddress);

    /**
     * 修改基本信息
     *
     * @param user 传入用户信息
     * @return
     */
    UserDTO setUserInfo(User user);
    /**
     * 修改密码
     */
    void setUserPwd(UserPwdForm form, String userId);
    /**
     * 修改用户状态
     */
    void setUserStatus(String userId, UserStatusText status);
    /**
     * 修改用户等级
     */
    void setUserExp(String userId);
    /**
     * 查询用户信息
     */
    UserDTO findUserById(String id);

    /**
     * 批量查询用户信息
     * @param userIds 用户id集合
     * @return 用户集合
     */
    List<UserDTO> getUsersById(List<String> userIds);

    @Log("根据id集合查询多个用户信息业务层")
    List<UserDTO> getMyFriends(List<Contacts> contactsList);

    /**
     * @author 后续接口由ZhouJun实现
     * @param id
     * @return
     */
    User friendAllowviewpro(String id);

    User friendSimple(String id);

    User selectById(String search);


    Timeline saveTimelines(SendTimelineFrom sendTimelineFrom, String userId);


    Timeline saveTimelines(Timeline timeline);

    void removeTimelinesById(String userId, String timelineId);

    List<Timeline> getTimeline(List<String> friendsInfo);

    Timeline getTimelineById(String timelineId);

    ResponseEntity<String> star(ReplyFrom replyfrom, String userId);

    Timeline updateTimelines(Timeline timelines);

    List<TimelineDto> findTimelineByUserId(String friendsId);

    List<User> findUserByInviteLink(String id);

    UserDTO setUserMobile(String userId,UserMobileForm form);

    Page<User> getUserList(Integer pageNum, Integer pageSize, String search);

    UserDTO setUserAvatarUrl(String userId, MultipartFile file);

    boolean userLogout(String id);

    Page<User> userAll(Integer pageNum, Integer pageSize);

    Boolean formUserByMobile(String mobile);

    String loginMobile(String mobile, String code ,String ipAddress);

    UserDTO registerMobile(RegisterMobile form , String ipAddress);

    void setUserMobilePwd(String userId, String password);

    Object deleteUser(String id);

    List<User> lookOnLine();
}

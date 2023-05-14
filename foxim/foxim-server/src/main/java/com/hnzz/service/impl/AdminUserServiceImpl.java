package com.hnzz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.hnzz.commons.base.enums.AdminUserStatus;
import com.hnzz.commons.base.enums.AdminRoleEnum;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.commons.base.jwt.JWTHelper;
import com.hnzz.dao.AdminUserDao;
import com.hnzz.dto.MessageDTO;
import com.hnzz.dto.MessageList;
import com.hnzz.dto.UserDTO;
import com.hnzz.entity.*;
import com.hnzz.form.AdminUserLoginForm;
import com.hnzz.form.UserAbleForm;
import com.hnzz.form.UserAutoForm;
import com.hnzz.service.AdminUserService;
import com.hnzz.service.GroupService;
import com.hnzz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HB on 2023/3/2
 * TODO 管理员业务层
 */
@Service
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserDao adminUserDao;
    @Resource
    private JWTHelper jwtHelper;
    @Resource
    private UserService userService;

    @Resource
    private GroupService groupService;

    @Value("${app.inviteUrl}")
    private String inviteUrl;

    @Override
    public void register(AdminUser adminUser, Platform platform){
        adminUser.setRole(AdminRoleEnum.PLATFORM_SUPER_ADMIN);
        adminUser.setStatus(AdminUserStatus.APPROVED);
        adminUser.setCreatedAt(new Date());
        adminUser.setPassword(DigestUtil.sha256Hex(adminUser.getPassword()));
        adminUserDao.register(adminUser,platform);
    }

    @Override
    public String login(AdminUserLoginForm form) {
        AdminUser admin = adminUserDao.findAdmin(form.getAccount());
        String pwd = DigestUtil.sha256Hex(form.getPassword());
        if (!Objects.equals(admin.getPassword(),pwd)){
            throw new AppException("输入密码有误 , 无法登录");
        }
        if (Objects.equals(admin.getStatus(),AdminUserStatus.DISABLED)){
            throw new AppException("账号处于封禁状态 , 无法登录");
        }

        Map<String,Object> map = new HashMap<>(10);
        map.put("id",admin.getId());
        map.put("role",admin.getRole());
        return jwtHelper.createJWT(map);
    }

    @Override
    public String getInviteLink(String adminId) {
        AdminUser user = adminUserDao.findAdminById(adminId);
        if (user==null){
            throw new AppException("该管理员不存在");
        }
        log.info("查询到的管理员信息:{}", user);
        return inviteUrl + user.getPlatformId();
    }

    @Override
    public AdminUser findById(String userId) {
        return adminUserDao.findAdmin(userId);
    }

    @Override
    public MessageList getMessageList(Integer pageNum, Integer pageSize, LocalDate start, LocalDate end, String type, String search) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        ArrayList<MessageDTO> messageDTOS = new ArrayList<>();
        MessageList messageList = new MessageList();
        if ("private".equals(type)) {
            Page<PrivateMessage> page = adminUserDao.getMessageListToPrivate(pageable, start, end, search);
            messageList.setPage(page);
            List<PrivateMessage> privateMessages = page.getContent();
            List<String> userIds =privateMessages.stream().map(PrivateMessage::getUserId).collect(Collectors.toList());
            List<UserDTO> users = userService.getUsersById(userIds);
            for (String userId : userIds) {
                PrivateMessage privateMessage = privateMessages.stream().filter(p -> p.getUserId().equals(userId)).findFirst().orElse(null);
                UserDTO user = users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
                if (privateMessage!=null && user!=null){
                    MessageDTO messageDTO = BeanUtil.copyProperties(privateMessage, MessageDTO.class);
                    messageDTO.setUsername(user.getUsername());
                    messageDTO.setType("私聊消息");
                    messageDTOS.add(messageDTO);
                }
            }
        }else if ("group".equals(type)) {
            Page<GroupMessage> page = adminUserDao.getMessageListToGroup(pageable, start, end, search);
            messageList.setPage(page);
            List<GroupMessage> groupMessages = page.getContent();
            List<String> groupIds = groupMessages.stream().map(GroupMessage::getGroupId).collect(Collectors.toList());
            List<Group> groups = groupService.getGroupByIds(groupIds);
            for (String groupId : groupIds) {
                GroupMessage groupMessage = groupMessages.stream().filter(g -> g.getGroupId().equals(groupId)).findFirst().orElse(null);
                Group group = groups.stream().filter(g -> g.getId().equals(groupId)).findFirst().orElse(null);
                if (groupMessage!=null && group!=null){
                    MessageDTO messageDTO = BeanUtil.copyProperties(groupMessage, MessageDTO.class);
                    messageDTO.setUsername(group.getName());
                    messageDTO.setType("群聊消息");
                    messageDTOS.add(messageDTO);
                }
            }
        }
        messageList.setMessageDTOS(messageDTOS);
        return messageList;
    }

    @Override
    public boolean setUserPwd(String account, String password) {
        return adminUserDao.setUserPwd(account, password);
    }

    @Override
    public AdminUser addAdmin(AdminUser adminUser) {
        adminUser.setRole(AdminRoleEnum.PLATFORM_ADMIN);
        adminUser.setStatus(AdminUserStatus.APPROVED);
        adminUser.setCreatedAt(new Date());
        adminUser.setPassword(DigestUtil.sha256Hex(adminUser.getPassword()));
        return adminUserDao.addAdmin(adminUser);
    }

    @Override
    public UserDTO setUserAble(UserAbleForm form) {
        User user = adminUserDao.setUserAble(form);
        return BeanUtil.copyProperties(user, UserDTO.class);
    }

    @Override
    public UserDTO setUserAutoAdd(UserAutoForm form) {
        User user = adminUserDao.setUserAutoAdd(form);
        return BeanUtil.copyProperties(user, UserDTO.class);
    }
}

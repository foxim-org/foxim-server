package com.hnzz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.hnzz.common.ExpLevel;
import com.hnzz.common.SeaweedFSUtil;
import com.hnzz.commons.base.enums.userenums.ContactStatus;
import com.hnzz.commons.base.enums.userenums.UserStatusText;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.commons.base.jwt.JWTHelper;
import com.hnzz.commons.base.log.Log;
import com.hnzz.commons.base.util.PinYinUtil;
import com.hnzz.dao.UserDao;
import com.hnzz.dto.TimelineDto;
import com.hnzz.dto.UserDTO;
import com.hnzz.entity.*;
import com.hnzz.form.IdsPattern;
import com.hnzz.form.Timelinefrom.ReplyFrom;
import com.hnzz.form.userform.*;
import com.hnzz.service.IdsService;
import com.hnzz.service.PrivateMessageService;
import com.hnzz.service.SmsService;
import com.hnzz.service.UserService;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HB
 * @Classname UserServiceImpl
 * @Date 2023/1/4 11:49
 * @Description TODO
 */
@Slf4j
@Service
@RefreshScope
public class UserServiceImpl implements UserService {
    @Resource
    private MongoTemplate template;
    @Value("${user.exp}")
    private Integer exp;
    @Value("${user.tokenExpiry}")
    private Long tokenExpiry;
    @Value("${app.uploadUrl}")
    private String uploadUrl;
    @Resource
    private UserDao userDao;
    @Resource
    private IdsService idsService;
    @Resource
    private SmsService smsService;
    @Resource
    private JWTHelper jwtHelper;
    @Resource
    private PrivateMessageService privateMessageService;

    @Override
    @Log("用户注册业务层")
    public UserDTO register(RegisterForm form) {

        IdsPattern idsPattern = new IdsPattern();
        Integer idWithRandom = idsService.getIdWithRandom(idsPattern);
        User user = BeanUtil.copyProperties(form, User.class);
        user.setExp(ExpLevel.LEVEL_1)
                .setCreatedAt(new Date())
//                .setAvatarUrl(defaultAvatarUrl)
                .setStatusText(UserStatusText.LINE_ON.getCode())
                .setFoxCode(idWithRandom)
                .setIsDisabled(false)
                .setPassword(DigestUtil.sha256Hex(user.getPassword()));

        // 判断是否有邀请码
        Optional.ofNullable(form.getInviteCode()).ifPresent(v -> {
            boolean exists = template.exists(new Query(Criteria.where("id").is(v)), AdminUser.class);
            if (!exists) {
                throw new AppException("邀请码有误 , 注册失败");
            }
        });
        // 尝试注册
        try {
            user = template.insert(user);
            idsService.setIdsUsed(idWithRandom);
        } catch (DuplicateKeyException e) {
            throw new AppException("已存在该用户信息 , 不可重复注册");
        }
        if (user.getId() == null) {
            throw new AppException("用户注册失败");
        }
        Map<String, Object> map = BeanUtil.beanToMap(user, new HashMap<>(), false, true);
        String jwt = jwtHelper.createJWT(new Date(System.currentTimeMillis() + tokenExpiry), map);
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        userDTO.setToken(jwt);
        return userDTO;
    }

    @Override
    public UserDTO registerMobile(RegisterMobile form , String ipAddress) {
        IdsPattern idsPattern = new IdsPattern();
        Integer idWithRandom = idsService.getIdWithRandom(idsPattern);
        User user = BeanUtil.copyProperties(form, User.class);
        user.setExp(ExpLevel.LEVEL_1)
                .setCreatedAt(new Date())
                .setMobile(form.getMobile())
                .setStatusText(UserStatusText.LINE_ON.getCode())
                .setFoxCode(idWithRandom)
                .setIsDisabled(false)
                .setCreateIp(ipAddress)
                .setPassword(DigestUtil.sha256Hex(user.getPassword()));
        // 尝试注册
        try {
            user = template.insert(user);
            idsService.setIdsUsed(idWithRandom);
            // 查找自动注册的好友
            List<User> autoAddFriend = getAutoAddFriend();
            if (autoAddFriend!=null && !autoAddFriend.isEmpty()) {
                ArrayList<Contacts> contacts = new ArrayList<>();
                Date date = new Date();
                for (User friend : autoAddFriend) {
                    // 生成 好友和我的关系
                    Contacts withMe = new Contacts();
                    withMe.setUserId(user.getId());
                    withMe.setContactId(friend.getId());
                    withMe.setRemark(friend.getUsername());
                    withMe.setFriendName(friend.getUsername());
                    withMe.setCreatedAt(date);
                    withMe.setHead(friend.getAvatarUrl());
                    withMe.setStatus(ContactStatus.ACCEPTED.getCode());
                    contacts.add(withMe);
                    // 生成 我与好友的关系
                    Contacts withFriend = new Contacts();
                    withFriend.setUserId(friend.getId());
                    withFriend.setContactId(user.getId());
                    withFriend.setRemark(user.getUsername());
                    withFriend.setFriendName(user.getUsername());
                    withFriend.setCreatedAt(date);
                    withFriend.setHead(user.getAvatarUrl());
                    withFriend.setStatus(ContactStatus.ACCEPTED.getCode());
                    contacts.add(withFriend);
                }
                template.insert(contacts,Contacts.class);
            }
        } catch (DuplicateKeyException e) {
            throw new AppException("已存在该用户信息 , 不可重复注册");
        }
        if (user.getId() == null) {
            throw new AppException("用户注册失败");
        }
        Map<String, Object> map = BeanUtil.beanToMap(user, new HashMap<>(), false, true);
        String jwt = jwtHelper.createJWT(new Date(System.currentTimeMillis() + tokenExpiry), map);
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        userDTO.setToken(jwt);
        return userDTO;
    }

    public List<User> getAutoAddFriend() {
        return template.find(Query.query(Criteria.where("autoAdd").is(true)), User.class);
    }

    @Override
    @Log("用户登录业务层")
    public String login(LoginForm form , String ipAddress) {
        Criteria criteria = new Criteria();
        if (form.getAccount().length() == 5) {
            int i = Integer.parseInt(form.getAccount());
            criteria.orOperator(Criteria.where("foxCode").is(i));
        } else {
            criteria.orOperator(Criteria.where("mobile").is(form.getAccount()));
        }
        criteria.andOperator(Criteria.where("password").is(DigestUtil.sha256Hex(form.getPassword())));
        User user = template.findOne(new Query(criteria), User.class);
        if (user == null || user.getId() == null) {
            throw new AppException("用户信息输入有误");
        }
        UserDTO userDTO = setUserInfo(new User().setId(user.getId()).setLoginIp(ipAddress).setLastLoginAt(new Date()).setStatusText(UserStatusText.LINE_ON.getCode()));
        Map<String, Object> map = BeanUtil.beanToMap(userDTO, new HashMap<>(), false, true);
        return jwtHelper.createJWT(new Date(System.currentTimeMillis() + tokenExpiry), map);
    }

    @Override
    public String loginMobile(String mobile, String code , String ipAddress) {
        smsService.verifyVerificationCode(mobile, code);

        User user = template.findOne(new Query(Criteria.where("mobile").is(mobile)), User.class);
        if (user == null || user.getId() == null) {
            throw new AppException("没有该用户或该手机号没有注册!");
        }
        UserDTO userDTO = setUserInfo(new User().setId(user.getId()).setLoginIp(ipAddress).setLastLoginAt(new Date()).setStatusText(UserStatusText.LINE_ON.getCode()));
        Map<String, Object> map = BeanUtil.beanToMap(userDTO, new HashMap<>(), false, true);
        return jwtHelper.createJWT(new Date(System.currentTimeMillis() + tokenExpiry), map);
    }

    @Override
    @Log("修改用户基本信息业务层")
    public UserDTO setUserInfo(User user) {
        User newUser = userDao.setUserInfo(user);
        if (newUser == null) {
            throw new AppException("用户不存在");
        }
        return BeanUtil.copyProperties(newUser, UserDTO.class);
    }

    @Override
    @Log("修改用户密码业务层")
    public void setUserPwd(UserPwdForm form, String userId) {
        User user = template.findById(userId, User.class);
        if (user == null) {
            throw new AppException("该用户id不存在");
        }
        String oldPwd = DigestUtil.sha256Hex(form.getOldPwd());
        if (!user.getPassword().equals(oldPwd)) {
            throw new AppException("该用户输入的原有密码有误");
        }
        template.save(user.setPassword(DigestUtil.sha256Hex(form.getNewPwd())), "user");
    }

    @Override
    public void setUserMobilePwd(String userId, String password) {
        User byId = template.findById(new ObjectId(userId), User.class);
        if (byId == null) {
            throw new AppException("该用户id不存在");
        }
        log.info("Setting user mobile password is {}",byId);
        byId.setPassword(DigestUtil.sha256Hex(password));
        template.save(byId);
        log.info("Setting user mobile password is {}",byId);
    }

    @Override
    @Log("修改用户状态业务层")
    public void setUserStatus(String userId, UserStatusText status) {
        User user = template.findById(userId,User.class);
        if (user == null) {
            throw new AppException("不存在该用户id： " + userId);
        }
        user.setStatusText(status.getCode());
        Update update = new Update();
        update.set("statusText", status.getCode());
        template.upsert(Query.query(Criteria.where("_id").is(userId)),update,User.class);
    }

    @Override
    @Log("修改用户经验")
    public void setUserExp(String userId) {
        Query query = Query.query(Criteria.where("_id").is(userId));
        User user = template.findById(userId, User.class);
        if (user == null) {
            log.error("该用户id查询不到:==={}", userId);
            throw new AppException("查询不到该用户: " + userId);
        }
        user.setExp(user.getExp() + exp);
        User save = template.save(user);
        if (user.equals(save)) {
            log.error("用户信息修改失败");
            throw new AppException("用户信息修改失败");
        }
    }

    @Override
    @Log("根据token查询用户信息业务层")
    public UserDTO findUserById(String id) {
        User user = template.findById(id, User.class);
        UserDTO userDTO = Optional.ofNullable(user).isPresent() ? BeanUtil.copyProperties(user, UserDTO.class) : null;
        if (userDTO != null) {
            userDTO.setRemark(user.getUsername());
        }
        return userDTO;
    }

    @Override
    @Log("根据id集合查询多个用户信息业务层")
    public List<UserDTO> getUsersById(List<String> userIds) {
        if (userIds == null) {
            return null;
        }
        Query query = new Query(Criteria.where("id").in(userIds)).with(Sort.by(Sort.Direction.ASC, "userName"));

        List<User> users = template.find(query, User.class);

        List<UserDTO> userDTOS = BeanUtil.copyToList(users, UserDTO.class);

        userDTOS.forEach(userDTO -> {
            String remark = userDTO.getRemark(), s = "";
            if (remark != null) {
                s = PinYinUtil.toPinyin(remark);
            } else {
                s = PinYinUtil.toPinyin(userDTO.getUsername());
            }
            userDTO.setPinyinUserName(s);
        });

        return userDTOS;
    }
    @Override
    @Log("获取我的好友信息")
    public List<UserDTO> getMyFriends(List<Contacts> contactsList) {
        List<String> friendIds = contactsList.stream().map(Contacts::getContactId).collect(Collectors.toList());

        List<User> users = template.find(Query.query(Criteria.where("id").in(friendIds)), User.class);
        List<UserDTO> userDTOS = BeanUtil.copyToList(users, UserDTO.class);

        for (UserDTO userDTO : userDTOS) {
            for (Contacts contacts : contactsList){
                if (contacts.getContactId().equals(userDTO.getId())){
                    if (contacts.getRemark()!=null){
                        userDTO.setUsername(contacts.getRemark());
                    }
                    contactsList.remove(contacts);
                    break;
                }
            }
            String s = PinYinUtil.toPinyin(userDTO.getUsername());
            userDTO.setPinyinUserName(s);
        }
        return userDTOS;
    }

    @Override
    public User friendAllowviewpro(String id) {
        return userDao.friendAllowviewpro(id);
    }

    @Override
    public User friendSimple(String id) {
        return userDao.friendSimple(id);
    }

    @Override
    public User selectById(String search) {
        return userDao.selectBySearch(search);
    }

    @Override
    @Log("发送朋友圈")
    public Timeline saveTimelines(SendTimelineFrom sendTimelineFrom, String userId) {
        List<Reply> replyList = new ArrayList<>();

        Timeline timeline = new Timeline()
                .setReplies(replyList)
                .setUserId(userId)
                .setCreatedAt(new Date())
                .setText(sendTimelineFrom.getText())
                .setImageUrls(sendTimelineFrom.getImageUrls())
                .setVideoUrls(sendTimelineFrom.getVideoUrls());

        return template.save(timeline);
    }

    @Override
    @Log("转发时发送朋友圈")
    public Timeline saveTimelines(Timeline timeline) {
        return template.save(timeline);
    }

    @Override
    @Log("删除朋友圈")
    public void removeTimelinesById(String userId, String timelineId) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("id").is(timelineId), Criteria.where("userId").is(userId));
        Query query = new Query(criteria);
        template.findAllAndRemove(query, Timeline.class);
    }

    @Override
    @Log("获取朋友圈并倒序")
    public List<Timeline> getTimeline(List<String> friendsInfo) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("userId").in(friendsInfo));
        Query query = new Query(new Criteria().andOperator(criteria)).with(Sort.by(Sort.Direction.DESC, "createdAt"));
        return template.find(query, Timeline.class);
    }


    @Override
    @Log("获取单条朋友圈")
    public Timeline getTimelineById(String timelineId) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("id").is(timelineId));
        Query query = new Query(criteria);
        return template.findOne(query, Timeline.class);
    }

    @Override
    @Log("点赞朋友圈")
    public ResponseEntity<String> star(ReplyFrom replyfrom, String userId) {
        //查询用户头像
        User user = userDao.findUserById(userId);
        //查询昵称备注，暂时没用
        Timeline timelineById = getTimelineById(replyfrom.getTimelineId());

        List<Reply> replies = timelineById.getReplies();

        //判断这条点赞是否存在，如果存在就删除
        if (!replies.isEmpty()) {
            for (int i = 0; i < replies.size(); i++) {
                if ((replies.get(i).getAuthorId() == null && replies.get(i).getReplyText() == null) && (userId.equals(replies.get(i).getUserId()))) {
                    replies.remove(replies.get(i));

                    Timeline timeline = timelineById.setReplies(replies);

                    updateTimelines(timeline);

                    return ResponseEntity.ok("取消点赞成功");
                }
            }
        }

        Reply starReply = new Reply()
                .setId(new ObjectId().toString())
                .setUserId(userId)
                .setUsername(replyfrom.getUsername())
                .setAvatarUrl(user.getAvatarUrl());

        replies.add(starReply);

        Timeline timeline = timelineById.setReplies(replies);

        updateTimelines(timeline);
        return ResponseEntity.ok("点赞成功");
    }

    @Override
    @Log("修改朋友圈")
    public Timeline updateTimelines(Timeline timelines) {
        return template.save(timelines);
    }

    @Override
    @Log("查看某人的朋友圈")
    public List<TimelineDto> findTimelineByUserId(String friendsId) {
        Query query = new Query(Criteria.where("userId").is(friendsId)).with(Sort.by(Sort.Direction.ASC, "createdAt"));
        List<Timeline> timelines = template.find(query, Timeline.class);
        List<TimelineDto> timelineDtos = new ArrayList<>();

        for (Timeline timeline : timelines) {
            TimelineDto timelineDto = BeanUtil.copyProperties(timeline, TimelineDto.class);
            timelineDtos.add(timelineDto);
        }
        return timelineDtos;
    }

    @Override
    public List<User> findUserByInviteLink(String id) {
        Query query = new Query(Criteria.where("inviteCode").is(id));
        return template.find(query, User.class);
    }

    @Override
    public UserDTO setUserMobile(String userId, UserMobileForm form) {

        boolean mobileExists = template.exists(new Query(Criteria.where("mobile").is(form.getMobile())), User.class);
        boolean idExists = template.exists(new Query(Criteria.where("id").is(userId)), User.class);
        if (!mobileExists && idExists) {
            Update update = new Update().set("mobile", form.getMobile());
            FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
            User user = template.findAndModify(new Query(Criteria.where("id").is(userId)), update, options, User.class);
            return BeanUtil.copyProperties(user, UserDTO.class);
        } else if (!idExists) {
            throw new AppException("用户不存在 , 无法修改手机号");
        } else {
            throw new AppException("该手机号已注册 , 不可用于修改");
        }
    }

    @Override
    public Page<User> getUserList(Integer pageNum, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return userDao.findAll(pageable, search);
    }

    @Override
    public UserDTO setUserAvatarUrl(String userId, MultipartFile file) {
        ResponseEntity<FileInfo> response = SeaweedFSUtil.uploadFile(uploadUrl, file);
        FileInfo body = response.getBody();
        if (body == null) {
            throw new AppException("头像上传失败");
        }
        User user = userDao.setUserAvatarUrl(userId, body.getFileUrl());
        return BeanUtil.copyProperties(user, UserDTO.class);
    }

    @Override
    public boolean userLogout(String id) {
        return userDao.userLogout(id);
    }

    @Override
    public Page<User> userAll(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return userDao.userAll(pageable);
    }

    @Override
    public Boolean formUserById(String mobile) {
        return userDao.formUserByMobile(mobile);
    }

}

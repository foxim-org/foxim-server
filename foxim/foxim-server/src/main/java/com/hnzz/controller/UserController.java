package com.hnzz.controller;


import cn.hutool.core.bean.BeanUtil;
import com.hnzz.common.ResultUtil;
import com.hnzz.commons.base.enums.system.SettingEnum;
import com.hnzz.commons.base.enums.system.UserValidTypeEnum;
import com.hnzz.commons.base.enums.userenums.ContactStatus;
import com.hnzz.commons.base.enums.userenums.UserStatusText;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.commons.base.jwt.JWTHelper;
import com.hnzz.commons.base.result.Result;
import com.hnzz.dto.*;
import com.hnzz.entity.Contacts;
import com.hnzz.entity.User;
import com.hnzz.entity.system.UserRegisterSetting;
import com.hnzz.form.userform.*;
import com.hnzz.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author HB and 周俊
 * @Classname UserController
 * @Date 2023/1/4 11:16
 * @Description TODO
 */
@Slf4j
@RestController
@RequestMapping("api/v1/user/")
@Api(tags = "用户管理接口")
public class UserController  {

    @Resource
    private UserService userService;
    @Resource
    private JWTHelper jwtHelper;
    @Resource
    private ContactsService contactsService;
    @Resource
    private GroupService groupService;
    @Resource
    private SettingService settingService;


    @GetMapping("/AboutWith")
    @ApiOperation("查看“关于我们")
    public ResponseEntity AboutWith(@RequestHeader("ipAddr")String ipAddr){
        if (ipAddr==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ip不能为空");
        }
        return ResponseEntity.ok(
                settingService.findAboutWith()
        );
    }

    @PostMapping("register")
    @ApiOperation("用户注册")
    public ResponseEntity register(@RequestHeader("ipAddr")String ipAddr , @RequestBody @Valid RegisterForm form){
        UserRegisterSetting userRegisterSetting = (UserRegisterSetting) settingService.getSetting(SettingEnum.REGISTER_SETTING.name()).getBody();
        if (userRegisterSetting!=null){
            boolean canRegister = userRegisterSetting.canRegister(form.getRegisterType());
            if (canRegister){
                RegisterMobile registerMobile = BeanUtil.copyProperties(form, RegisterMobile.class);
                if (form.getRegisterType().equals(UserValidTypeEnum.USERNAME_PASSWORD.name())){
                    return ResponseEntity.ok(userService.registerMobile(registerMobile , ipAddr));
                }else if(form.getRegisterType().equals(UserValidTypeEnum.MOBILE_PASSWORD.name())){
                    Boolean exits = userService.formUserByMobile(form.getMobile());
                    if (exits){
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("该手机号已被被注册,请重新输入手机号注册!");
                    }
                    return ResponseEntity.ok(userService.registerMobile(registerMobile , ipAddr));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("暂时无法通过"+form.getRegisterType()+"方法注册");
    }

    @GetMapping("/setting/get/{name}")
    @ApiImplicitParam(name = "key", value = "配置key", paramType = "path" , allowableValues = "REGISTER_SETTING , LOGIN_SETTING , WITH_USER_SETTING")
    public ResponseEntity getSetting(@RequestHeader("ipAddr")String ipAddr,@PathVariable("name") String name){
        if (ipAddr==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ip不能为空");
        }
        return settingService.getSet(name);
    }

    @PostMapping("registerMobile")
    @ApiOperation("用户手机号验证码注册接口")
    public ResponseEntity registerMobile(@RequestHeader("ipAddr")String ipAddr , @RequestBody @Valid RegisterMobile form){
        Boolean exits = userService.formUserByMobile(form.getMobile());
        if (exits){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("该手机号已被被注册,请重新输入手机号注册!");
        }
        return ResponseEntity.ok(userService.registerMobile(form , ipAddr));
    }

    @PostMapping("login")
    @ApiOperation("用户登录")
    public ResponseEntity login(@RequestHeader("ipAddr")String ipAddr, @RequestBody @Valid LoginForm form){
        String login = userService.login(form , ipAddr);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/loginMobile")
    @ApiOperation("用户手机号验证码登录接口")
    public ResponseEntity<String> loginMobile(@RequestHeader("ipAddr")String ipAddr , @RequestParam String mobile, @RequestParam String code) {
        if (mobile==null || Objects.equals("",mobile)){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("该手机号还未被注册,请注册后在登录!");
        }

        String s = userService.loginMobile(mobile, code , ipAddr);
        return ResponseEntity.ok(s);
    }

    @PostMapping("setUserAvatarUrl")
    @ApiOperation(("修改用户头像"))
    public ResponseEntity<Object> setUserAvatarUrl(@RequestHeader("userId")String userId, @RequestParam("file") MultipartFile file){
        UserDTO userDTO = userService.setUserAvatarUrl(userId, file);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("setUserInfo")
    @ApiOperation("修改用户个人资料")
    public ResponseEntity<UserDTO> setUserInfo(@RequestHeader("userId")String userId, @RequestBody UserInfoForm form){

        User user = BeanUtil.copyProperties(form, User.class);
        user.setId(userId);
        UserDTO userDTO = userService.setUserInfo(user);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("checkToken")
    @ApiOperation("验证token是否过期及合法")
    public ResponseEntity checkToken(@RequestParam("token") String token){
        String userId = "";
        try {
            userId = jwtHelper.parseJWT(token).get("id",String.class);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token校验失败 , token不存在或已过期");
        }
        UserDTO userById = userService.findUserById(userId);
        if (userById==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("token所携带的用户信息有误 , 查询不到相关信息");
        }
        return ResponseEntity.ok(userById);
    }

    @GetMapping("findById")
    @ApiOperation("根据token获取用户信息")
    public ResponseEntity findById(@RequestHeader("userId") String userId){

        UserDTO userDTO = userService.findUserById(userId);
        return userDTO!=null?
                ResponseEntity.ok(userDTO):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("token所携带的用户信息有误 , 查询不到相关信息");
    }

    @PostMapping("setPwd")
    @ApiOperation("修改用户密码")
    public ResponseEntity<String> setUserPwd(@RequestHeader("userId") String userId,@RequestBody @Valid UserPwdForm form){
        userService.setUserPwd(form,userId);
        return ResponseEntity.ok("修改成功!");
    }

    @PostMapping("setUserMobilePwd")
    @ApiOperation("使用手机验证码修改用户密码")
    public ResponseEntity<String> setUserMobilePwd(@RequestHeader("userId") String userId,@RequestParam("password") String password){
        userService.setUserMobilePwd(userId,password);
        return ResponseEntity.ok("修改密码成功!");
    }

    @GetMapping("setStatus")
    @ApiOperation("修改用户状态 ")
    public ResponseEntity<String> setUserStatus(@RequestHeader("id") String userId,@RequestParam("status") String status){
        userService.setUserStatus(userId,UserStatusText.valueOf(status));
        return ResponseEntity.ok("修改成功!");
    }

    @PostMapping("setExp")
    @ApiOperation("修改用户经验，未完善")
    public ResponseEntity setUserExp(){
        return null;
    }


    @PostMapping("setMobile")
    @ApiOperation("修改用户手机号")
    public ResponseEntity<UserDTO> setUserMobile(@RequestHeader("userId") String userId,@RequestBody UserMobileForm form){
        UserDTO userDTO = userService.setUserMobile(userId,form);
        return ResponseEntity.ok(userDTO);
    }


    /**
     * 加好友后查看资料
     */
    @GetMapping("/full/{id}")
    @ApiOperation("加好友后查看资料")
    public ResponseEntity friendAllowviewpro(@PathVariable("id") String id) {
        User users = userService.friendAllowviewpro(id);
        if (users == null) {
            return ResultUtil.response(HttpStatus.NOT_FOUND,"没有查询到该用户");
        } else {
            return ResultUtil.resultToResponse(Result.success(users));
        }
    }

    /**
     * 好友前查看资料
     */
    @GetMapping("/simple/{id}")
    @ApiOperation("好友前查看资料")
    public ResponseEntity friendSimple(@PathVariable("id") String id) {
        User users = userService.friendSimple(id);
        if (users == null) {
            return ResultUtil.response(HttpStatus.NOT_FOUND,"没有查询到该用户");
        } else {
            return ResultUtil.resultToResponse(Result.success(users));
        }
    }

    @GetMapping("/search")
    @ApiOperation("查找联系人")
    public ResponseEntity selectById(@RequestHeader("userId") String userId,@RequestParam("userSearch") String userSearch)  {

        if (!userSearch.matches("\\d+")&&userSearch!=null&&!userSearch.isEmpty()) {
            // 如果不是数字，返回错误信息
            return ResponseEntity.badRequest().body("只能传入狐狸号或者手机号！");
        }

        // 判断查询的用户是否存在
        User friend = userService.selectById(userSearch);

        if (friend==null){
            return ResultUtil.response(HttpStatus.NOT_FOUND,"没有查询到该用户");
        }
        // 判断a和b是否存在好友关系
        Contacts andB = contactsService.selectContacts(userId,friend.getId());
        // 判断b和a是否存在好友关系
        Contacts andA = contactsService.selectContacts(friend.getId(),userId);

        if (andB!=null&&andA!=null&&andB.getStatus().equals(ContactStatus.ACCEPTED.getCode())&&andA.getStatus().equals(ContactStatus.ACCEPTED.getCode())){
            User user = userService.friendAllowviewpro(friend.getId());
            UserAllowviewproDTO userAllowviewproDTO = BeanUtil.copyProperties(user, UserAllowviewproDTO.class);
            userAllowviewproDTO.setStatus(ContactStatus.ACCEPTED .getCode());
            return ResultUtil.resultToResponse(Result.success(userAllowviewproDTO));
        }
        if (andB!=null&&andB.getStatus().equals(ContactStatus.PENDING.getCode())) {
            UserSimpleDTO userSimpleDTO = BeanUtil.copyProperties(friend, UserSimpleDTO.class);
            userSimpleDTO.setStatus(ContactStatus.PENDING.getCode());
            return ResultUtil.resultToResponse(Result.success(userSimpleDTO));
        }
        UserSimpleDTO userSimpleDTO = BeanUtil.copyProperties(friend, UserSimpleDTO.class);
        userSimpleDTO.setStatus(ContactStatus.NOTAFRIEND.getCode());
        return ResultUtil.resultToResponse(Result.success(userSimpleDTO));
    }


    /**
     * 首页搜索框查询好友
     */
    @GetMapping("/userList")
    @ApiOperation("首页搜索框")
    public ResponseEntity<Object> userList(@RequestHeader("userId") String userId,
                                           @RequestParam(value = "search") String search) {

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户Id不存在!");
        }
        if (search.trim().isEmpty()){
            throw new AppException("查询条件不能为空");
        }
        List<InformSearch> informSearches = new ArrayList<>();
        List<ContactsSearch> contactsSearch = contactsService.contactsList(search);
        for (ContactsSearch value : contactsSearch) {
            InformSearch informSearch = new InformSearch();
            informSearch.setUserId(value.getId());
            informSearch.setAvatarUrl(value.getAvatarUrl());
            informSearch.setUserName(value.getUsername());
            informSearches.add(informSearch);
        }
        List<GroupData> groupSearches = groupService.groupList(search);
        for (GroupData groupSearch : groupSearches) {
            InformSearch informSearch = new InformSearch();
            informSearch.setGroupId(groupSearch.getId());
            informSearch.setGroupHead(groupSearch.getGroupHead());
            informSearch.setGroupName(groupSearch.getName());
            informSearches.add(informSearch);
        }
        return ResponseEntity.ok(informSearches);
    }

    /**
     * 注销用户
     */
    @PostMapping("/userLogout/{id}")
    @ApiOperation("注销用户")
    public ResponseEntity userLogout(@PathVariable("id") String id) {
        boolean b = userService.userLogout(id);
        if (b){
            return  ResponseEntity.ok("注销成功!");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( "没有该用户或该用户已被注销!");
        }
    }

    @GetMapping("userAll")
    @ApiOperation("查询所有用户(附近的人)")
    public ResponseEntity<Object> userAll(@RequestHeader("userId") String userId,
                                              @RequestParam("pageNum")Integer pageNum,
                                              @RequestParam(value = "pageSize")Integer pageSize){
        UserDTO userById = userService.findUserById(userId);
        if (userById==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        if (pageNum<=0 || pageSize<=0){
            throw new AppException("查询页数必须大于零");
        }
        Page<User> userList = userService.userAll(pageNum-1,pageSize);
        return ResponseEntity.ok(userList);
    }

}

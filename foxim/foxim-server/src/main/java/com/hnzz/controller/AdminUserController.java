package com.hnzz.controller;

import cn.hutool.core.bean.BeanUtil;
import com.hnzz.common.ResultUtil;
import com.hnzz.commons.base.enums.AdminRoleEnum;
import com.hnzz.commons.base.enums.system.SettingEnum;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.dto.ContactsPage;
import com.hnzz.dto.GroupUserAdmin;
import com.hnzz.dto.MessageList;
import com.hnzz.dto.UserDTO;
import com.hnzz.entity.*;
import com.hnzz.entity.system.Setting;
import com.hnzz.form.AddAdminForm;
import com.hnzz.form.AdminUserLoginForm;
import com.hnzz.form.AdminUserRegisterForm;
import com.hnzz.form.UserAbleForm;
import com.hnzz.form.groupform.GroupId;
import com.hnzz.form.groupform.QuitGroup;
import com.hnzz.form.userform.LoginForm;
import com.hnzz.form.userform.RegisterForm;
import com.hnzz.form.userform.RegisterValidForm;
import com.hnzz.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HB on 2023/3/2
 * TODO 管理员界面相关接口
 */
@RestController
@RequestMapping("api/v1/admin")
@Api(tags = "管理员API")
public class  AdminUserController {
    @Resource
    private AdminUserService adminUserService;
    @Resource
    private UserService userService;
    @Resource
    private GroupService groupService;
    @Resource
    private GroupUserService groupUserService;
    @Resource
    private ContactsService contactsService;
    @Resource
    private SettingService settingService;

    @PostMapping("lookLogoAvatarUrl")
    @ApiOperation(("查看启动页Logo"))
    public ResponseEntity<Object> lookLogoAvatarUrl(){
        Setting setting = settingService.lookLogoAvatarUrl();
        return ResponseEntity.ok(setting);
    }

    @PostMapping("setLogoAvatarUrl")
    @ApiOperation(("修改启动页Logo"))
    public ResponseEntity<Object> setUserAvatarUrl(@RequestHeader("adminId")String userId, @RequestParam("file") MultipartFile file){
        AdminUser byId = adminUserService.findById(userId);
        if (byId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        settingService.saveLogoAvatarUrl(userId,file);
        return ResponseEntity.ok("上传成功");
    }


    @PostMapping("/setting/AboutWith")
    @ApiOperation("管理员编辑“关于我们")
    public ResponseEntity AboutWith(@RequestHeader("adminId")String userId,@RequestParam String value){
        AdminUser byId = adminUserService.findById(userId);
        if (byId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        return ResponseEntity.ok(
                settingService.saveAboutWith(value)
        );
    }

    @PostMapping("/setting/put/{name}")
    @ApiOperation("设置方式")
    public ResponseEntity putSetting(@PathVariable("name")String name, @RequestBody String value){

        return ResponseEntity.ok(
                settingService.saveSetting(name,value)
        );
    }

    @GetMapping("/setting/get/{name}")
    @ApiOperation("获取方式")
    @ApiImplicitParam(name = "key", value = "配置key", paramType = "path" , allowableValues = "REGISTER_SETTING , LOGIN_SETTING , WITH_USER_SETTING")
    public ResponseEntity getSetting(@PathVariable("name") String name){
        return settingService.getSetting(name);
    }


    @PostMapping("/kickOutGroup")
    @ApiOperation("管理员踢出群成员")
    public ResponseEntity<Object> kickOutGroup(@RequestHeader("adminId")String userId, @RequestBody QuitGroup quitGroup){
        AdminUser byId = adminUserService.findById(userId);
        if (byId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        Group groupById = groupService.getGroupById(quitGroup.getGid());
        if (groupById==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("该群不存在！");
        }

        GroupUsers groupUserByUserId = groupUserService.getGroupUserByUserId(quitGroup.getUserId(), quitGroup.getGid());

        if (groupUserByUserId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("该群成员不存在！");
        }

        groupUserService.removeGroupUserByUserId(quitGroup.getUserId(), quitGroup.getGid());

        return ResponseEntity.ok("成功踢出");

    }

    @GetMapping("/getGroupUser")
    @ApiOperation("管理员获取群成员")
    public ResponseEntity<Object> getGroupUser(@RequestHeader("adminId")String userId, @RequestParam String groupId){
        AdminUser byId = adminUserService.findById(userId);
        if (byId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        Group groupById = groupService.getGroupById(groupId);
        if (groupById==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("该群不存在！");
        }

        List<GroupUsers> groupUserByGroupId = groupUserService.getGroupUserByGroupId(groupId);

        List<GroupUserAdmin> groupUserList=new ArrayList<>();
        for (GroupUsers groupUsers:groupUserByGroupId) {
            UserDTO userById = userService.findUserById(groupUsers.getUserId());
            if (userById!=null){
                GroupUserAdmin adminGroupUser= BeanUtil.copyProperties(groupUsers,GroupUserAdmin.class);
                adminGroupUser.setAccounts(userById.getMobile());
                groupUserList.add(adminGroupUser);
            }
        }

        return ResponseEntity.ok(groupUserList);

    }

    @PostMapping("/removeGroup")
    @ApiOperation("管理员删除群聊")
    public ResponseEntity<Object> removeGroup(@RequestHeader("adminId")String userId, @RequestBody GroupId groupId){
        AdminUser byId = adminUserService.findById(userId);
        if (byId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        Group groupById = groupService.getGroupById(groupId.getGroupId());
        if (groupById==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("该群不存在！");
        }

        groupService.removeGroupById(groupId.getGroupId());

        return ResponseEntity.ok("删除成功");
    }

    @PostMapping("/updateGroup")
    @ApiOperation("管理员编辑单个群聊功能")
    public ResponseEntity<Object> updateGroup(@RequestHeader("adminId")String userId, @RequestBody Group newGroup){
        AdminUser byId = adminUserService.findById(userId);
        if (byId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        Group group = groupService.AdminUpdateGroup(newGroup);
        return ResponseEntity.ok(group);
    }

    @GetMapping("userInfo/{userId}")
    @ApiOperation("根据userId查询用户信息")
    public ResponseEntity<Object> getUserInfo(@RequestHeader("adminId")String adminId,@PathVariable("userId")String userId){
        AdminUser byId = adminUserService.findById(userId);
        if (byId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        UserDTO userById = userService.findUserById(userId);
        return ResponseEntity.ok(userById);
    }
    @GetMapping("/messageList")
    @ApiOperation("分页查询所有聊天记录")
    public ResponseEntity<Object> getMessageList(
            @ApiParam(value = "用户ID(携带token自动填入 , 不用管)") @RequestHeader("adminId") String userId,
            @ApiParam(value = "页码,必填") @RequestParam("pageNum") Integer pageNum,
            @ApiParam(value = "每页展示数量,必填") @RequestParam(value = "pageSize")Integer pageSize,
            @ApiParam(value = "起始日期") @RequestParam(value = "start", required = false) LocalDate start,
            @ApiParam(value = "截止日期") @RequestParam(value = "end", required = false) LocalDate end,
            @ApiParam(value = "消息类型 private|group 私聊和群聊") @RequestParam(value = "type") String type,
            @ApiParam(value = "搜索用户名或昵称") @RequestParam(value = "search",required = false)String search){
        AdminUser byId = adminUserService.findById(userId);
        if (byId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        if (pageNum<=0 || pageSize<=0){
            throw new AppException("查询页数必须大于零");
        }
        if (start!=null&&end!=null&&start.isAfter(end)){
            throw new AppException("起始时间必须小于结束时间");
        }
        MessageList messageList = adminUserService.getMessageList(pageNum-1, pageSize, start, end, type, search);
        return ResponseEntity.ok(messageList);
    }

    @GetMapping("/GroupList")
    @ApiOperation("查询所有群聊")
    public ResponseEntity<Object> getGroupList(@RequestHeader("adminId")String adminId,
                                               @RequestParam("pageNum") Integer pageNum,
                                               @RequestParam(value = "pageSize")Integer pageSize,
                                       @RequestParam(value = "search",required = false)String search){
        AdminUser byId = adminUserService.findById(adminId);
        if (byId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        if (pageNum<=0 || pageSize<=0){
            throw new AppException("查询页数必须大于零");
        }
        return ResponseEntity.ok(groupService.pageGroup(pageNum-1,pageSize,search));
    }

    @GetMapping("listUser")
    @ApiOperation("分页获取所有用户, search可以不填,如果第一次使用search , 需要num=0")
    public ResponseEntity<Object> getUserList(@RequestHeader("adminId") String adminId,
                                              @RequestParam("pageNum")Integer pageNum,
                                              @RequestParam(value = "pageSize")Integer pageSize,
                                              @RequestParam(value = "search",required = false)String search){
        AdminUser adminUser = adminUserService.findById(adminId);
        if (adminUser==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        if (pageNum<=0 || pageSize<=0){
            throw new AppException("查询页数必须大于零");
        }
        Page<User> userList = userService.getUserList(pageNum-1,pageSize,search);
        return ResponseEntity.ok(userList);
    }
    @PostMapping("setUserPwd")
    @ApiOperation("设置用户密码 , 表中account字段填待修改的userId")
    public ResponseEntity setUserPwd(@RequestHeader("adminId")String adminId,@Valid @RequestBody LoginForm form){
        AdminUser byId = adminUserService.findById(adminId);
        if (byId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        boolean b = adminUserService.setUserPwd(form.getAccount(), form.getPassword());
        return b?ResponseEntity.ok("修改成功"):ResponseEntity.status(HttpStatus.BAD_REQUEST).body("修改失败");
    }

    @PostMapping("setUserAble")
    @ApiOperation("设置用户是否封禁")
    public ResponseEntity<Object> setUserStatus(@RequestHeader("adminId") String adminId,@RequestBody UserAbleForm form){
        AdminUser byId = adminUserService.findById(adminId);
        if (byId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        UserDTO userDTO = adminUserService.setUserAble(form);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("y/register")
    @ApiOperation("新平台注册")
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid AdminUserRegisterForm form) {
        Platform platform = new Platform().createPlatform(form.getPlatformName(), null);
        AdminUser adminUser = BeanUtil.copyProperties(form, AdminUser.class);
        adminUserService.register(adminUser,platform);
        return ResponseEntity.ok("注册成功!");
    }

    @PostMapping("addAdmin")
    @ApiOperation("超管添加管理员")
    public ResponseEntity<Object> addAdmin(@RequestHeader("adminId")String adminId,@RequestBody @Valid AddAdminForm form) {
        AdminUser byId = adminUserService.findById(adminId);
        if (byId==null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("该管理员不存在");
        }
        if (!byId.getRole().equals(AdminRoleEnum.TERMINAL_SUPER_ADMIN) && !byId.getRole().equals(AdminRoleEnum.TERMINAL_SUPER_ADMIN)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("非超管不能添加管理员");
        }
        AdminUser adminUser = BeanUtil.copyProperties(form, AdminUser.class);
        adminUser.setPlatformId(byId.getPlatformId());
        AdminUser admin = adminUserService.addAdmin(adminUser);
        return ResponseEntity.ok(admin);
    }


    @PostMapping("y/login")
    @ApiOperation("管理员登录")
    public ResponseEntity<String> login(@RequestBody @Valid AdminUserLoginForm form){
        String token = adminUserService.login(form);
        return ResponseEntity.ok(token);
    }

    @GetMapping("adminInfo")
    @ApiOperation("管理员查看个人信息")
    public ResponseEntity<Object> getAdminInfo(@RequestHeader("adminId")String adminId){
        AdminUser byId = adminUserService.findById(adminId);
        return ResponseEntity.ok(byId);
    }

    @GetMapping("inviteLink")
    @ApiOperation("获取邀请链接")
    public ResponseEntity<String> getInviteLink(@RequestHeader("adminId") String adminId) throws UnsupportedEncodingException {
        String inviteLink = adminUserService.getInviteLink(adminId);
        return ResponseEntity.ok(inviteLink);
    }
    
    @GetMapping("lookUser")
    @ApiOperation("查看平台下所有用户")
    public ResponseEntity lookUser(@RequestHeader("adminId") String adminId){
        AdminUser byId = adminUserService.findById(adminId);

        if (byId==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该管理员不存在");
        }

        List<User> userList=userService.findUserByInviteLink(byId.getId());

        return ResponseEntity.ok(userList);
    }

    @GetMapping("lookGroup")
    @ApiOperation("查看平台下所有群")
    public ResponseEntity lookGroup(@RequestHeader("adminId") String adminId){
        AdminUser byId = adminUserService.findById(adminId);

        if (byId==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该管理员不存在");
        }

        List<User> userList=userService.findUserByInviteLink(byId.getId());

        List<String> ownerIds=new ArrayList<>();
          for (int i = 0; i < userList.size(); i++) {
            ownerIds.add(userList.get(i).getId());
        }

        List<Group> groups = groupService.getGroupByUserList(ownerIds);

        return ResponseEntity.ok(groups);
    }
    /**
     * 通讯录列表
     *
     * @return
     */
    @GetMapping("/addressList")
    @ApiOperation("通讯录列表")
    public ResponseEntity addressList(@RequestHeader("adminId") String adminId,
                                      @RequestParam("pageNum") Integer pageNum,
                                      @RequestParam(value = "pageSize")Integer pageSize,
                                      @RequestParam(value = "search",required = false) String search) {

        AdminUser adminUser = adminUserService.findById(adminId);
        if (adminUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无权访问该接口");
        }
        if (pageNum<=0 || pageSize<=0){
            throw new AppException("查询页数必须大于零");
        }
        ContactsPage contactsPage = contactsService.addressList(pageNum-1, pageSize, search);
        return ResponseEntity.ok(contactsPage);
    }

    @PostMapping("addNewUsers")
    @ApiOperation("新增用户")
    public ResponseEntity addNewUsers(@RequestHeader("adminId") String adminId, @RequestBody RegisterForm form) {
        RegisterValidForm registerValidForm = BeanUtil.copyProperties(form, RegisterValidForm.class);
        return ResponseEntity.ok(userService.register(registerValidForm));
    }
    @PostMapping("deleteUsers")
    @ApiOperation("删除用户")
    public ResponseEntity deleteUsers(@RequestHeader("adminId") String adminId, @RequestParam String userId) {
        UserDTO userById = userService.findUserById(userId);
        if (userById==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("该用户不存在");
        }
        return ResponseEntity.ok(userService.deleteUser(userById.getId()));
    }

    @PostMapping("updateUsers")
    @ApiOperation("修改用户")
    public ResponseEntity updateUsers(@RequestHeader("adminId") String adminId, @RequestBody User user) {
        return ResponseEntity.ok(userService.setUserInfo(user));
    }

}

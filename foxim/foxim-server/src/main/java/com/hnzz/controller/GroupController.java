package com.hnzz.controller;

import com.hnzz.common.ResultUtil;
import com.hnzz.commons.base.enums.activity.ActivitiesField;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.commons.base.result.Result;
import com.hnzz.dto.*;
import com.hnzz.entity.Activities;
import com.hnzz.entity.Contacts;
import com.hnzz.entity.Group;
import com.hnzz.entity.GroupUsers;

import com.hnzz.form.groupform.*;
import com.hnzz.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @PackageName:com.zzkj.controller
 * @ClassName:GroupController
 * @Author 冼大丰
 * @Date 2023/1/4 10:22
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/v1/groups")
@Slf4j
@Api(tags = "群管理接口")
public class GroupController {

    @Resource
    private GroupService groupService;

    @Resource
    private UserService userService;

    @Resource
    private GroupUserService groupUserService;

    @Resource
    private ActivitiesService activitiesService;

    @Resource
    private GroupMessageService groupMessageService;

    /**
     * 发送添加群申请
     *
     * @param
     * @return
     */
    @GetMapping("/addGroup/{groupId}")
    @ApiOperation("发送添加群申请")
    public ResponseEntity addContacts(@PathVariable("groupId")String groupId, @RequestHeader("userId") String userId) {
        // 判断群是否存在
        Group groupById = groupService.getGroupById(groupId);

        List<GroupUsers> groupUserByGroupId = groupUserService.getGroupUserByGroupId(groupId);

        List<GroupUsers> adminGroupUserByGroupId=new ArrayList<>();
        if (groupUserByGroupId != null) {
            for (int i = 0; i < groupUserByGroupId.size(); i++) {
                if (groupUserByGroupId.get(i).getIsAdmin()){
                    adminGroupUserByGroupId.add(groupUserByGroupId.get(i));
                }
            }
        }
        if (groupById == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( "该群不存在");
        }
        UserDTO userById = userService.findUserById(userId);
        String s = userById.getUsername() + "请求加入群聊";
        HashMap<String, String> payload = new HashMap<>(8);
        payload.put(ActivitiesField.GROUP_ID,groupId);
        payload.put(ActivitiesField.TEXT,s);
        payload.put(ActivitiesField.ACTIVITY_CREATEDAT,new Date ().toString());
        payload.put(ActivitiesField.TYPE,"addGroup");
        for (int i = 0; i < adminGroupUserByGroupId.size(); i++) {
            String topic = Activities.topic("private",adminGroupUserByGroupId.get(i).getUserId(), "addGroup");
            Activities activities = new Activities(topic,payload);
            try {
                activities = activitiesService.saveActivities(activities);
            } catch (IOException e) {
                log.warn("编号为{}的消息发送失败",activities.getId());
                throw new AppException("加入群发送失败!");
            }
        }
        return ResponseEntity.ok("加入群发送成功！");
    }



    @GetMapping("/searchGroupInfo")
    @ApiOperation("根据群名称或群狐狸号搜索该群")
        public ResponseEntity<List<GroupSearchInfo>> searchGroupInfo(@RequestHeader("userId")String userId,@RequestParam("search")String search){
        List<GroupSearchInfo> search1 = groupService.search(search, userId);
        return ResponseEntity.ok(search1);
    }

    @PostMapping("/setGroupAvatarUrl")
    @ApiOperation(("修改群聊头像"))
    public ResponseEntity setUserAvatarUrl(@RequestHeader("userId")String userId,@RequestParam("groupId")String groupId,@RequestParam("file") MultipartFile file){

        GroupUsers groupUserByUserId = groupUserService.getGroupUserByUserId(userId, groupId);

        if (!groupUserByUserId.getIsAdmin()||groupUserByUserId==null) {
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该用户不存在或没有该权限");
        }
        Group group = groupService.setGroupAvatarUrl(groupId, file);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/groupInvite")
    @ApiOperation("生成群聊链接")
    public ResponseEntity groupInvite(@RequestHeader("userId")String userId,@RequestBody GroupId groupId){

        Group groupById = groupService.getGroupById(groupId.getGroupId());

        if (groupById==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该群已解散，请重试！！");
        }

        GroupUsers groupUserByUserId = groupUserService.getGroupUserByUserId(userId, groupId.getGroupId());

        if (groupUserByUserId==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该用户不在该群或该群已解散，请重试！！");
        }

        String jwt = groupService.saveGroupInvite(userId,groupId.getGroupId());

        return ResultUtil.resultToResponse(Result.success(jwt));
    }

    @PostMapping("/ViewGroupSettings")
    @ApiOperation("群聊设置页面展示")
    public ResponseEntity viewGroupSettings(@RequestHeader("userId")String userId,@RequestBody GroupId groupId){

        Group groupById = groupService.getGroupById(groupId.getGroupId());

        if (groupById==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该群已解散，请重试！！");
        }

        //获取群成员资料
        GroupUsers groupUserByUserId = groupUserService.getGroupUserByUserId(userId, groupId.getGroupId());

        if (groupUserByUserId==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该用户不在该群或该群已解散，请重试！！");
        }


        GroupSetupDto groupSetupDto=new GroupSetupDto()
                .setRemarksName(groupUserByUserId.getRemarksName())
                .setGroupName(groupById.getName())
                .setBackGround(groupById.getBackGround())
                .setUsername(groupUserByUserId.getUsername())
                .setIsSticky(groupUserByUserId.getIsSticky())
                .setIsMuted(groupUserByUserId.getIsMuted())
                .setNotice(groupById.getNotice());
        return ResultUtil.resultToResponse(Result.success(groupSetupDto));
    }


    @GetMapping("/notDisturb/{groupId}")
    @ApiOperation("群免打扰和取消群免打扰")
    public ResponseEntity<String> notDisturb(@RequestHeader("userId")String userId,@PathVariable("groupId") String groupId){
        //获取群成员资料
        GroupUsers groupById = groupUserService.getGroupUserByUserId(userId, groupId);

        if (groupById ==null){
            return ResultUtil.response(HttpStatus.NOT_FOUND,"该用户不在该群或该群已解散，请重试！！");
        }

        groupUserService.saveGroupUser(groupById);
        if (!groupById.getIsMuted()){
            groupById.setIsMuted(true);
            groupUserService.saveGroupUser(groupById);
            return ResponseEntity.ok("免打扰设置成功!");
        }else {
            groupById.setIsMuted(false);
            groupUserService.saveGroupUser(groupById);
            return ResponseEntity.ok("取消免打扰成功!");
        }
    }


    @GetMapping("/groupTop/{groupId}")
    @ApiOperation("群置顶和取消群置顶")
    public ResponseEntity groupTop(@RequestHeader("userId")String userId,@PathVariable("groupId") String groupId){

        //获取群成员资料
        GroupUsers groupById = groupUserService.getGroupUserByUserId(userId, groupId);

        if (groupById==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该用户不在该群或该群已解散，请重试！！");
        }

        if (!groupById.getIsSticky()){
            groupById.setIsSticky(true);
            groupUserService.saveGroupUser(groupById);
            return ResponseEntity.ok("设置置顶成功!");
        }else {
            groupById.setIsSticky(false);
            groupUserService.saveGroupUser(groupById);
            return ResponseEntity.ok("取消置顶成功!");
        }
    }

    @PostMapping("/silentAll")
    @ApiOperation("设置全局禁言,解除全局禁言")
    public ResponseEntity silentAll(@RequestHeader("userId")String userId,@RequestParam GroupId groupId){
        Group groupById = groupService.getGroupById(groupId.getGroupId());
        GroupUsers groupUserByUserId = groupUserService.getGroupUserByUserId(userId, groupId.getGroupId());
        if (!userId.equals(groupById.getOwnerId())&&groupUserByUserId.getIsAdmin()==false){
            throw new AppException("您没有权限执行此操作！");
        }
        if (groupById.getIsSilencedToAll()==false){
            groupById.setIsSilencedToAll(true);
            groupService.save(groupById);
            return ResponseEntity.ok("设置全局禁言成功!");
        }else {
            groupById.setIsSilencedToAll(false);
            groupService.save(groupById);
            return ResponseEntity.ok("解除全局禁言成功!");
        }
    }


    @PostMapping("/silent")
    @ApiOperation("设置禁言")
    public ResponseEntity silent(@RequestHeader("userId")String userId,@RequestParam String groupId,@RequestParam String toId,@RequestParam Date date){
        Group groupById = groupService.getGroupById(groupId);
        GroupUsers groupUserByUserId = groupUserService.getGroupUserByUserId(userId, groupId);
        if (!userId.equals(groupById.getOwnerId())&&groupUserByUserId.getIsAdmin()==false){
            throw new AppException("您没有权限执行此操作！");
        }
        GroupUsers groupUserByToId = groupUserService.getGroupUserByUserId(toId, groupId);
        if (groupUserByToId==null){
            throw new AppException("该用户不在群内！");
        }
        groupUserByToId.setSilencedTo(date);
        groupUserByToId.setIsSilencedTo(true);
        groupUserService.saveGroupUser(groupUserByToId);
        return ResponseEntity.ok("操作成功");
    }

    @PostMapping("/notSilent")
    @ApiOperation("解除禁言")
    public ResponseEntity notSilent(@RequestHeader("userId")String userId,@RequestParam String groupId,@RequestParam String toId) {
        Group groupById = groupService.getGroupById(groupId);
        GroupUsers groupUserByUserId = groupUserService.getGroupUserByUserId(userId, groupId);
        if (!userId.equals(groupById.getOwnerId()) && groupUserByUserId.getIsAdmin() == false) {
            throw new AppException("您没有权限执行此操作！");
        }
        GroupUsers groupUserByToId = groupUserService.getGroupUserByUserId(toId, groupId);
        if (groupUserByToId == null) {
            throw new AppException("该用户不在群内！");
        }
        Date date = new Date();
        if (date.after(groupUserByToId.getSilencedTo())) {
            throw new AppException("还在禁言时间内！");
        } else if (date.before(groupUserByToId.getSilencedTo())) {
            groupUserByToId.setSilencedTo(null);
            groupUserByToId.setIsSilencedTo(false);
        }else {
            groupUserByToId.setSilencedTo(null);
            groupUserByToId.setIsSilencedTo(false);
        }
        groupUserService.saveGroupUser(groupUserByToId);
        return ResponseEntity.ok("操作成功");
    }

    @PostMapping("/viewSilent")
    @ApiOperation("查看群禁言列表")
    public ResponseEntity viewSilent(@RequestHeader("userId")String userId,@RequestBody GroupId groupId){


        if (groupId==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"传入群聊Id不能为空");
        }

        GroupUsers groupUserByUserId = groupUserService.getGroupUserByUserId(userId, groupId.getGroupId());

        if (!groupUserByUserId.getIsAdmin()){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该用户没有该权限");
        }

        List<GroupUsers> groupUsersList = groupUserService.getGroupUserByGroupId(groupId.getGroupId());

        List<GroupUsers> groupUsersListone=new ArrayList<>();
        for (GroupUsers groupUsers : groupUsersList) {
            Date silencedTo = groupUsers.getSilencedTo();
            if (silencedTo.compareTo(new Date())>0){
                groupUsersListone.add(groupUsers);
            }
        }

        return ResultUtil.resultToResponse(Result.success(groupUsersListone));
    }

     /**
     * 群转让
     */
    @PostMapping("/transfer")
    @ApiOperation("群主转让")
    public ResponseEntity transfer(@RequestHeader("userId")String userId, @RequestBody Transfer transfer){

        //判断
        if (transfer==null){
            return ResultUtil.response(HttpStatus.NOT_FOUND,"请传入群转让的数据");
        }

        //判断
        if (transfer.getUserId()==null){
            return ResultUtil.response(HttpStatus.NOT_FOUND,"转让的用户不能为空");
        }

        Group groupById = groupService.getGroupById(transfer.getGroupId());

        if (groupById==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该群不存在或已删除");
        }

        if(!groupById.getOwnerId().equals(userId)){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"您没有该权限进行操作");
        }

        GroupUsers groupUserByUserId = groupUserService.getGroupUserByUserId(transfer.getUserId(), transfer.getGroupId());

        if (groupUserByUserId==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该用户不存在该群");
        }

        Group group = groupService.transferGroup(groupById,transfer.getUserId());

        if (groupUserByUserId.getIsAdmin().equals(false)){
            groupUserService.SetAdmin(groupUserByUserId);
        }

        GroupUsers groupUsers = groupUserService.getGroupUserByUserId(userId,transfer.getUserId());

        groupUserService.unSetAdmin(groupUsers);

        return ResultUtil.resultToResponse(Result.success(group));
    }

    @GetMapping("/messages/{groupId}")
    @ApiOperation("显示群聊历史记录")
    public ResponseEntity messages(@RequestHeader("userId")String userId,@PathVariable("groupId") String groupId){
        GroupUsers groupUsers = groupUserService.getGroupUserByUserId(userId, groupId);
        if(groupUsers == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("您不是该群成员 , 无权查看该群历史记录");
        }
        return  ResultUtil.resultToResponse(Result.success(groupMessageService.getAllGroupMessageWithASC(groupId)));
    }

    @PostMapping("/deleteMessages")
    @ApiOperation("清空群聊历史记录")
    public ResponseEntity deleteMessages(@RequestHeader("userId")String userId,@RequestParam("groupId") String groupId){
        GroupUsers groupUsers = groupUserService.getGroupUserByUserId(userId, groupId);
        if(groupUsers == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("您不是该群成员 , 无权查看该群历史记录");
        }
        Group groupById = groupService.getGroupById(groupId);
        if (!userId.equals(groupById.getOwnerId())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("您不是群主,无权清空该群历史记录");
        }
         groupMessageService.deleteMessages(groupId);
        return  ResultUtil.resultToResponse(Result.success("删除成功"));
    }

    /**
     * 显示群聊历史记录
     */
    @GetMapping("/messagesPage")
    @ApiOperation("分页展示群聊历史记录")
    public ResponseEntity messages(@ApiParam(value = "用户ID(携带token自动填入 , 不用管)") @RequestHeader("userId")String userId,
                                   @ApiParam(value = "页码,必填") @RequestParam("pageNum") Integer pageNum,
                                   @ApiParam(value = "每页展示数量,必填") @RequestParam(value = "pageSize")Integer pageSize,
                                   @ApiParam(value = "群id,必填")@RequestParam("groupId")String groupId){

        GroupUsers groupUsers = groupUserService.getGroupUserByUserId(userId, groupId);
        if(groupUsers == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("您不是该群成员 , 无权查看该群历史记录");
        }
        return  ResultUtil.resultToResponse(Result.success(groupMessageService.getAllGroupMessageWithASC(userId, groupId, pageNum , pageSize)));
    }

    /**
     * 查看所拥有的群聊
     */
    @GetMapping("/findgroup")
    @ApiOperation("查看所拥有的群聊")
    public ResponseEntity findMyGroups(@RequestHeader("userId") String userId){
        return ResponseEntity.ok(
                groupService.getGroupByUserId(userId)
        );
    }


    @PostMapping("/findGroupName")
    @ApiOperation("查看聊天界面的群名称和群成员数量")
    public ResponseEntity findGroupName(@RequestBody GroupId groupId){
        if (groupId==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"未收到群Id");
        }

        Group groupById = groupService.getGroupById(groupId.getGroupId());

        if (groupById==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"该群组不存在或者已解散");
        }

        Long groupUserSize = groupUserService.getGroupUserSize(groupId.getGroupId());

        GroupSize groupSize=new GroupSize()
                .setGroupId(groupId.getGroupId())
                .setGroupName(groupById.getName())
                .setGroupUserSize(groupUserSize)
                .setGroupHead(groupById.getGroupHead());

        return ResultUtil.resultToResponse(Result.success(groupSize));
    }

    /**
     * 创建群聊
     */
    @PostMapping("/new")
    @ApiOperation("新建群")
    public ResponseEntity createGroup(@RequestHeader("userId")String userId,@RequestBody NewGroup newGroup){
        log.info("群的参数 - {}",newGroup);
        // 新建群
        Group group = groupService.saveGroup(newGroup,userId);
        //返回一个Result类，里面带了group对象
        return ResultUtil.resultToResponse(Result.success(group));
    }

    /**
     * 解散群
     */
    @PostMapping("/remove")
    @ApiOperation("解散群")
    public ResponseEntity unGroup(@RequestBody GroupId groupId,@RequestHeader("userId") String userId){
        //群聊id不能为空
        if (groupId==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"群聊Id不能为空");
        }

        //根据群聊id获取群聊的信息
        Group group=groupService.getGroupById(groupId.getGroupId());


        //判断该群聊是否已经解散
        if (group==null||!group.getIsLive()){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该群聊已解散！");
        }

        //判断创建群聊的用户id是否于当前操作群聊的用户id是否一致
        String ownerId = group.getOwnerId();

        if (ownerId.equals(userId)){
            groupService.removeGroupById(groupId.getGroupId());
            //删除群成员关系表的记录
            groupUserService.removeGroupUserByGroupId(groupId.getGroupId());
        }else{
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"创建群聊的用户id与当前进行操作的用户id不匹配！");
        }

        return ResultUtil.resultToResponse(Result.success("解散成功"));
    }

    /**
     * 加入群聊
     * @param joinGroup 加入群对象
     */
    @PostMapping("/add")
    @ApiOperation("用户加入群聊")
    public ResponseEntity joinGroup(@RequestBody @Valid JoinGroup joinGroup){
        String gid = joinGroup.getGid();
        List<String> uids = joinGroup.getUids();
        //群聊id不能为空
        if (gid==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"群聊Id不能为空");
        }

        //加入群聊的群成员id不能为空或者小于0
        if (uids==null || uids.isEmpty()){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"加入群聊的群成员id不能为空或者小于0");
        }

        //查询用户是否存在于这个群
        List<GroupUsers> groupUserByUserId = groupUserService.getGroupUserByUserId(joinGroup.getUids(),gid);

        for (int i = 0; i < joinGroup.getUids().size(); i++) {
            for (int j = 0; j < groupUserByUserId.size(); j++) {
                if (joinGroup.getUids().get(i).equals(groupUserByUserId.get(i).getUserId())){
                    return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"存在已进群用户！");
                }
            }
        }

        groupUserService.joinGroupByGid(gid,uids);

        return ResultUtil.resultToResponse(Result.success("加入成功"));
    }

    /**
     * 退出群聊
     * @param groupId
     */
    @PostMapping("/quit")
    @ApiOperation("用户退出群聊")
    public ResponseEntity quitGroup(@RequestHeader("userId")String userId,@RequestBody GroupId groupId){


        if (groupId.getGroupId()==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"群聊Id不能为空");
        }
        //获取群聊的信息
        Group group = groupService.getGroupById(groupId.getGroupId());

        //判断群聊是否存在
        if (group==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该群聊已解散！");
        }

        groupUserService.removeGroupUserByUserId(userId,groupId.getGroupId());

        return ResultUtil.resultToResponse(Result.success("退出群聊成功"));
    }

    /**
     * 设置群管理员
     * @param joinGroup
     */
    @PostMapping("/setmod")
    @ApiOperation("设置群管理员")
    public ResponseEntity setMod(@RequestHeader("userId")String userId,@RequestBody @Valid JoinGroup joinGroup){
        Group groupById = groupService.getGroupById(joinGroup.getGid());

        if (!userId.equals(groupById.getOwnerId())){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"您没有该权限！");
        }

        List<GroupUsers> users = groupUserService.getGroupUserByUserId(joinGroup.getUids(),joinGroup.getGid());

        //判断是否存在
        if (users.isEmpty()){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"没有查到该群或者该群没有该用户！");
        }

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getIsAdmin()){
                return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该用户是管理员身份！");
            }
        }

        //如果存在就修改为管理员
        List<GroupUsers> usersList= groupUserService.SetAdmin(users);

        return ResultUtil.resultToResponse(Result.success(usersList));
    }

    /**
     * 取消群管理员
     * @param joinGroup
     */
    @PostMapping("/unsetmod")
    @ApiOperation("取消群管理员")
    public ResponseEntity unsetMod(@RequestHeader("userId")String userId,@RequestBody @Valid JoinGroup joinGroup){

        //获取群消息
        Group groupById = groupService.getGroupById(joinGroup.getGid());

        //获取群成员信息
        List<GroupUsers> users = groupUserService.getGroupUserByUserId(joinGroup.getUids(),joinGroup.getGid());

        if (users.isEmpty()){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"没有查到该群或者该群没有该用户！");
        }

        if (!userId.equals(groupById.getOwnerId())){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该用户没有此权限！");
        }

        return ResultUtil.resultToResponse(groupUserService.unSetAdmin(users));

    }

    /**
     * 更新群资料
     * @param updateGroup
     */
    @PostMapping("/update")
    @ApiOperation("更新群资料")
    public ResponseEntity updateGroupData(@RequestHeader("userId")String userId,@RequestBody @Valid UpdateGroup updateGroup){

        if (updateGroup.getGroupId()==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"群聊Id不能为空");
        }

        Group groupById = groupService.getGroupById(updateGroup.getGroupId());

        if (groupById==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"该群组不存在或者已解散");
        }

        //获取该用户信息是否为管理员
        GroupUsers groupUserById = groupUserService.getGroupUserByUserId(userId,updateGroup.getGroupId());

        if (groupUserById==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"该群用户不存在或已踢出！");
        }

        if (updateGroup.getDisplayName()!=null){
            groupUserService.updateGroupUser(groupUserById,updateGroup.getDisplayName());
        }

        //判断是否为管理员
        if (groupUserById.getIsAdmin()){
            Group RenewalGroupData =  groupService.updateGroupData(updateGroup,groupById);
            return ResultUtil.resultToResponse(Result.success(RenewalGroupData));
        }
        return ResultUtil.response(HttpStatus.BAD_REQUEST,"该用户没有此权限！");
    }

    /**
     * 查看群成员
     * @param groupId
     */
    @GetMapping("/users")
    @ApiOperation("查看群成员")
    public ResponseEntity groupUsers(@RequestHeader("userId")String userId,@RequestParam String groupId){

        GroupUsers groupUserByUserId = groupUserService.getGroupUserByUserId(userId,groupId);

        if (groupUserByUserId==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"您不属于该群成员！");
        }

        return ResponseEntity.ok(groupUserService.findAllGroupMembers(groupId , userId));
    }

    /**
     * 踢出群成员
     * @param joinGroup
     */
    @PostMapping("/kick")
    @ApiOperation("踢出群成员")
    public ResponseEntity kickGroup(@RequestHeader("userId")String userId,@RequestBody @Valid JoinGroup joinGroup){

        if (joinGroup.getGid()==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"群聊Id不能为空");
        }

        if (joinGroup.getUids()==null){
            return ResultUtil.response(HttpStatus.BAD_REQUEST,"踢出用户的Id不能为空");
        }

        GroupUsers groupUserByUserId = groupUserService.getGroupUserByUserId(userId,joinGroup.getGid());
        log.info("用户表 - {}",groupUserByUserId);
        if (!groupUserByUserId.getIsAdmin()){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"该用户没有此权限！");
        }

        return ResultUtil.resultToResponse(groupUserService.kickGroup(joinGroup.getUids(),joinGroup.getGid()));
    }

}

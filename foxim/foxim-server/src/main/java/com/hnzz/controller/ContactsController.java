package com.hnzz.controller;

import cn.hutool.core.bean.BeanUtil;
import com.hnzz.common.ResultUtil;
import com.hnzz.commons.base.enums.activity.ActivitiesField;
import com.hnzz.commons.base.enums.userenums.ContactStatus;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.dto.ContactSort;
import com.hnzz.dto.ForwardDTO;
import com.hnzz.entity.Activities;
import com.hnzz.dto.UserDTO;
import com.hnzz.entity.Contacts;
import com.hnzz.form.ContactsForm;
import com.hnzz.form.ForwardForm;
import com.hnzz.service.ActivitiesService;
import com.hnzz.service.ContactsService;
import com.hnzz.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @PackageName:com.zzkj.controller
 * @ClassName:ContactsController
 * @Author 周俊
 * @Date 2023/1/4 10:22
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/v1/contacts")
@Slf4j
@Api(tags = "好友关系接口")
public class ContactsController {
    @Resource
    private ContactsService contactsService;
    @Resource
    private UserService userService;
    @Resource
    private ActivitiesService activitiesService;

    @PostMapping("/setForwardTime")
    @ApiOperation("更新转发请求")
    public ResponseEntity<String> setForwardTime(@RequestHeader("userId") String userId, @RequestBody ForwardForm forwardForm){

        contactsService.setForwardTime(forwardForm , userId);
        return ResponseEntity.ok("转发成功!");
    }

    @GetMapping("/getForwardList")
    @ApiOperation("获取最近转发列表")
    public ResponseEntity<List<ForwardDTO>> getForwardList(@RequestHeader("userId") String userId){
        return ResponseEntity.ok(
                contactsService.getForwardList(userId)
        );
    }

    /**
     * 删除好友
     *
     * @param id
     */
    @PostMapping("/remove/{id}")
    @ApiOperation("删除好友")
    public ResponseEntity<String> remove(@RequestHeader("userId") String userId, @PathVariable("id") String id) {
        boolean b = contactsService.deleteId(id, userId);
        contactsService.deleteId(userId, id);
        if (b) {
            return ResponseEntity.ok("删除成功!");
        } else {
            return ResultUtil.response(HttpStatus.NOT_FOUND, "好友不存在!");
        }
    }

    /**
     * 查询好友列表
     *
     * @return
     */
    @GetMapping("/findMyFriends")
    @ApiOperation("查询好友列表")
    public ResponseEntity<List<UserDTO>> findMyFriends(@RequestHeader("userId") String userId) {

        List<Contacts> friendsInfo = contactsService.findMyFriends(userId);

        List<UserDTO> usersById = userService.getMyFriends(friendsInfo);


        if (usersById != null && usersById.size() != 0) {
            Collections.sort(usersById);
        }
        return ResponseEntity.ok(usersById);
    }

    @GetMapping("/recents")
    @ApiOperation("获取最近联系人列表")
    public ResponseEntity getContactSort(@RequestHeader("userId") String userId) {
        if (userId == null) {
            return ResultUtil.response(HttpStatus.NOT_FOUND, "用户Id不存在!");
        }
        List<ContactSort> contactSortByUserId = contactsService.getContactSortByUserId(userId);

        return ResponseEntity.ok(contactSortByUserId);
    }


    /**
     * 拉黑好友
     *
     * @param
     * @param
     */
    @PostMapping("/block")
    @ApiOperation("拉黑好友")
    public ResponseEntity<String> updateById(@RequestHeader("userId") String userId, @RequestBody Contacts contacts) {
        if (userId == null) {
            return ResultUtil.response(HttpStatus.NOT_FOUND, "用户Id不存在!");
        }
        if (contacts.getContactId() == null) {

            return ResultUtil.response(HttpStatus.NOT_FOUND, "未传入该好友Id!");
        }
        Contacts contactsByUserId = contactsService.selectByUserId(userId, contacts.getContactId());
        if (contactsByUserId == null) {

            return ResultUtil.response(HttpStatus.NOT_FOUND, "未查询到好友!");
        }

        if (contactsByUserId.getStatus().equals(ContactStatus.BLOCKED.getCode())) {
            return ResultUtil.response(HttpStatus.NOT_FOUND, "好友处于拉黑状态!");
        }
        contactsService.updateById(contactsByUserId);

        return ResponseEntity.ok("拉黑成功!");
    }

//    /**
//     * 新增好友
//     *
//     * @param
//     * @return
//     */
//    @PostMapping("/addSave")
//    @ApiOperation("新增好友")
//
//    public ResponseEntity<String> addSave(@RequestBody ContactsForm contactId, @RequestHeader("userId") String userId) {
//
//        // 判断a和b是否存在好友关系
//        Contacts andB = contactsService.selectByUserId(userId, contactId.getContactId());
//        if (andB != null) {
//            return ResultUtil.response(HttpStatus.NOT_FOUND, "该用户与你是好友关系!");
//        }
//        contactsService.addSave(userId, contactId.getContactId());
//        contactsService.addSave(contactId.getContactId(), userId);
//        return ResponseEntity.ok("添加成功!");
//    }

    /**
     * 修改联系人关系
     *
     * @param
     * @param
     */
    @PostMapping("/setContact")
    @ApiOperation("修改联系人关系")
    public ResponseEntity<Contacts> contactId(@RequestHeader("userId") String userId, @RequestBody ContactsForm contacts) {

        if (contacts.getContactId() == null) {
            throw new AppException("未传入该好友Id!");
        }
        if (!contactsService.existsContacts(userId, contacts.getContactId())) {
            throw new AppException("未查询到好友!");
        }

        return ResponseEntity.ok(contactsService.updateContactId(contacts , userId));
    }

    @GetMapping("/see/{contactId}")
    @ApiOperation("查看好友关系详情")
    public ResponseEntity<Contacts> see(@PathVariable("contactId") String contactId, @RequestHeader("userId") String userId) {
        Contacts contacts = contactsService.selectByUserId(userId, contactId);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/blockList")
    @ApiOperation("拉黑联系人列表")
    public ResponseEntity blockList(@RequestHeader("userId") String userId) {
        if (userId == null) {
            return ResultUtil.response(HttpStatus.NOT_FOUND, "用户Id不存在!");
        }

        List<String> blockList = contactsService.blockList(userId);

        List<UserDTO> usersById = userService.getUsersById(blockList);

        if (usersById == null || usersById.size() == 0) {
            return ResultUtil.response(HttpStatus.NOT_FOUND, "没有拉黑的好友!");
        }
        return ResponseEntity.ok(usersById);
    }

    /**
     * 发送添加好友申请
     *
     * @param
     * @return
     */
    @GetMapping("/addContacts/{friendId}")
    @ApiOperation("发送添加好友申请")
    public ResponseEntity addContacts(@PathVariable("friendId")String friendId, @RequestHeader("userId") String userId) {
        // 判断a和b是否存在好友关系
        Contacts andB = contactsService.selectByUserId(userId, friendId);
        if (andB != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( "该用户与你是好友关系!");
        }
        Contacts contacts = contactsService.addContacts(userId, friendId);
        if (contacts==null){
            return ResponseEntity.ok("已经是好友, 无需重复添加");
        }
        UserDTO userById = userService.findUserById(userId);
        StringBuilder sb = new StringBuilder();
        String s = sb.append(userById.getUsername()).append("请求添加你为好友").toString();
        HashMap<String, String> payload = new HashMap<>(8);
        payload.put(ActivitiesField.CONTACT_ID,friendId);
        payload.put(ActivitiesField.TEXT,s);
        payload.put(ActivitiesField.ACTIVITY_CREATEDAT,new Date ().toString());
        payload.put(ActivitiesField.TYPE,"addFriend");
        String topic = Activities.topic("private", friendId, "addFriend");
        Activities activities = new Activities(topic,payload);
        try {
            activities = activitiesService.saveActivities(activities);
        } catch (IOException e) {
            log.warn("编号为{}的消息发送失败",activities.getId());
            contactsService.deleteContacts(userId,friendId);
            throw new AppException("添加好友消息发送失败!");
        }
        return ResponseEntity.ok(contacts);
    }

    /**
     * 好友申请列表
     * @param userId
     * @return
     */
    @GetMapping("/contactsRequests")
    @ApiOperation("好友申请列表")
    public ResponseEntity contactsRequests(@RequestHeader("userId") String userId) {
        if (userId == null) {
            return ResultUtil.response(HttpStatus.NOT_FOUND, "用户Id不存在!");
        }
        return ResponseEntity.ok(
                contactsService.contactsRequests(userId)
        );
    }

    /**
     * 同意好友申请
     *
     * @param
     * @return
     */
    @GetMapping("/agreeContacts/{friendId}")
    @ApiOperation("同意好友申请")
    public ResponseEntity agreeContacts(@PathVariable("friendId") String friendId, @RequestHeader("userId") String userId) {

        // 判断a和b是否存在好友关系
        Contacts andB = contactsService.selectByUserId(userId, friendId);
        if (andB != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( "该用户与你是好友关系!");
        }
        Contacts contacts = contactsService.agreeContacts(userId, friendId);
        return contacts!=null?ResponseEntity.ok("添加成功"):ResponseEntity.status(HttpStatus.BAD_REQUEST).body("添加失败!");
    }

    /**
     * 删除好友申请
     *
     * @param
     * @return
     */
    @PostMapping("/deleteContacts")
    @ApiOperation("删除好友申请")
    public ResponseEntity deleteContacts(@RequestParam String contactId, @RequestHeader("userId") String userId) {

        // 判断a和b是否存在好友关系
        Contacts andB = contactsService.selectByUserId(userId, contactId);
        if (andB != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( "该用户与你是好友关系!");
        }
        contactsService.deleteContacts(userId, contactId);
        return ResponseEntity.ok("删除成功");
    }
}

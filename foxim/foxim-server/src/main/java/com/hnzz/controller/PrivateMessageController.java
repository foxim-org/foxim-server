package com.hnzz.controller;

import com.hnzz.service.PrivateMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author HB on 2023/1/30
 * TODO 私聊信息管理层
 */
@Slf4j
@RestController
@RequestMapping("api/v1/private-message")
@Api(tags = "私聊信息业务管理层")
public class PrivateMessageController {
    @Resource
    private PrivateMessageService privateMessageService;

    @ApiOperation("分页获取私聊信息")
    @GetMapping("/getPrivateMessagesPage")
    public ResponseEntity getPrivateMessagesPage(@ApiParam(value = "用户ID(携带token自动填入 , 不用管)") @RequestHeader("userId")String userId,
                                                     @ApiParam(value = "页码,必填") @RequestParam("pageNum") Integer pageNum,
                                                     @ApiParam(value = "每页展示数量,必填") @RequestParam(value = "pageSize")Integer pageSize,
                                                     @ApiParam(value = "好友id,必填")@RequestParam("contactId")String contactId){
        return ResponseEntity.ok(privateMessageService.getAllPrivateMessageWithASC(userId, contactId, pageNum , pageSize));
    }
    @ApiOperation("获取所有私聊信息")
    @GetMapping("/getPrivateMessages/{contactId}")
    public ResponseEntity<Object> getPrivateMessages(@RequestHeader("userId")String userId, @PathVariable("contactId")String contactId){
        return ResponseEntity.ok(privateMessageService.getAllPrivateMessageWithASC(userId, contactId));
    }

    @ApiOperation("清空聊天历史记录")
    @PostMapping("/emptyPrivateMessages")
    public ResponseEntity<Object> emptyPrivateMessages(@RequestHeader("userId")String userId, @RequestParam String contactId){
        privateMessageService.emptyPrivateMessages(userId,contactId);
        return ResponseEntity.ok("清除历史记录成功!");
    }

}

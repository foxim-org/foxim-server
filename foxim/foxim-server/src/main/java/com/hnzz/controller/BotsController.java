package com.hnzz.controller;

import cn.hutool.core.bean.BeanUtil;
import com.hnzz.entity.bot.*;
import com.hnzz.form.bots.*;
import com.hnzz.service.BotsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * @author HB on 2023/2/15
 * TODO 机器人管理层
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/bots")
@Api(tags = "机器人接口")
public class BotsController {

    @Resource
    private BotsService botsService;


    @GetMapping("/get/{groupId}")
    @ApiOperation("根据群id获取群机器人")
    public ResponseEntity<Bots> getBotByGroupId(@PathVariable("groupId") String groupId){
        Bots bot = botsService.getBotById(groupId);
        return ResponseEntity.ok().body(bot);
    }

    @PostMapping("/setting/info")
    @ApiOperation("修改群聊机器人基本信息")
    public ResponseEntity<Bots> settingBotInfo(@RequestHeader("userId") String userId, @RequestBody @Valid BotsInfoSetting form){
        Bots botSetting = BeanUtil.copyProperties(form, Bots.class);
        Bots newBots = botsService.settingBots(botSetting, userId);
        return ResponseEntity.ok().body(newBots);
    }
    @PostMapping("/setting/autoForward")
    @ApiOperation("修改群聊机器人自动转发设置")
    public ResponseEntity<Bots> settingBotAutoForward(@RequestHeader("userId") String userId, @RequestBody @Valid BotsAutoForwardSetting form){
        BotsAutoForward botsAutoForward = new BotsAutoForward();
        BeanUtil.copyProperties(form, botsAutoForward);
        Bots bots = new Bots().setId(form.getId()).setAutoForward(botsAutoForward);
        Bots newBots = botsService.settingBots(bots, userId);
        return ResponseEntity.ok().body(newBots);
    }
    @PostMapping("/setting/autoReply")
    @ApiOperation("修改群聊机器人自动问答设置")
    public ResponseEntity<Bots> settingBotAutoReply(@RequestHeader("userId")String userId, @RequestBody @Valid BotsAutoReplySetting form){
        Bots newBots = botsService.settingBotsAutoReplies(form, userId);
        return ResponseEntity.ok().body(newBots);
    }

    @PostMapping("/setting/joinGroup")
    @ApiOperation("修改群聊机器人入群欢迎信息")
    public ResponseEntity<Bots> settingBotJoinGroup(@RequestHeader("userId")String userId, @RequestBody @Valid BotsJoinGroupSetting form){
        BotsJoinGroup botsJoinGroup = BeanUtil.copyProperties(form, BotsJoinGroup.class);
        Bots bots = new Bots().setId(form.getId()).setJoinGroup(botsJoinGroup);
        Bots newBots = botsService.settingBots(bots, userId);
        return ResponseEntity.ok().body(newBots);
    }
    @PostMapping("/setting/speechManage")
    @ApiOperation("修改群聊机器人发言管理")
    public ResponseEntity<Bots> settingBotSpeechManage(@RequestHeader("userId")String userId, @RequestBody @Valid BotsSpeechManageSetting form){
        BotsSpeechManage botsSpeechManage = BeanUtil.copyProperties(form, BotsSpeechManage.class);
        Bots bots = new Bots().setId(form.getId()).setSpeechManage(botsSpeechManage);
        Bots newBots = botsService.settingBots(bots, userId);
        return ResponseEntity.ok().body(newBots);
    }
    @PostMapping("/setting/timedMessage")
    @ApiOperation("修改群聊机器人发送定时消息")
    public ResponseEntity<Bots> settingBotTimedMessage(@RequestHeader("userId")String userId, @RequestBody @Valid BotsTimedMessageSetting form){
        BotsTimedMessage botsTimedMessage = BeanUtil.copyProperties(form, BotsTimedMessage.class);
        Bots bots = new Bots().setId(form.getId()).setTimedMessage(botsTimedMessage);
        Bots newBots = botsService.settingBots(bots, userId);
        return ResponseEntity.ok().body(newBots);
    }
}

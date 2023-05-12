package com.hnzz.controller;


import com.hnzz.service.SmsService;
import com.hnzz.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @PackageName:com.hnzz.controller
 * @ClassName:SmsController
 * @Author zj
 * @Date 2023/3/27 11:43
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/v1/sms")
@Slf4j
@Api(tags = "短信接口")
public class SmsController {

    @Resource
    private SmsService smsService;

    @PostMapping("/codes")
    @ApiOperation("发送验证码")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String mobile) {
            smsService.sendVerificationCode(mobile);
            return ResponseEntity.ok("验证码发送成功");
    }


    @PostMapping("/verify")
    @ApiOperation("校验验证码")
    public ResponseEntity<String> verifyVerificationCode(@RequestParam String mobile, @RequestParam String code) {
        smsService.verifyVerificationCode(mobile, code);
        return ResponseEntity.ok("校验成功!");
    }
}

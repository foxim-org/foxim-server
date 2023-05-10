package com.hnzz.controller;

import cn.hutool.core.bean.BeanUtil;
import com.hnzz.common.ResultUtil;
import com.hnzz.commons.base.result.Result;
import com.hnzz.form.ActivitiesForm;
import com.hnzz.entity.Activities;
import com.hnzz.commons.base.enums.activity.ActivitiesStatus;
import com.hnzz.service.ActivitiesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author HB on 2023/2/2
 * TODO 消息推送管理层
 */
@Slf4j
@RestController
@RequestMapping("api/v1/activities")
@Api(tags = "消息推送接口")
public class ActivitiesController {

    @Resource
    private ActivitiesService activitiesService;

    @PostMapping("save")
    @ApiOperation("新增消息推送业务")
    public ResponseEntity savaActivities(@RequestBody @Valid ActivitiesForm activitiesForm) throws IOException {
        Activities activity = BeanUtil.copyProperties(activitiesForm, Activities.class);
        activity.setStatus(ActivitiesStatus.TO_BE_SEND);
        Activities activities = activitiesService.saveActivities(activity);
        if (activities.getId()==null){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"推送数据未能存入数据库");
        }else if (activities.getStatus().equals(ActivitiesStatus.SEND_FAILED)){
            return ResultUtil.response(HttpStatus.INTERNAL_SERVER_ERROR,"推送数据发送失败");
        }
        return ResultUtil.resultToResponse(Result.success(activities));
    }

    @GetMapping("get/{id}")
    @ApiOperation("根据业务id查询消息")
    public ResponseEntity getActivitiesById(@PathVariable("id")String id){
        Activities activitiesById = activitiesService.findActivitiesById(id);
        if (activitiesById==null){
            return ResultUtil.response(HttpStatus.NOT_FOUND,"不存在该条信息");
        }
        return ResponseEntity.ok(activitiesById);
    }
}

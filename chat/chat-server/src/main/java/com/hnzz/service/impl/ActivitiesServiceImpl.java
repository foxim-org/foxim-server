package com.hnzz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.hnzz.commons.base.log.Log;
import com.hnzz.dao.ActivitiesDao;
import com.hnzz.entity.Activities;
import com.hnzz.entity.ActivitiesOptions;
import com.hnzz.entity.ActivitySendInfo;
import com.hnzz.commons.base.enums.activity.ActivitiesField;
import com.hnzz.commons.base.enums.activity.ActivitiesStatus;
import com.hnzz.service.ActivitiesService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

/**
 * @author HB on 2023/2/1
 * TODO 消息推送业务实现层
 */
@Service
@Slf4j
@RefreshScope
public class ActivitiesServiceImpl implements ActivitiesService {

    @Resource
    private ActivitiesDao activitiesDao;

    @Value("${app.publishUrl}")
    private String publishUrl;

    @Override
    @Log("发送推送消息并存入数据库")
    public Activities saveActivities(Activities activities) throws IOException {
        // 塞入参数并存入数据库
        Map<String, String> payload = activities.getPayload();
        payload.put(ActivitiesField.ACTIVITY_CREATEDAT,new Date().toString());
        activities.setOptions(new ActivitiesOptions());
        Activities a = activitiesDao.saveActivities(activities);

        // 调用接口发送消息
        ActivitySendInfo activitySendInfo = BeanUtil.copyProperties(a, ActivitySendInfo.class);
        activitySendInfo.getPayload().put("activitiesId",a.getId());
        Response response = sendActivities(activitySendInfo);

        // 判断返回值
        if (response.code() == HttpStatus.OK.value()) {
            a.setStatus(ActivitiesStatus.SEND_SUCCESS);
        } else {
            a.setStatus(ActivitiesStatus.SEND_FAILED);
        }
        return activitiesDao.setActivitiesStatus(a);
    }

    @Override
    @Log("根据id查询推送消息")
    public Activities findActivitiesById(String id) {
        return activitiesDao.findActivitiesById(id);
    }

    Response sendActivities(ActivitySendInfo activitySendInfo) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType parse = MediaType.parse("application/json; charset=utf-8");
        String s = JSONUtil.parse(activitySendInfo).toString();
        RequestBody requestBody = RequestBody.create(parse, s);
        Request request = new Request.Builder()
                .post(requestBody).url(publishUrl).build();
        return client.newCall(request).execute();
    }
}

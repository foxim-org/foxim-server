package com.hnzz.service;

import com.hnzz.entity.Activities;

import java.io.IOException;

/**
 * @author HB on 2023/2/1
 * TODO 消息推送业务层
 */
public interface ActivitiesService {
    /**
     * 增
     * @param activities 传入activities
     * @return 返回存储后的Activities
     */
    Activities saveActivities(Activities activities) throws IOException;

    /**
     * 查id
     * @param id 传入消息id
     * @return 返回找到的Activities
     */
    Activities findActivitiesById(String id);

}

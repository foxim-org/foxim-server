package com.hnzz.dao;

import com.hnzz.commons.base.enums.activity.ActivitiesStatus;
import com.hnzz.entity.Activities;

import java.util.List;

/**
 * @author HB on 2023/2/1
 * TODO 消息推送dao层
 */
public interface ActivitiesDao {
    /**
     * 增
     * @param activities 待存入的activities
     * @return Activities 返回存入成功的Activities消息
     */
    Activities saveActivities(Activities activities);

    Activities setActivitiesStatus(Activities activities);

    /**
     * 根据id查
     * @param id 待查询的id
     * @return 返回查询到的Activities
     */
    Activities findActivitiesById(String id);

    /**
     * 根据条件查
     * @param activities 内部填充条件
     * @return 返回符合条件的Activities集合
     */
    List<Activities> findActivitiesByConditions(Activities activities);

}

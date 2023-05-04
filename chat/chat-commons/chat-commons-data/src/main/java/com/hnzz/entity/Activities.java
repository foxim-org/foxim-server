package com.hnzz.entity;

import com.hnzz.commons.base.enums.activity.ActivitiesStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Map;

/**
 * @author HB on 2023/2/1
 * TODO 消息活动实体类
 */
@Data
@NoArgsConstructor
@Document("activities")
@Accessors(chain = true)
public class Activities {

    @Id
    private String id;
    /**
     * 消息订阅通道
     */
    private String topic;
    /**
     * 消息内容 , json格式发送
     */
    private Map<String,String> payload;
    /**
     * 推送消息的状态
     */
    private String status;
    /**
     * 推送消息的设置
     */
    private ActivitiesOptions options = new ActivitiesOptions();

    public static String topic(String type,String id,String option){
        StringBuilder sb = new StringBuilder();
        return sb.append(type).append("/").append(id).append("/").append(option).toString();
    }

    public Activities(String topic,Map<String,String> map){
        this.topic=topic;
        this.payload=map;
        this.status= ActivitiesStatus.TO_BE_SEND;
        this.options=new ActivitiesOptions();
    }
}

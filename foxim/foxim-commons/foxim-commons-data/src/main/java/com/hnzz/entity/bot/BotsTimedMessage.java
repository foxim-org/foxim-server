package com.hnzz.entity.bot;

import lombok.Data;

import java.util.Date;

/**
 * @author HB on 2023/2/22
 * TODO 机器人发送定时消息设置
 */
@Data
public class BotsTimedMessage {

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 消息内容
     */
    private String messageContext;
    /**
     * 附加图片
     */
    private String imgUrl;
    /**
     * 编辑次数
     */
    private int editCount;
    /**
     * 提醒时间
     */
    private Date reminderTime;

    /**
     * 重复次数
     */
    private int repeatNum;

    /**
     * 消息发送前提醒
     */
    private Date reminderBeforeStart;

}

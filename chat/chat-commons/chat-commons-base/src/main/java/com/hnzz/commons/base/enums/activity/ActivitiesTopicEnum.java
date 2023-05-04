package com.hnzz.commons.base.enums.activity;

/**
 * @author HB on 2023/2/9
 * TODO aa
 */
public enum ActivitiesTopicEnum {
    /**
     * 私聊
     */
    PRIVATE("private"),
    /**
     * 群聊
     */
    GROUP("group");

    private final String topic;

    ActivitiesTopicEnum(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}

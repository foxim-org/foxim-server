package com.hnzz.commons.base.enums.activity;

/**
 * @author HB on 2023/2/2
 * TODO
 */
public interface ActivitiesField {
    /**
     * 消息创建时间
     */
    String ACTIVITY_CREATEDAT = "createdAt";
    /**
     * 消息生产者id
     */
    String USER_ID = "userId";
    /**
     * 消息消费者id
     */
    String CONTACT_ID = "contactId";
    /**
     * 群聊id
     */
    String GROUP_ID = "groupId";
    /**
     * 消息正文
     */
    String TEXT = "text";
    /**
     * 图片链接,多个图片用json数组的形式分割
     */
    String IMG_URL = "img_url";

    String TYPE = "type";

}

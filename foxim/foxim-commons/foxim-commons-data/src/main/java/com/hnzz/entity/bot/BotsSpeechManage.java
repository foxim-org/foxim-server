package com.hnzz.entity.bot;

import lombok.Data;


/**
 * @author HB on 2023/2/22
 * TODO 机器人言论管理设置
 */
@Data
public class BotsSpeechManage {
    /**
     * 不准发链接
     */
    private Boolean noLink;

    /**
     * 不准发包含二维码的图片
     */
    private Boolean noImageContainQRCode;

    public BotsSpeechManage() {
        this.noLink = Boolean.FALSE;
        this.noImageContainQRCode = Boolean.FALSE;
    }
}

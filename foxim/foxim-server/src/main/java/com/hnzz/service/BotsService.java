package com.hnzz.service;

import com.hnzz.commons.base.log.Log;
import com.hnzz.entity.bot.Bots;
import com.hnzz.entity.Group;
import com.hnzz.form.bots.BotsAutoReplySetting;

/**
 * @author HB on 2023/2/15
 * TODO
 */
public interface BotsService {
    @Log("创建机器人业务")
    Bots createBots(Group group);

    Bots settingBots(Bots newBot, String userId);


    @Log("修改机器人自助问答设置")
    Bots settingBotsAutoReplies(BotsAutoReplySetting setting, String userId);

    @Log("获取机器人信息")
    Bots getBotById(String id);
}

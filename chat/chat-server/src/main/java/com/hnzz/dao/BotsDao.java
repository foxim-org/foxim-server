package com.hnzz.dao;

import com.hnzz.entity.bot.Bots;
import com.hnzz.form.bots.BotsAutoReplySetting;

/**
 * @author HB on 2023/2/15
 * TODO 机器人dao层
 */
public interface BotsDao {
    /**
     * 新建一个机器人
     *
     * @param bots 保存传入的bot对象
     * @return 返回是否保存成功
     */
    Bots createBot(Bots bots);

    /**
     * 修改设置机器人
     * @param bots 用save保存传入的bot对象
     * @return 返回更新后的bot对象
     */
    Bots settingBot(Bots bots);

    Bots settingBotsAutoReplies(BotsAutoReplySetting setting);

    /**
     * 根据群id获取该群的机器人
     * @param groupId 传入当前所在群的群id
     * @return 返回bot对象
     */
    Bots getBotById(String id);
}

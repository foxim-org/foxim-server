package com.hnzz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.commons.base.log.Log;
import com.hnzz.dao.BotsDao;
import com.hnzz.entity.Group;
import com.hnzz.entity.GroupUsers;
import com.hnzz.entity.bot.Bots;
import com.hnzz.entity.bot.BotsAutoForward;
import com.hnzz.form.bots.BotsAutoReplySetting;
import com.hnzz.service.ActivitiesService;
import com.hnzz.service.BotsService;
import com.hnzz.service.GroupService;
import com.hnzz.service.GroupUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HB on 2023/2/15
 * TODO 机器人业务实现类
 */
@Slf4j
@Service
public class BotsServiceImpl implements BotsService {
    @Resource
    private BotsDao botsDao;
    @Resource
    private GroupUserService groupUserService;
    @Resource
    private GroupService groupService;
    @Resource
    private ActivitiesService activitiesService;

    /**
     * 信息源成员最大值
     */
    private static final Integer SOURCES_MAX = 3;
    /**
     * 转发目标最大值
     */
    private static final Integer TARGETS_MAX = 10;

    @Override
    @Log("创建机器人")
    public Bots createBots(Group group){
        return botsDao.createBot( new Bots(group));
    }



    @Override
    @Log("修改机器人设置")
    public Bots settingBots(Bots newBot, String userId){
        // 鉴权
        authentication(newBot.getId(), userId);
        // 获取修改前的bots信息
        Bots bot = getBotById(newBot.getId());
        // 判断机器人是否开启 , 不开启无法修改
        botsIsEnable(newBot, bot);

        // 判断是否存在自动转发设置
        Optional.ofNullable(newBot.getAutoForward()).ifPresent(value->{
            List<String> sourceIds = new ArrayList<>();
            Optional.ofNullable(value.getSourceIds()).ifPresent(v ->
                sourceIds.addAll(v.stream().distinct().collect(Collectors.toList()))
            );
            if (sourceIds.size() > 0 && sourceIds.size() <= SOURCES_MAX){
                List<GroupUsers> groupUserByUserId1 = groupUserService.getGroupUserByUserId(sourceIds, newBot.getId());
                if (groupUserByUserId1==null||groupUserByUserId1.size()<1){
                    throw new AppException("找不到您所设置的被转发用户信息");
                }else {
                    List<String> groupUserIds = groupUserByUserId1.stream().map(GroupUsers::getUserId).collect(Collectors.toList());
                    boolean isSame = sourceIds.size() == groupUserIds.size() && new HashSet<>(sourceIds).containsAll(groupUserIds);
                    if (!isSame){
                        List<String> differentElements = sourceIds.stream()
                                .filter(e -> !groupUserIds.contains(e))
                                .collect(Collectors.toList());
                        throw new AppException("群成员"+ JSONUtil.parse(differentElements)+"不存在于"+newBot.getId()+"群");
                    }
                }
            } else if (sourceIds.size() > SOURCES_MAX) {
                throw new AppException("信息源成员最多只有三人");
            }
            // 判断添加的转发群是否存在
            Optional.ofNullable(value.getTargetIdsToGroup()).ifPresent(v -> {
                if (v.contains(newBot.getId())){
                    throw new AppException("转发群中不能包含当前机器人所在的群");
                }
                int count = 0;
                count += v.size();
                count = count + (value.getTargetIdsToUser()==null?value.getTargetIdsToUser().size():0);
                if (count>TARGETS_MAX){
                    throw new AppException("转发目标不能超过10个");
                }
            });
            // 判断机器人转发时间是否存在
            if (value.getStartTime() != null && value.getEndTime()!= null && value.getEndTime().compareTo(value.getStartTime())<0){
                throw new AppException("时间设置不合理————开始转发时间必须小于停止时间");
            }
            BotsAutoForward botsAutoForward = new BotsAutoForward();
            BeanUtil.copyProperties(value,botsAutoForward);
            bot.setAutoForward(botsAutoForward);
        });

        bot.setUpdatedAt(new Date());
        return botsDao.settingBot(bot);
    }

    @Override
    @Log("修改机器人自助问答设置")
    public Bots settingBotsAutoReplies(BotsAutoReplySetting setting, String userId){
        // 鉴权
        authentication(setting.getId() , userId);
        // 获取修改前的bots信息
        Bots bot = getBotById(setting.getId());
        if (!bot.getIsEnable()){
            throw new AppException("机器人未开启使用 , 无法修改机器人自助问答");
        }

        return botsDao.settingBotsAutoReplies(setting);
    }

    /**
     * 判断机器人是否可以修改
     * @param bots 外界传入机器人有关设置
     * @param newBot 原有机器人
     */
    private static void botsIsEnable(Bots bots, Bots newBot) {
        Optional.ofNullable(bots.getIsEnable()).ifPresent(value -> {
            if (Objects.equals(value, newBot.getIsEnable())&& bots.getIsEnable().equals(false)){
                throw new AppException("群机器人未开启 , 无法改变机器人设置");
            }
        });
    }


    @Override
    @Log("获取机器人信息")
    public Bots getBotById(String id){
        return botsDao.getBotById(id);
    }

    /**
     * 鉴定用户是否有权限操作该机器人
     * @param botsId 机器人id
     * @param userId 用户id
     */
    private void authentication(String botsId , String userId){
        GroupUsers groupUser = groupUserService.getGroupUserByUserId(userId, botsId);
        if (groupUser==null){
            throw new AppException("群"+botsId+"中不存在用户"+userId);
        }else if (!groupUser.getIsAdmin()){
            throw new AppException("用户"+userId+"没有权限操作该群机器人");
        }
    }
}

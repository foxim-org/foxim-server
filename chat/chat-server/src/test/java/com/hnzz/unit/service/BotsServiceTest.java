package com.hnzz.unit.service;

import com.github.javafaker.Faker;
import com.hnzz.dao.BotsDao;
import com.hnzz.entity.Group;
import com.hnzz.entity.bot.Bots;
import com.hnzz.service.BotsService;
import com.hnzz.service.GroupUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author HB on 2023/2/15
 * TODO 机器人业务层测试
 */
@RunWith(MockitoJUnitRunner.class)
public class BotsServiceTest {
    @Mock
    private BotsDao botsDao;

    @Mock
    private GroupUserService groupUserService;

    @Resource
    private BotsService botsService;

    private Faker faker = new Faker(Locale.CHINA);

    private Group group;

    @Before
    public void setUp() {
        group = new Group();
        group = new Group();
        group.setId(faker.idNumber().valid())
                .setCreatedAt(faker.date().past(365, TimeUnit.DAYS))
                .setGroupHead(faker.internet().avatar())
                .setName(faker.company().name());
    }

    /**
     * 测试创建机器人
     */
    @Test
    public void testCreateBots() {
        Bots bots = botsService.createBots(group);

        // 判断机器人是否创建成功
        System.out.println(bots);
    }

    /**
     * 测试修改机器人设置
     */
    @Test
    public void testSettingBots() {
        Bots bots = new Bots();
        // TODO 初始化bots对象
        String userId = "xxx";
        Bots updatedBots = botsService.settingBots(bots, userId);

        // 判断机器人设置是否修改成功
        assertEquals(bots.getId(), updatedBots.getId());
    }

    /**
     * 测试获取机器人信息
     */
    @Test
    public void testGetBotByGroupId() {
        String groupId = "xxx";
        Bots bots = botsService.getBotById(groupId);

        // 判断获取机器人信息是否成功
        assertNotNull(bots.getId());
    }
}

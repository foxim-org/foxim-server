package com.hnzz.unit.control;

import cn.hutool.json.JSONUtil;
import com.github.javafaker.Faker;
import com.hnzz.entity.User;
import com.hnzz.form.userform.LoginForm;
import com.hnzz.form.userform.RegisterValidForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.annotation.Resource;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author HB on 2023/2/8
 * TODO 测试
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Resource
    private MockMvc mockMvc;
    private final Faker faker = new Faker(Locale.CHINA);
    private final String URL_PREFIX="/api/v1/user/";

    @Before
    public void setUp(){
        User user = new User();
        user.setUsername("zhaoyi")
                .setMobile("15123483561")
                .setPassword("11111111")
                .setId("");
    }


    @Test
    public void testUserRegister() throws Exception {
        // 生成注册的随机数据
        RegisterValidForm registerValidForm = new RegisterValidForm();
        registerValidForm.setMobile(faker.regexify("[1][3456789][0-9]{9}"));
        registerValidForm.setUsername(faker.name().name());
        registerValidForm.setPassword("12345678");
        // 将随机数据转换为json格式
        String s = JSONUtil.parse(registerValidForm).toString();
        log.info("生成随机数据为: {}",s);
        // 调用接口进行测试
        mockMvc.perform(post(URL_PREFIX + "register",2)
                .contentType(MediaType.APPLICATION_JSON).content(s))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUserLogin() throws Exception {
        LoginForm loginForm = new LoginForm("zhaoyi","11111111");
        String s = JSONUtil.parse(loginForm).toString();
        mockMvc.perform(post(URL_PREFIX + "login",2)
                        .contentType(MediaType.APPLICATION_JSON).content(s))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void aaa() throws Exception {
        for (int i = 0; i < 1000; i++) {
            System.out.println("第一个");
            testUserRegister();
        }
    }
}

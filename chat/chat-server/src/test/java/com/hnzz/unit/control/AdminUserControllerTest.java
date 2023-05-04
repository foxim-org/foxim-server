package com.hnzz.unit.control;

import cn.hutool.json.JSONUtil;
import com.github.javafaker.Faker;
import com.hnzz.form.AdminUserLoginForm;
import com.hnzz.form.AdminUserRegisterForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author HB on 2023/3/2
 * TODO 管理员测试
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AdminUserControllerTest {

    @Resource
    private MockMvc mockMvc;

    private final Faker faker = new Faker(Locale.ENGLISH);

    private final String URL_PREFIX="/api/v1/admin/";

    @Test
    public void getInviteLink() throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL_PREFIX + "inviteLink");
        mockMvc.perform(get(builder.toUriString(),2)
                        .header("adminId","6406fc874800d339ca31bd0c"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void register() throws Exception {
        AdminUserRegisterForm registerForm = new AdminUserRegisterForm();
        registerForm.setUsername(faker.name().name());
        registerForm.setMobile(faker.regexify("1[3456789][0-9]{9}"));
        registerForm.setPassword("12345678");
        registerForm.setPlatformName(faker.company().name());
        // 将随机数据转换为json格式
        String s = JSONUtil.parse(registerForm).toString();
        // 调用接口进行测试
        mockMvc.perform(post(URL_PREFIX + "y/register",2)
                        .contentType(MediaType.APPLICATION_JSON).content(s))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void login() throws Exception {
        AdminUserLoginForm loginForm = new AdminUserLoginForm("64006525f1ea13782727c38c","12345678");
        String s = JSONUtil.parse(loginForm).toString();
        mockMvc.perform(post(URL_PREFIX + "y/login",2)
                        .contentType(MediaType.APPLICATION_JSON).content(s))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }



}

package com.hnzz.unit.control;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author HB on 2023/2/13
 * TODO 用户关系管理层测试
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class ContactsControllerTest {
    @Resource
    private MockMvc mockMvc;
    private Faker faker = new Faker(Locale.CHINA);
    private String URL_PREFIX="/api/v1/contacts/";

    @Test
    public void testGetContactSort() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(URL_PREFIX + "recents", 1).header("userId", "63bbcc57e0308e2ea979587e")

                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        response.setCharacterEncoding("UTF-8");
        log.info("打印响应信息==>{}",response.getContentAsString());
    }


}

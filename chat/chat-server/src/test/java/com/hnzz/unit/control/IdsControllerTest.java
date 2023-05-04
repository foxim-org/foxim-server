package com.hnzz.unit.control;

import com.github.javafaker.Faker;
import com.hnzz.service.IdsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.annotation.Resource;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author HB on 2023/2/9
 * TODO 号池管理测试
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class IdsControllerTest {
    @Resource
    private MockMvc mockMvc;
    private Faker faker = new Faker(Locale.CHINA);
    private String URL_PREFIX="/api/v1/ids/";

    @MockBean
    private IdsService idsService;

    @Before
    public void setUp(){
//        Mockito.when(idsService.getIdWithRandom()).thenReturn(new Integer(123456));
    }

    @Test
    public void testGetIdWithRandom() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "get",2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}

package com.hnzz.unit.service;

import com.hnzz.service.IdsService;
import com.hnzz.service.impl.IdsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author HB on 2023/2/9
 * TODO ids业务层单元测试
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class IdsServiceTest {

    @Resource
    private MongoTemplate mongoTemplate;
    @InjectMocks
    private IdsService idsService = Mockito.mock(IdsServiceImpl.class);

}

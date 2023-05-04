package com.hnzz.service.impl;

import com.hnzz.entity.SearchUrl;
import com.hnzz.service.SearchUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HB on 2023/4/27
 * TODO 配置读取业务实现层
 */
@Slf4j
@Service
public class SearchUrlServiceImpl implements SearchUrlService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<SearchUrl> getAllSearchUrl() {
        return mongoTemplate.findAll(SearchUrl.class);
    }

    @Override
    public SearchUrl setSearch(SearchUrl searchUrl) {

        return mongoTemplate.insert(searchUrl);
    }

}

package com.hnzz.service.impl;

import cn.hutool.json.JSONUtil;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.commons.base.log.Log;
import com.hnzz.entity.ReservedId;
import com.hnzz.form.IdsPattern;
import com.hnzz.service.IdsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author HB on 2023/2/9
 * TODO 号池业务实现层
 */
@Slf4j
@Service
public class IdsServiceImpl implements IdsService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    @Log("号池抽号业务")
    public Integer getIdWithRandom(IdsPattern idsPattern) {
        List<String> trueFields = this.getTrueFields(idsPattern);
        Criteria criteria = Criteria.where("used").ne(true);
        if (!trueFields.isEmpty()){
            criteria.and("reserved").is(true);
            for (String trueField : trueFields) {
                criteria.and(trueField).is(true);
            }
        }else {
            criteria.and("reserved").is(false);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.sample(1)
        );
        AggregationResults<ReservedId> results = mongoTemplate.aggregate(aggregation, "ids", ReservedId.class);
        List<ReservedId> mappedResults = results.getMappedResults();
        log.info("号池所有信息：{}",mappedResults);
        if (mappedResults.isEmpty()){
            throw new AppException("号池中不存在未分配的号码");
        }

        ReservedId ids = mappedResults.get(0);
        return ids.getId();
    }

    @Override
    public void setIdsUsed(Integer foxCode){
        Update update = new Update();
        update.set("used",true);
        mongoTemplate.upsert(new Query(Criteria.where("value").is(foxCode)),update,"ids");
    }

    private List<String> getTrueFields(IdsPattern idsPattern) {
        List<String> trueFields = new ArrayList<>();
        Field[] fields = idsPattern.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.getBoolean(idsPattern)) {
                    trueFields.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                // 忽略访问限制问题
            }
        }
        return trueFields;
    }
}

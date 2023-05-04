package com.hnzz.dao.impl;

import com.hnzz.dao.FavsDao;
import com.hnzz.entity.Favs;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @PackageName:com.hnzz.dao.impl
 * @ClassName:FavsDaoImpl
 * @Author zj
 * @Date 2023/3/29 15:01
 * @Version 1.0
 **/
@Repository
public class FavsDaoImpl implements FavsDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public Favs save(Favs favs) {
        return mongoTemplate.save(favs);
    }

    @Override
    public List<Favs> findFavorites(String userId) {
        return mongoTemplate.find(new Query(Criteria.where("userId").is(userId)), Favs.class);
    }

    @Override
    public boolean putCollect(String id) {
        DeleteResult id1 = mongoTemplate.remove(new Query(Criteria.where("id").is(id)), Favs.class);
        return id1.wasAcknowledged();
    }


}

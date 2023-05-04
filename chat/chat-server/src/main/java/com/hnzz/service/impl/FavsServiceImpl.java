package com.hnzz.service.impl;

import com.hnzz.dao.FavsDao;
import com.hnzz.entity.Favs;
import com.hnzz.service.FavsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @PackageName:com.hnzz.service.impl
 * @ClassName:FavsServiceImpl
 * @Author zj
 * @Date 2023/3/29 15:00
 * @Version 1.0
 **/
@Service
public class FavsServiceImpl implements FavsService {

    @Autowired
    private FavsDao favsDao;

    @Override
    public Favs findByUserIdAndMessageId(Favs favs) {
        return favsDao.save(favs);
    }

    @Override
    public List<Favs> findFavorites(String userId) {
        return favsDao.findFavorites(userId);
    }

    @Override
    public boolean putCollect(String id) {
        return favsDao.putCollect(id);
    }

}

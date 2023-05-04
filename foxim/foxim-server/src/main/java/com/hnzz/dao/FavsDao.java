package com.hnzz.dao;

import com.hnzz.entity.Favs;

import java.util.List;

/**
 * @PackageName:com.hnzz.dao
 * @ClassName:FavsDao
 * @Author zj
 * @Date 2023/3/29 15:01
 * @Version 1.0
 **/
public interface FavsDao {

    Favs save(Favs favs);

    List<Favs> findFavorites(String userId);

    boolean putCollect(String id);

}

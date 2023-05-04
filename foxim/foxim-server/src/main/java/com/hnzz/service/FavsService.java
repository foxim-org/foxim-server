package com.hnzz.service;

import com.hnzz.entity.Favs;

import java.util.List;

public interface FavsService {

    Favs findByUserIdAndMessageId(Favs favs);

    List<Favs> findFavorites(String userId);

    boolean putCollect(String id);
}

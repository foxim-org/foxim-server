package com.hnzz.service;

import com.hnzz.entity.SearchUrl;

import java.util.List;

/**
 * @author HB on 2023/4/27
 * TODO 配置读取业务
 */
public interface SearchUrlService {

    List<SearchUrl> getAllSearchUrl();

    SearchUrl setSearch(SearchUrl searchUrl);
}

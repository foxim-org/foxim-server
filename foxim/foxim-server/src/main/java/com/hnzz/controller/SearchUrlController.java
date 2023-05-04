package com.hnzz.controller;

import com.hnzz.entity.SearchUrl;
import com.hnzz.service.SearchUrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HB on 2023/4/27
 * TODO 配置相关接口
 */
@RestController
@RequestMapping("api/v1/searchUrl/")
public class SearchUrlController {
    @Resource
    private SearchUrlService searchUrlService;

    @GetMapping
    public ResponseEntity<List<SearchUrl>> getAllUrl(){
        return ResponseEntity.ok(searchUrlService.getAllSearchUrl());
    }



    @PostMapping("/set")
    public ResponseEntity<SearchUrl> setSearchUrl(@RequestBody SearchUrl searchUrl){
        return ResponseEntity.ok(searchUrlService.setSearch(searchUrl));
    }



}

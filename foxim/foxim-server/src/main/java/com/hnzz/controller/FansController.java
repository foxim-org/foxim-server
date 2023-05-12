package com.hnzz.controller;

import com.hnzz.entity.Favs;
import com.hnzz.service.FavsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @PackageName:com.hnzz.controller
 * @ClassName:FavsController
 * @Author zj
 * @Date 2023/3/29 14:56
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/v1/favs")
@Slf4j
@Api(tags = "收藏内容接口")
public class FansController {

    @Resource
    private FavsService favsService;

    /**
     * 收藏信息
     */
    @PostMapping("addCollect")
    @ApiOperation("收藏信息")
    public ResponseEntity<String> addCollect(@RequestHeader String userId, @RequestBody Favs favs) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户Id不存在!");
        }
        favsService.findByUserIdAndMessageId(favs);
        return ResponseEntity.ok("收藏成功");
    }

    /**
     * 查询所有收藏信息
     */
    @GetMapping("getCollect")
    @ApiOperation("查询所有收藏信息")
    public ResponseEntity<Object> getCollect(@RequestHeader String userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户Id不存在!");
        }
        List<Favs> list = favsService.findFavorites(userId);
        return ResponseEntity.ok(list);
    }

    /**
     * 删除收藏信息
     */
    @PostMapping("/putCollect/{id}")
    @ApiOperation("删除收藏信息")
    public ResponseEntity<String> putCollect(@RequestHeader("userId") String userId, @PathVariable("id") String id) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户Id不存在!");
        }
        favsService.putCollect(id);
        return ResponseEntity.ok("删除成功");
    }
}

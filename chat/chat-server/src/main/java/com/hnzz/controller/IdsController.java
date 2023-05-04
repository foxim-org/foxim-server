package com.hnzz.controller;

import com.hnzz.form.IdsPattern;
import com.hnzz.service.IdsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HB on 2023/2/9
 * TODO 号池接口管理层
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ids")
@Api(tags = "号池接口管理层")
public class IdsController {
    @Resource
    private IdsService idsService;

    @PostMapping("get")
    @ApiOperation("根据选号规则随机抽取一个编号")
    public ResponseEntity<Integer> getIdWithRandom(@RequestBody IdsPattern idsPattern){
        Integer idWithRandom = idsService.getIdWithRandom(idsPattern);
        return ResponseEntity.ok(idWithRandom);
    }


}

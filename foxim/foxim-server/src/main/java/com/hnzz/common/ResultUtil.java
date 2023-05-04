package com.hnzz.common;

import com.hnzz.commons.base.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author HB on 2023/2/6
 * TODO 响应处理工具类
 */
public class ResultUtil {

    /**
     * 用于Result 和 ResponseEntity 之间的转换
     * @param result
     * @return
     */
    public static ResponseEntity resultToResponse(Result result){
        if (result.getCode()==200){
            return ResponseEntity.ok(result.getData());
        }
        return ResponseEntity.status(result.getCode()).body(result.getMessage());
    }

    /**
     * 自定义http的状态码
     * @param status
     * @param result
     * @return
     */
    public static ResponseEntity<String> response(HttpStatus status, String result){
        return ResponseEntity.status(status).body(result);
    }
}

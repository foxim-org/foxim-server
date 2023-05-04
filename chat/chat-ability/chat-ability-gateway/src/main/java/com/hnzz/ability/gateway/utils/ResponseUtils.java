package com.hnzz.ability.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.hnzz.commons.base.result.Result;
import com.hnzz.commons.base.result.ResultCodes;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

/**
 * @author xdf
 * @version 1.0
 */
public class ResponseUtils {
    /**
     * 设置响应体里面的内容
     */
    public static Mono result(ServerWebExchange exchange, ResultCodes codes, HttpStatus httpStatus){

        //获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        //设置一个响应码
        response.setStatusCode(httpStatus);
        //设置响应头，设置响应的数据类型
        response.getHeaders().add("Content-Type","application/json;charset=utf-8");

        //设置响应内容
        DataBuffer dataBuffer=null;
        try {
            Result result = Result.withResultCodes(codes,null);
            dataBuffer=response.bufferFactory().wrap(JSON.toJSONString(result).getBytes("UTF-8"));

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return response.writeWith(Mono.just(dataBuffer));

    }
}

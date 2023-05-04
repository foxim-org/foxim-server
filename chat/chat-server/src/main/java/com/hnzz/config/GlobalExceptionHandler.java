package com.hnzz.config;

import com.hnzz.common.ResultUtil;
import com.hnzz.commons.base.exception.AppException;
import com.hnzz.commons.base.result.Result;
import com.hnzz.commons.base.util.GetBindingResult;
import com.hnzz.entity.AccessRecords;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @PackageName:com.hnzz.config
 * @ClassName:GlobalExceptionHandler
 * @Author 冼大丰
 * @Date 2023/1/12 16:05
 * @Version 1.0
 **/
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = AppException.class)
    @ResponseBody
    public ResponseEntity<String> bizExceptionHandler(HttpServletRequest req, AppException e){
        ResponseEntity<String> body = ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        updateAccessRecord(req,body);
        return body;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<String> exceptionHandler(HttpServletRequest req, MethodArgumentNotValidException e){
        log.error("发生参数校验异常：{}",e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        String validErrorCollect = GetBindingResult.validErrorCollect(bindingResult);
        ResponseEntity<String> body = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validErrorCollect);
        updateAccessRecord(req,body);
        return body;
    }

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    @ResponseBody
    public ResponseEntity<String> exceptionHandler(HttpServletRequest req,MissingRequestHeaderException e){
        log.error("请求头获取参数异常：{}",e.getMessage());
        ResponseEntity<String> body = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        updateAccessRecord(req,body);
        return body;
    }

    /**
     * 处理系统异常
     */

    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public ResponseEntity<String> exceptionHandler(HttpServletRequest req, Exception e){
        log.error("发生系统异常！原因是:",e);
        ResponseEntity<String> body = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        updateAccessRecord(req,body);
        return body;
    }


    private void updateAccessRecord(HttpServletRequest req , ResponseEntity<String> responseEntity){
        String accessId = req.getHeader("accessId");
        Update update = new Update();
        update.set("errorMessage", responseEntity.getBody());
        update.set("statusCode",responseEntity.getStatusCode().value());
        mongoTemplate.upsert(Query.query(Criteria.where("id").is(accessId)),update, AccessRecords.class);
    }
}

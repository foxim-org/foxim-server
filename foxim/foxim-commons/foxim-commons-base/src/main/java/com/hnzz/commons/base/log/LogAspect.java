package com.hnzz.commons.base.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author HB
 * @Classname LogAspect
 * @Date 2023/1/4 14:54
 * @Description TODO
 */
@Aspect
@Slf4j
@Component
public class LogAspect {
    /**
     * 通过环绕通知在接口执行前后输出日志信息
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.hnzz.commons.base.log.Log)")
    public Object logOutputInfo(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature)proceedingJoinPoint.getSignature();
        String operation = ms.getMethod().getAnnotation(Log.class).value();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String finalOp = operation.trim().length() == 0 ? "" : "[" + operation + "]";

        log.info("执行业务"+ finalOp +",参数:{} ",proceedingJoinPoint.getArgs());
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();
        log.info("业务"+ finalOp +"执行完毕: {},耗时{}ms",result,stopWatch.getTotalTimeMillis());
        return result;
    }
}

package com.hnzz.commons.base.log;

import java.lang.annotation.*;

/**
 * @author HB
 * @Classname Log
 * @Date 2023/1/4 14:54
 * @Description TODO
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /*
     * 业务名称
     */
    String value() default "";
}

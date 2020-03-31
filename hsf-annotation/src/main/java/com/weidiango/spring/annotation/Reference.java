package com.weidiango.spring.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.*;

/**
 @author mht
 @date 2019/9/12 16:33
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Autowired
public @interface Reference {

    String value() default "";

    String version() default "";

    String group() default "";

    String target() default "";

    String connectionNum() default "";

    String clientTimeout() default "";

    String maxWaitTimeForCsAddress() default "";

}

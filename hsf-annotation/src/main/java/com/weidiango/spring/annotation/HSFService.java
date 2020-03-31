package com.weidiango.spring.annotation;

import java.lang.annotation.*;

/**
 @author mht
 @date 2019/9/12 16:33
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HSFService {
    String value() default "";

    String version() default "";

    String group() default "";

    String clientTimeout() default "";

    String corePoolSize() default "";

    String enableTXC() default "";

    String maxPoolSize() default "";

    String serializeType() default "";
}

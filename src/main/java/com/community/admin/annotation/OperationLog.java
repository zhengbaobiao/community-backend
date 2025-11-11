package com.community.admin.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author Community Team
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 操作类型
     */
    String value() default "";

    /**
     * 操作描述
     */
    String description() default "";
}

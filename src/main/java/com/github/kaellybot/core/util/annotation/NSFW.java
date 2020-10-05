package com.github.kaellybot.core.util.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to describe if the command argument is NSFW or not
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface NSFW {
    boolean value() default true;
}

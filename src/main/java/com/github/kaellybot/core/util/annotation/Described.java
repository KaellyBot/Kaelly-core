package com.github.kaellybot.core.util.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to know if the argument is described or not
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface Described {
}

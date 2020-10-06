package com.github.kaellybot.core.util.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to describe if the command can only be used by the bot administrator or no
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface SuperAdministrator {
}

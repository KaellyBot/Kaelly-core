package com.github.kaellybot.core.util.annotation;

import com.github.kaellybot.core.model.constant.Priority;

import java.lang.annotation.*;

/**
 * This annotation is used to give an order of processing during the triggering phase.
 * As an example, a HIGH priority will always be taken if the other triggered commands are in a lower priority
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface PriorityProcessing {
    Priority value();
}
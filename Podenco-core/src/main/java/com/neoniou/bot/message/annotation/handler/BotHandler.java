package com.neoniou.bot.message.annotation.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Neo.Zzj
 * @date 2021/7/8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BotHandler {

    /**
     * BotHandler名称，不可重复，指定后可以动态开关整个Handler
     *
     * @return String
     */
    String name() default "";
}

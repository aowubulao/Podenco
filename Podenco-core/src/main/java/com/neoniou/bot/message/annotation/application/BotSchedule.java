package com.neoniou.bot.message.annotation.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定时任务注解，被标注的方法会被加入定时任务
 *
 * @author Neo.Zzj
 * @date 2021/2/20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BotSchedule {

    String cron();
}

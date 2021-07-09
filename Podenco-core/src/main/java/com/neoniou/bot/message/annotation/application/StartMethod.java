package com.neoniou.bot.message.annotation.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开始计划任务，用于初始化服务
 * 被标记的方法会在Bot启动前调用，此方法会阻塞，被此注解标记的方法一定会在Bot启动前执行完成
 *
 * @author Neo.Zzj
 * @date 2021/2/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StartMethod {
}

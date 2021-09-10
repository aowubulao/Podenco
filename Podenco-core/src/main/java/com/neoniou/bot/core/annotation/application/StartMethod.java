package com.neoniou.bot.core.annotation.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开始计划任务，用于初始化服务
 * 被标记的方法会在Bot启动前调用
 *
 * @author Neo.Zzj
 * @date 2021/2/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StartMethod {

    /**
     * 是否异步执行
     * 为false的方法会阻塞，并一定会在Bot启动前执行完成
     * 为true则会异步执行
     *
     * @return boolean
     */
    boolean async();

    /**
     * 只有在async为true时该字段才有意义
     * 使用场景：
     * 如果为true，则代表这个方法中存在无限循环，那么将使用一个单线程的线程池执行
     * 如果为false，则认为该方法会产生阻塞，但将在一定时间后执行完成，
     * 程序将使用默认的线程池执行方法
     *
     * @return boolean
     */
    boolean infLoop() default false;
}

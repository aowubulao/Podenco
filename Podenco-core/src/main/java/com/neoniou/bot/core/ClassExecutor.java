package com.neoniou.bot.core;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author Neo.Zzj
 * @date 2021/7/16
 */
@Slf4j
public class ClassExecutor {

    public static void execMethod(Class<?> clazz, Method method, Object... obj) {
        try {
            method.invoke(clazz.newInstance(), obj);
        } catch (Exception e) {
            log.error("Method---{}:{}, execute error: ", clazz.getName(), method.getName(), e);
        }
    }
}

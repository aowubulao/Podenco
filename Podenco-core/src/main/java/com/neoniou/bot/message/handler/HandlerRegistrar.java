package com.neoniou.bot.message.handler;

import cn.hutool.core.util.ClassUtil;
import com.neoniou.bot.core.ClassExecutor;
import com.neoniou.bot.core.PodencoCore;
import com.neoniou.bot.core.authority.AuthorityMap;
import com.neoniou.bot.entity.handler.HandlerClass;
import com.neoniou.bot.message.annotation.application.StartMethod;
import com.neoniou.bot.message.annotation.handler.BotHandler;
import com.neoniou.bot.utils.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Neo.Zzj
 * @date 2021/7/9
 */
@Slf4j
public class HandlerRegistrar {

    private static final String CORE_HANDLER_PACKAGE = "com.neoniou.bot.core.handler";

    public static void start(String packageName) {
        AuthorityMap.initConfig();
        Set<Class<?>> classes = getClasses(packageName);

        for (Class<?> clazz : classes) {
            //注册Handler
            execBotHandler(clazz);
            //执行启动方法
            execStartMethod(clazz);
            //注册定时任务
        }
    }

    private static void execBotHandler(Class<?> clazz) {
        BotHandler botHandler = clazz.getAnnotation(BotHandler.class);
        if (botHandler == null) {
            return;
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            BotHandler mBotHandler = method.getAnnotation(BotHandler.class);
            if (mBotHandler == null) {
                continue;
            }
            regMethod(method, clazz, mBotHandler);
        }
    }

    private static void execStartMethod(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        List<Method> methodList = new ArrayList<>();
        for (Method method : methods) {
            StartMethod startMethod = method.getAnnotation(StartMethod.class);
            if (startMethod == null) {
                continue;
            }

            if (startMethod.async()) {
                if (startMethod.infLoop()) {
                    ThreadUtil.createSingle().execute(() ->
                            ClassExecutor.execMethod(clazz, method)
                    );
                } else {
                    PodencoCore.runAsync(() ->
                            ClassExecutor.execMethod(clazz, method)
                    );
                }
            } else {
                methodList.add(method);
            }
        }
        //Execute synchronized method in the last
        for (Method method : methodList) {
            ClassExecutor.execMethod(clazz, method);
        }
    }

    @SuppressWarnings("all")
    private static Set<Class<?>> getClasses(String packageName) {
        Set<Class<?>> classes = ClassUtil.scanPackage(packageName);
        Set<Class<?>> coreClasses = ClassUtil.scanPackage(CORE_HANDLER_PACKAGE);
        for (Class<?> clazz : coreClasses) {
            if (!classes.contains(clazz)) {
                classes.add(clazz);
            }
        }
        return classes;
    }

    private static void regMethod(Method method, Class<?> clazz, BotHandler botHandler) {
        HandlerClass handler = HandlerClass.builder()
                .clazz(clazz)
                .method(method)
                .name(botHandler.name())
                .desc(botHandler.desc())
                .role(botHandler.role())
                .matchStr(botHandler.matchStr())
                .matchRule(botHandler.matchRule())
                .async(botHandler.async())
                .type(botHandler.type())
                .build();

        HandlerMap.put(handler);
    }
}

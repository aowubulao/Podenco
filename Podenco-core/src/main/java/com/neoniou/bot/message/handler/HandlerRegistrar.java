package com.neoniou.bot.message.handler;

import cn.hutool.core.util.ClassUtil;
import com.neoniou.bot.entity.HandlerClass;
import com.neoniou.bot.message.annotation.handler.BotHandler;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Neo.Zzj
 * @date 2021/7/9
 */
public class HandlerRegistrar {

    public static void start(String packageName) {
        Set<Class<?>> classes = ClassUtil.scanPackage(packageName);

        for (Class<?> clazz : classes) {
            BotHandler botHandler = clazz.getAnnotation(BotHandler.class);
            if (botHandler == null) {
                continue;
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
    }

    private static void regMethod(Method method, Class<?> clazz, BotHandler botHandler) {
        HandlerClass handler = HandlerClass.builder()
                .clazz(clazz)
                .method(method)
                .name(botHandler.name())
                .role(botHandler.role())
                .matchStr(botHandler.matchStr())
                .matchRule(botHandler.matchRule())
                .type(botHandler.type())
                .build();

        HandlerMap.put(handler);
    }
}

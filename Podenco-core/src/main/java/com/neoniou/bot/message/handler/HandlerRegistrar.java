package com.neoniou.bot.message.handler;

import cn.hutool.core.util.ClassUtil;
import com.neoniou.bot.message.annotation.handler.BotHandler;

import java.util.Set;

/**
 * @author Neo.Zzj
 * @date 2021/7/9
 */
public class HandlerRegistrar {

    public static void run(String packageName) {
        Set<Class<?>> classes = ClassUtil.scanPackage(packageName);

        for (Class<?> clazz : classes) {
            BotHandler botHandler = clazz.getAnnotation(BotHandler.class);
            if (botHandler == null) {
                continue;
            }
        }
    }
}

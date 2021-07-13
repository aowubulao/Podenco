package com.neoniou.bot.message;

import com.neoniou.bot.message.annotation.application.BotComponentScan;
import com.neoniou.bot.message.handler.HandlerRegistrar;

/**
 * @author Neo.Zzj
 * @date 2021/7/8
 */
public class AnnotationRegistrar {

    public static void run(Class<?> mainClass) {
        BotComponentScan componentScan = mainClass.getAnnotation(BotComponentScan.class);
        if (componentScan != null) {
            HandlerRegistrar.start(componentScan.ScanPackage());
        }
    }
}

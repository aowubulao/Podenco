package com.neoniou.bot.core;

import com.neoniou.bot.config.PodencoConfig;
import com.neoniou.bot.message.AnnotationRegistrar;

/**
 * @author Neo.Zzj
 * @date 2021/7/8
 */
public class PodencoCore {

    private PodencoConfig podencoConfig;

    public static void run(Class<?> mainClass, String... args) {
        run(mainClass, new PodencoConfig(), args);
    }

    public static void run(Class<?> mainClass, PodencoConfig config, String... args) {
        AnnotationRegistrar.run(mainClass);
    }

    public PodencoCore(PodencoConfig podencoConfig, Class<?> mainClass) {
        this.podencoConfig = podencoConfig;
    }
}

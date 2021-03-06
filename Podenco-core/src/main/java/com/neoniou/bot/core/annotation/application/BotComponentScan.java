package com.neoniou.bot.core.annotation.application;

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
public @interface BotComponentScan {

    /**
     * 要扫描的包名
     *
     * @return package name
     */
    String ScanPackage();
}

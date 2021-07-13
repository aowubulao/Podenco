package com.neoniou.bot.core.log;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase;
import org.jetbrains.annotations.Nullable;

/**
 * 继承MiraiLog，替换自带的Logger
 *
 * @author Neo.Zzj
 * @date 2021/7/13
 */
@Slf4j
public class PodencoLogger extends MiraiLoggerPlatformBase {

    @Nullable
    @Override
    public String getIdentity() {
        return null;
    }

    @Override
    protected void debug0(@Nullable String s, @Nullable Throwable throwable) {
        log.debug(s, throwable);
    }

    @Override
    protected void error0(@Nullable String s, @Nullable Throwable throwable) {
        log.error(s, throwable);
    }

    @Override
    protected void info0(@Nullable String s, @Nullable Throwable throwable) {
        log.info(s, throwable);
    }

    @Override
    protected void verbose0(@Nullable String s, @Nullable Throwable throwable) {
        log.info(s, throwable);
    }

    @Override
    protected void warning0(@Nullable String s, @Nullable Throwable throwable) {
        log.warn(s, throwable);
    }
}

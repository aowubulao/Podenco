package com.neoniou.bot;

import com.neoniou.bot.core.PodencoCore;
import com.neoniou.bot.message.annotation.application.BotComponent;

/**
 * @author Neo.Zzj
 * @date 2021/7/8
 */
@BotComponent(ScanPackage = "com.neoniou.bot")
public class PodencoApplication {

    public static void main(String[] args) {
        PodencoCore.run(PodencoApplication.class);
    }
}
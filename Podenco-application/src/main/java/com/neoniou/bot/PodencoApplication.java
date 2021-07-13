package com.neoniou.bot;

import com.neoniou.bot.core.PodencoCore;
import com.neoniou.bot.message.annotation.application.BotComponentScan;

/**
 * @author Neo.Zzj
 * @date 2021/7/8
 */
@BotComponentScan(ScanPackage = "com.neoniou.bot")
public class PodencoApplication {

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        PodencoCore.run(PodencoApplication.class);
    }
}

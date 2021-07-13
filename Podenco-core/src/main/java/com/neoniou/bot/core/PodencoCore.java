package com.neoniou.bot.core;

import com.neoniou.bot.config.PodencoConfig;
import com.neoniou.bot.core.log.PodencoLogger;
import com.neoniou.bot.message.AnnotationRegistrar;
import com.neoniou.bot.utils.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

/**
 * @author Neo.Zzj
 * @date 2021/7/8
 */
@Slf4j
public class PodencoCore {

    private static Bot bot;

    public static void run(Class<?> mainClass, String... args) {
        run(mainClass, new PodencoConfig(), args);
    }

    public static void run(Class<?> mainClass, PodencoConfig config, String... args) {
        //注解初始化
        AnnotationRegistrar.run(mainClass);
        //配置检查
        config.checkConfig();
        //启动Bot
        startBot(config);
        //健康检查
        if (config.isHealthCheck()) {
            ThreadUtil.createSingle().execute(() -> startHealthCheck(config));
        }
    }

    public static void startBot(PodencoConfig config) {
        bot = BotFactory.INSTANCE.newBot(config.getQq(), config.getPassword(), getBotConfiguration(config));
        bot.login();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private static void startHealthCheck(PodencoConfig config) {
        while (true) {
            ThreadUtil.sleep(config.getHealthCheckInterval());
            boolean online = bot.isOnline();
            log.info("Check bot status: [{}]", online ? "online" : "offline");
            if (!online) {
                log.info("Bot offline, restart...");
                closeBot();
                startBot(config);
            }
        }
    }

    private static BotConfiguration getBotConfiguration(PodencoConfig config) {
        BotConfiguration botConfiguration = new BotConfiguration() {
            {
                fileBasedDeviceInfo(System.getProperty("user.dir") + "/data/system/deviceInfo.json");
                setProtocol(MiraiProtocol.ANDROID_PAD);
            }
        };

        botConfiguration.setBotLoggerSupplier(bot -> config.getLogger() == null ? new PodencoLogger() : config.getLogger());
        botConfiguration.setNetworkLoggerSupplier(bot -> config.getLogger() == null ? new PodencoLogger() : config.getLogger());

        return botConfiguration;
    }

    private static void closeBot() {
        bot.close();
    }

    public static Bot getBot() {
        return bot;
    }
}

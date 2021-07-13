package com.neoniou.bot.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase;

import java.util.Scanner;
import java.util.concurrent.*;

/**
 * @author Neo.Zzj
 * @date 2021/7/9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PodencoConfig {

    public static final String CONFIG_FILE = System.getProperty("user.dir") + "/data/system/podenco.json";

    private Long qq;

    private String password;

    private boolean isHealthCheck = true;

    private long healthCheckInterval = 30 * 1000L;

    private ExecutorService threadPool;

    private MiraiLoggerPlatformBase logger;

    private void getConfig() {
        if (!FileUtil.exist(CONFIG_FILE)) {
            PodencoConfig podencoConfig = inputAccount();
            FileUtil.writeUtf8String(JSONUtil.parseObj(podencoConfig).toString(), CONFIG_FILE);
        }

        String configStr = FileUtil.readUtf8String(CONFIG_FILE);
        JSONObject configJson = JSONUtil.parseObj(configStr);

        this.qq = configJson.getLong("qq");
        this.password = configJson.getStr("password");
    }

    private PodencoConfig inputAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入QQ账号：");
        this.qq = scanner.nextLong();

        System.out.println("请输入密码：");
        this.password = scanner.next();

        return PodencoConfig
                .builder()
                .qq(this.qq)
                .password(this.password)
                .build();
    }

    public void createDefaultThreadPool() {
        this.threadPool = new ThreadPoolExecutor(8,
                10,
                5L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(6),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void checkConfig() {
        if (this.qq == null || this.password == null) {
            getConfig();
        }
        if (this.threadPool == null) {
            createDefaultThreadPool();
        }
    }
}

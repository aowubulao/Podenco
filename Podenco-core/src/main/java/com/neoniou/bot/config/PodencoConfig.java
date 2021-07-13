package com.neoniou.bot.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;

/**
 * @author Neo.Zzj
 * @date 2021/7/9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PodencoConfig {

    public static final String CONFIG_FILE = System.getProperty("user.dir") + "/data/podenco.json";

    private Long qq;

    private String password;

    private ExecutorService threadPool;

    public static PodencoConfig getConfig() {
        if (!FileUtil.exist(CONFIG_FILE)) {
            PodencoConfig podencoConfig = inputAccount();
            FileUtil.writeUtf8String(JSONUtil.parseObj(podencoConfig).toString(), CONFIG_FILE);

            return podencoConfig;
        }
        String configStr = FileUtil.readUtf8String(CONFIG_FILE);
        JSONObject configJson = JSONUtil.parseObj(configStr);

        return PodencoConfig
                .builder()
                .qq(configJson.getLong("qq"))
                .password(configJson.getStr("password"))
                .build();
    }

    private static PodencoConfig inputAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入QQ账号：");
        long qq = scanner.nextLong();

        System.out.println("请输入密码：");
        String password = scanner.next();

        return PodencoConfig
                .builder()
                .qq(qq)
                .password(password)
                .build();
    }
}

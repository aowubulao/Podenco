package com.neoniou.bot.core.authority;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.neoniou.bot.core.PodencoCore;
import lombok.Synchronized;

import java.util.*;

/**
 * @author Neo.Zzj
 * @date 2021/7/15
 */
public class AuthorityMap {

    public static long owner;

    private static JSONObject configJson;

    private static final String OWNER = "owner";
    private static final String ADMIN = "admin";

    public static final Map<Long, Integer> ADMIN_MAP = new HashMap<>(64);

    private static final String CONFIG_FILE_PATH = System.getProperty("user.dir") + "/data/system/auth.json";

    public static void initConfig() {
        if (!FileUtil.exist(CONFIG_FILE_PATH)) {
            initFile();
            return;
        }
        configJson = JSONUtil.parseObj(FileUtil.readUtf8String(CONFIG_FILE_PATH));
        owner = configJson.getLong(OWNER);

        JSONArray adminList = configJson.getJSONArray(ADMIN);
        for (int i = 0; i < adminList.size(); i++) {
            ADMIN_MAP.put(adminList.getLong(i), 0);
        }
    }

    private static void initFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入BOT拥有者QQ：");
        owner = scanner.nextLong();
        JSONObject json = new JSONObject();
        json.putOpt(OWNER, owner);
        json.putOpt(ADMIN, new ArrayList<>());

        FileUtil.writeUtf8String(json.toString(), CONFIG_FILE_PATH);
    }

    @Synchronized
    public static void addAdmin(long id) {
        if (ADMIN_MAP.containsKey(id)) {
            return;
        }

        ADMIN_MAP.put(id, 0);
        JSONArray adminList = configJson.getJSONArray(ADMIN);
        adminList.add(id);
        PodencoCore.runAsync(AuthorityMap::writeConfig);
    }

    @Synchronized
    public static void delAdmin(long id) {
        if (!ADMIN_MAP.containsKey(id)) {
            return;
        }

        ADMIN_MAP.remove(id);
        JSONArray adminList = configJson.getJSONArray(ADMIN);
        adminList.remove(id);
        PodencoCore.runAsync(AuthorityMap::writeConfig);
    }

    @Synchronized
    private static void writeConfig() {
        FileUtil.writeUtf8String(configJson.toString(), CONFIG_FILE_PATH);
    }
}

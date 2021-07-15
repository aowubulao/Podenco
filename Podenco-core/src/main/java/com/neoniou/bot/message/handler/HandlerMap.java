package com.neoniou.bot.message.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.neoniou.bot.entity.handler.HandlerClass;
import com.neoniou.bot.entity.handler.HandlerPermit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Neo.Zzj
 * @date 2021/7/9
 */
public class HandlerMap {

    private static final String CONFIG_FILE_PATH = System.getProperty("user.dir") + "/data/system/permit.json";

    public static final Map<String, String> FATHER_TO_SON_MAP = new ConcurrentHashMap<>(256);

    public static final Map<String, HandlerClass> HANDLER_MAP = new ConcurrentHashMap<>(256);

    private static final Map<String, HandlerPermit> PERMIT_MAP = new HashMap<>();

    static {
        initPermitMap();
    }

    private static void initPermitMap() {
        if (!FileUtil.exist(CONFIG_FILE_PATH)) {
            return;
        }
        String configStr = FileUtil.readUtf8String(CONFIG_FILE_PATH);
        JSONObject configJson = JSONUtil.parseObj(configStr);

        for (Map.Entry<String, Object> entry : configJson.entrySet()) {
            JSONArray permitsJson = JSONUtil.parseArray(entry.getValue());
            List<HandlerPermit> permits = JSONUtil.toList(permitsJson, HandlerPermit.class);

            for (HandlerPermit permit : permits) {
                permit.setFatherName(entry.getKey());
                PERMIT_MAP.put(permit.getName(), permit);
            }
        }
    }

    public static void put(HandlerClass handler) {
        handler.checkName();
        if (HANDLER_MAP.containsKey(handler.getName()) || FATHER_TO_SON_MAP.containsKey(handler.getFatherName())) {
            throw new RuntimeException("Handler Name重复！");
        }

        HandlerPermit permit = getPermitMap(handler);
        if (permit != null) {
            handler.setGroups(permit.getGroups());
            handler.setFriends(permit.getFriends());
        }

        FATHER_TO_SON_MAP.put(handler.getFatherName(), handler.getName());
        HANDLER_MAP.put(handler.getName(), handler);
    }

    private static HandlerPermit getPermitMap(HandlerClass handler) {
        if (PERMIT_MAP.containsKey(handler.getName())) {
            HandlerPermit permit = PERMIT_MAP.get(handler.getName());
            if (handler.getFatherName().equals(permit.getFatherName())) {
                return permit;
            }
        }
        return null;
    }
}

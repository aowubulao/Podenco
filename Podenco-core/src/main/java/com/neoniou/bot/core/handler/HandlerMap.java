package com.neoniou.bot.core.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.neoniou.bot.core.PodencoCore;
import com.neoniou.bot.core.entity.HandlerClass;
import com.neoniou.bot.core.entity.HandlerPermit;
import lombok.Synchronized;

import java.util.ArrayList;
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

    public static final Map<String, List<String>> FATHER_TO_SON_MAP = new ConcurrentHashMap<>(256);

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
        List<HandlerPermit> permits = JSONUtil.toList(configStr, HandlerPermit.class);

        for (HandlerPermit permit : permits) {
            PERMIT_MAP.put(permit.getName(), permit);
        }
    }

    public static void put(HandlerClass handler) {
        handler.checkName();
        if (HANDLER_MAP.containsKey(handler.getName())) {
            throw new RuntimeException("Handler Name重复: " + handler.getFatherName() + ":" + handler.getName());
        }

        HandlerPermit permit = getPermitMap(handler);
        if (permit != null) {
            handler.setGroups(permit.getGroups());
            handler.setFriends(permit.getFriends());
        } else {
            permit = new HandlerPermit(handler.getFatherName(), handler.getName());
            PERMIT_MAP.put(handler.getName(), permit);

            handler.setGroups(permit.getGroups());
            handler.setFriends(permit.getFriends());
        }

        List<String> children;
        if (FATHER_TO_SON_MAP.containsKey(handler.getFatherName())) {
            children = FATHER_TO_SON_MAP.get(handler.getFatherName());
        } else {
            children = new ArrayList<>();
        }
        children.add(handler.getName());
        FATHER_TO_SON_MAP.put(handler.getFatherName(), children);
        HANDLER_MAP.put(handler.getName(), handler);
    }

    @Synchronized
    public static void addPermitGroupByFather(String fatherName, long groupId) {
        List<String> children = FATHER_TO_SON_MAP.get(fatherName);
        for (String child : children) {
            addPermitGroup(child, groupId, false);
        }
        PodencoCore.runAsync(HandlerMap::writeConfig);
    }

    @Synchronized
    public static void delPermitGroupByFather(String fatherName, long groupId) {
        List<String> children = FATHER_TO_SON_MAP.get(fatherName);
        for (String child : children) {
            delPermitGroup(child, groupId, false);
        }
        PodencoCore.runAsync(HandlerMap::writeConfig);
    }

    @Synchronized
    public static void addPermitFriendByFather(String fatherName, long friendId) {
        List<String> children = FATHER_TO_SON_MAP.get(fatherName);
        for (String child : children) {
            addPermitFriend(child, friendId, false);
        }
        PodencoCore.runAsync(HandlerMap::writeConfig);
    }

    @Synchronized
    public static void delPermitFriendByFather(String fatherName, long friendId) {
        List<String> children = FATHER_TO_SON_MAP.get(fatherName);
        for (String child : children) {
            delPermitFriend(child, friendId, false);
        }
        PodencoCore.runAsync(HandlerMap::writeConfig);
    }

    @Synchronized
    public static void addPermitGroup(String handlerName, long groupId, boolean write) {
        HandlerClass handler = HANDLER_MAP.get(handlerName);

        handler.getGroups().put(groupId, 0);

        if (write) {
            PodencoCore.runAsync(HandlerMap::writeConfig);
        }
    }

    @Synchronized
    public static void delPermitGroup(String handlerName, long groupId, boolean write) {
        HandlerClass handler = HANDLER_MAP.get(handlerName);

        handler.getGroups().remove(groupId, 0);

        if (write) {
            PodencoCore.runAsync(HandlerMap::writeConfig);
        }
    }

    @Synchronized
    public static void addPermitFriend(String handlerName, long friendId, boolean write) {
        HandlerClass handler = HANDLER_MAP.get(handlerName);
        HandlerPermit handlerPermit = PERMIT_MAP.get(handlerName);

        handler.getFriends().put(friendId, 0);

        if (write) {
            PodencoCore.runAsync(HandlerMap::writeConfig);
        }
    }

    @Synchronized
    public static void delPermitFriend(String handlerName, long friendId, boolean write) {
        HandlerClass handler = HANDLER_MAP.get(handlerName);

        handler.getFriends().remove(friendId, 0);

        if (write) {
            PodencoCore.runAsync(HandlerMap::writeConfig);
        }
    }

    @Synchronized
    private static void writeConfig() {
        String configStr = JSONUtil.parseArray(new ArrayList<>(HandlerMap.PERMIT_MAP.values())).toString();
        FileUtil.writeUtf8String(configStr, CONFIG_FILE_PATH);
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

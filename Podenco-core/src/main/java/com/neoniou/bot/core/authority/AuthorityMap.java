package com.neoniou.bot.core.authority;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Neo.Zzj
 * @date 2021/7/15
 */
public class AuthorityMap {

    public static long owner;

    public static final Map<Long, Integer> ADMIN_MAP = new ConcurrentHashMap<>(64);
}

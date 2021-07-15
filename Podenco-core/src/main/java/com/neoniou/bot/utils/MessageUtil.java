package com.neoniou.bot.utils;

import net.mamoe.mirai.event.events.MessageEvent;

import java.util.ArrayList;

/**
 * @author Neo.Zzj
 * @date 2021/7/14
 */
public class MessageUtil {

    public static String getMessageBody(MessageEvent event) {
        return event.getMessage().toString();
    }
}

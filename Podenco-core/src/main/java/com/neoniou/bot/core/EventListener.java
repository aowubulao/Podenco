package com.neoniou.bot.core;

import com.neoniou.bot.core.handler.MessageRouter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * @author Neo.Zzj
 * @date 2021/7/15
 */
public class EventListener {

    public static void startListen(Bot bot) {
        listenGroup(bot);
        listenFriend(bot);
    }

    private static void listenGroup(Bot bot) {
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, e ->
            PodencoCore.runAsync(() -> MessageRouter.handleGroup(e))
        );
    }

    private static void listenFriend(Bot bot) {
        bot.getEventChannel().subscribeAlways(FriendMessageEvent.class, e ->
                PodencoCore.runAsync(() -> MessageRouter.handleFriend(e))
        );
    }
}

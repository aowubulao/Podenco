package com.neoniou.bot.handler;

import com.neoniou.bot.message.annotation.handler.BotHandler;
import com.neoniou.bot.message.annotation.handler.MessageMapping;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @author Neo.Zzj
 * @date 2021/7/8
 */
@BotHandler(name = "Sample")
public class SampleHandler {

    @MessageMapping(name = "replyHelloMapping", matchStr = "HelloWorld")
    public void replyHelloMapping(MessageEvent event) {
        event.getSubject().sendMessage("Hello World!");
    }
}

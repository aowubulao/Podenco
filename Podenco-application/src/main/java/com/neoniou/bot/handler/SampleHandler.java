package com.neoniou.bot.handler;

import com.neoniou.bot.entity.message.BotMessage;
import com.neoniou.bot.message.annotation.handler.BotHandler;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @author Neo.Zzj
 * @date 2021/7/8
 */
@BotHandler(name = "Sample")
public class SampleHandler {

    @BotHandler(name = "replyHelloMapping", matchStr = "HelloWorld")
    public void replyHelloMapping(BotMessage<MessageEvent> message) {
        message.getEvent().getSubject().sendMessage("Hello World!");
    }

    @BotHandler(name = "hello2", matchStr = "HelloWorld2")
    public void sample2(BotMessage<MessageEvent> message) {
        message.getEvent().getSubject().sendMessage("Hello World!");
    }
}

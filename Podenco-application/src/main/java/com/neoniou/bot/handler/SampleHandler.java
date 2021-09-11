package com.neoniou.bot.handler;

import com.neoniou.bot.core.annotation.handler.BotHandler;
import com.neoniou.bot.core.entity.BotMessage;
import com.neoniou.bot.utils.MessageUtil;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;

/**
 * @author Neo.Zzj
 * @date 2021/7/8
 */
@BotHandler(name = "Sample")
public class SampleHandler extends MessageUtil {

    @BotHandler(name = "replyHelloMapping", matchStr = "hello")
    public void replyHelloMapping(BotMessage<MessageEvent> message) {
        Image image = MessageUtil.uploadImage(
                "https://img.neoniou.com/blog/20210820202649.png", message.getEvent());
        sendMessage(message, image);
    }

    @BotHandler(name = "hello2", matchStr = "HelloWorld2")
    public void sample2(BotMessage<MessageEvent> message) {
        sendMessage(message, "Hello World!");
    }
}

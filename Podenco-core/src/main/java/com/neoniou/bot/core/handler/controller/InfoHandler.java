package com.neoniou.bot.core.handler.controller;

import com.neoniou.bot.consts.MessageType;
import com.neoniou.bot.consts.RoleEnum;
import com.neoniou.bot.core.annotation.handler.BotHandler;
import com.neoniou.bot.core.entity.BotMessage;
import com.neoniou.bot.core.entity.HandlerClass;
import com.neoniou.bot.core.handler.HandlerMap;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;

import java.util.List;
import java.util.Map;

/**
 * @author Neo.Zzj
 * @date 2021/7/16
 */
@Slf4j
@BotHandler(name = "sys-Handler信息")
public class InfoHandler {

    /**
     * 返回Handler List时，每个消息包含的Handler(Father)数量
     */
    private static final int MESSAGE_SIZE = 3;

    @BotHandler(
            matchStr = "/hls",
            type = {MessageType.FRIEND},
            role = RoleEnum.ADMIN,
            desc = "获取Handler"
    )
    public void getHandlerList(BotMessage<MessageEvent> message) {
        int i = 0;
        StringBuilder sb = new StringBuilder("Handler列表");
        for (Map.Entry<String, List<String>> entry : HandlerMap.FATHER_TO_SON_MAP.entrySet()) {
            sb.append("\n------\n");
            sb.append(entry.getKey()).append(":");

            for (String child : entry.getValue()) {
                sb.append("\n");
                sb.append(child);
                HandlerClass handler = HandlerMap.HANDLER_MAP.get(child);
                if (!"".equals(handler.getDesc())) {
                    sb.append("(").append(handler.getDesc()).append(")");
                }
            }

            //三个父Handler一个消息
            if (++i == MESSAGE_SIZE) {
                message.getEvent().getSubject().sendMessage(sb.toString());
                sb = new StringBuilder();
                i = 0;
            }
        }
        message.getEvent().getSubject().sendMessage(sb.toString());
    }

    @BotHandler(
            matchStr = "/info ",
            matchRule = 4,
            type = {MessageType.FRIEND},
            role = RoleEnum.ADMIN,
            desc = "获取单个Handler信息"
    )
    public void getHandlerInfo(BotMessage<MessageEvent> message) {
        String handlerName = message.getMatch();
        HandlerClass handler = HandlerMap.HANDLER_MAP.get(handlerName);

        message.getEvent().getSubject().sendMessage(handler.toString());
    }
}
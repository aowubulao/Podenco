package com.neoniou.bot.message.handler;

import com.neoniou.bot.core.PodencoCore;
import com.neoniou.bot.core.authority.AuthorityMap;
import com.neoniou.bot.entity.handler.HandlerClass;
import com.neoniou.bot.entity.message.BotMessage;
import com.neoniou.bot.entity.message.MatchReturn;
import com.neoniou.bot.message.consts.RoleEnum;
import com.neoniou.bot.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Neo.Zzj
 * @date 2021/7/15
 */
@Slf4j
public class MessageRoute {

    public static void handleGroup(GroupMessageEvent event) {
        for (Map.Entry<String, HandlerClass> entry : HandlerMap.HANDLER_MAP.entrySet()) {
            HandlerClass handler = entry.getValue();
            if (!isPermitGroup(event, handler)) {
                continue;
            }

            String messageBody = MessageUtil.getMessageBody(event);
            MatchReturn matchReturn = isMatch(messageBody, handler);
            if (matchReturn == null) {
                continue;
            }

            executeHandler(handler, matchReturn, event, messageBody);
        }
    }

    public static void handleFriend(FriendMessageEvent event) {
        for (Map.Entry<String, HandlerClass> entry : HandlerMap.HANDLER_MAP.entrySet()) {
            HandlerClass handler = entry.getValue();
            if (!isPermitFriend(event, handler)) {
                continue;
            }

            String messageBody = MessageUtil.getMessageBody(event);
            MatchReturn matchReturn = isMatch(messageBody, handler);
            if (matchReturn == null) {
                continue;
            }

            executeHandler(handler, matchReturn, event, messageBody);
        }
    }

    private static <T> void executeHandler(HandlerClass handler, MatchReturn matchReturn, T event, String messageBody) {
        Class<?> clazz = handler.getClazz();
        Method method = handler.getMethod();
        BotMessage<T> message = new BotMessage<>(event, messageBody, matchReturn.getStr());

        if (handler.isAsync()) {
            PodencoCore.runAsync(() -> execute(clazz, method, message));
        } else {
            execute(clazz, method, message);
        }
    }

    private static <T> void execute(Class<?> clazz, Method method, BotMessage<T> message) {
        try {
            method.invoke(clazz.newInstance(), message);
        } catch (Exception e) {
            log.error("Handler--{}:{} execute error: ", clazz.getName(), method.getName(), e);
        }
    }

    private static boolean isPermitGroup(GroupMessageEvent event, HandlerClass handler) {
        long groupId = event.getGroup().getId();
        long senderId = event.getSender().getId();

        RoleEnum role = handler.getRole();
        return isAuth(senderId, role) && handler.getGroups().containsKey(groupId);
    }

    private static boolean isPermitFriend(FriendMessageEvent event, HandlerClass handler) {
        long senderId = event.getFriend().getId();

        RoleEnum role = handler.getRole();
        return isAuth(senderId, role) && handler.getFriends().containsKey(senderId);
    }

    private static boolean isAuth(long senderId, RoleEnum needRole) {
        if (senderId == AuthorityMap.owner) {
            return true;
        }
        if (needRole.equals(RoleEnum.ADMIN)) {
            return AuthorityMap.ADMIN_MAP.containsKey(senderId);
        }
        return true;
    }

    private static MatchReturn isMatch(String messageBody, HandlerClass handler) {
        String matchStr = handler.getMatchStr();
        if ("".equals(matchStr)) {
            return new MatchReturn("");
        }

        int matchRule = handler.getMatchRule();

        switch (matchRule) {
            case 1:
                return matchRegex(messageBody, matchStr);
            case 2:
                return matchEqual(messageBody, matchStr);
            case 3:
                return matchContain(messageBody, matchStr);
            case 4:
                return matchStartWith(messageBody, matchStr);
            case 5:
                return matchEndWith(messageBody, matchStr);

            default:
                return null;
        }
    }

    private static MatchReturn matchEndWith(String messageBody, String matchStr) {
        if (messageBody.endsWith(matchStr)) {
            return new MatchReturn(messageBody.replace(matchStr, ""));
        } else {
            return null;
        }
    }

    private static MatchReturn matchStartWith(String messageBody, String matchStr) {
        if (messageBody.startsWith(matchStr)) {
            return new MatchReturn(messageBody.replace(matchStr, ""));
        } else {
            return null;
        }
    }

    private static MatchReturn matchContain(String messageBody, String matchStr) {
        if (messageBody.contains(matchStr)) {
            return new MatchReturn(matchStr);
        } else {
            return null;
        }
    }

    private static MatchReturn matchRegex(String messageBody, String matchStr) {
        Pattern pattern = Pattern.compile(matchStr);
        Matcher matcher = pattern.matcher(messageBody);
        if (matcher.find()) {
            return new MatchReturn(matcher.group(0));
        } else {
            return null;
        }
    }

    private static MatchReturn matchEqual(String messageBody, String matchStr) {
        if (matchStr.equals(messageBody)) {
            return new MatchReturn(messageBody);
        } else {
            return null;
        }
    }
}

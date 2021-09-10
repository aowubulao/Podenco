package com.neoniou.bot.core.handler;

import com.neoniou.bot.consts.MessageType;
import com.neoniou.bot.consts.RoleEnum;
import com.neoniou.bot.core.ClassExecutor;
import com.neoniou.bot.core.PodencoCore;
import com.neoniou.bot.core.authority.AuthorityMap;
import com.neoniou.bot.core.entity.BotMessage;
import com.neoniou.bot.core.entity.HandlerClass;
import com.neoniou.bot.core.entity.MatchReturn;
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
public class MessageRouter {

    public static void handleGroup(GroupMessageEvent event) {
        for (Map.Entry<String, HandlerClass> entry : HandlerMap.HANDLER_MAP.entrySet()) {
            HandlerClass handler = entry.getValue();
            if (!checkType(MessageType.GROUP.getId(), handler.getType())) {
                continue;
            }
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
            if (!checkType(MessageType.FRIEND.getId(), handler.getType())) {
                continue;
            }
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

    private static boolean checkType(Integer needId, MessageType[] type) {
        for (MessageType messageType : type) {
            if (messageType.getId().equals(needId)) {
                return true;
            }
        }
        return false;
    }

    private static <T> void executeHandler(HandlerClass handler, MatchReturn matchReturn, T event, String messageBody) {
        Class<?> clazz = handler.getClazz();
        Method method = handler.getMethod();
        BotMessage<T> message = new BotMessage<>(event, messageBody, matchReturn.getStr());

        if (handler.isAsync()) {
            PodencoCore.runAsync(() -> ClassExecutor.execMethod(clazz, method, message));
        } else {
            ClassExecutor.execMethod(clazz, method, message);
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
        return isAuth(senderId, role);
    }

    private static boolean isAuth(long senderId, RoleEnum needRole) {
        if (senderId == AuthorityMap.owner) {
            return true;
        } else if (needRole.equals(RoleEnum.OWNER)) {
            return false;
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

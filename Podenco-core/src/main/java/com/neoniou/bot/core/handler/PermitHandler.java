package com.neoniou.bot.core.handler;

import com.neoniou.bot.core.PodencoCore;
import com.neoniou.bot.core.authority.AuthorityMap;
import com.neoniou.bot.entity.message.BotMessage;
import com.neoniou.bot.message.annotation.handler.BotHandler;
import com.neoniou.bot.consts.MessageType;
import com.neoniou.bot.consts.RoleEnum;
import com.neoniou.bot.message.handler.HandlerMap;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Neo.Zzj
 * @date 2021/7/16
 */
@Slf4j
@BotHandler(name = "sys-权限管理")
public class PermitHandler {

    @BotHandler(
            name = "addPermitGroup",
            matchStr = "/addG ",
            matchRule = 4,
            type = {MessageType.FRIEND},
            role = RoleEnum.ADMIN,
            desc = "添加Handler的群组权限"
    )
    public void addHandlerPermitGroup(BotMessage<MessageEvent> message) {
        String match = message.getMatch();
        String[] split = match.split(" ");
        try {
            if ("all".equals(split[1])) {
                ContactList<Group> groups = PodencoCore.getBot().getGroups();
                for (Group group : groups) {
                    HandlerMap.addPermitGroup(split[0], group.getId(), false);
                }
                HandlerMap.addPermitGroup(split[0], 0L, true);
            } else {
                HandlerMap.addPermitGroup(split[0], Long.parseLong(split[1]), true);
            }
            message.getEvent().getSubject().sendMessage("添加成功！");
        } catch (Exception e) {
            log.error("添加Handler[{}]群组[{}]权限失败：", split[0], split[1], e);
            message.getEvent().getSubject().sendMessage("添加失败！");
        }
    }

    @BotHandler(
            name = "delPermitGroup",
            matchStr = "/delG ",
            matchRule = 4,
            type = {MessageType.FRIEND},
            role = RoleEnum.ADMIN,
            desc = "删除Handler的群组权限"
    )
    public void delHandlerPermitGroup(BotMessage<MessageEvent> message) {
        String match = message.getMatch();
        String[] split = match.split(" ");
        try {
            if ("all".equals(split[1])) {
                ContactList<Group> groups = PodencoCore.getBot().getGroups();
                for (Group group : groups) {
                    HandlerMap.delPermitGroup(split[0], group.getId(), false);
                }
                HandlerMap.delPermitGroup(split[0], 0L, true);
            } else {
                HandlerMap.delPermitGroup(split[0], Long.parseLong(split[1]), true);
            }
            message.getEvent().getSubject().sendMessage("删除成功！");
        } catch (Exception e) {
            log.error("删除Handler[{}]群组[{}]权限失败：", split[0], split[1], e);
            message.getEvent().getSubject().sendMessage("删除失败！");
        }
    }

    @BotHandler(
            matchStr = "/addGF ",
            matchRule = 4,
            name = "addPermitGroupFather",
            type = {MessageType.FRIEND},
            role = RoleEnum.ADMIN,
            desc = "通过父节点添加Handler的群组权限"
    )
    public void addHandlerPermitGroupFather(BotMessage<MessageEvent> message) {
        String match = message.getMatch();
        String[] split = match.split(" ");
        try {
            if ("all".equals(split[1])) {
                ContactList<Group> groups = PodencoCore.getBot().getGroups();
                for (Group group : groups) {
                    HandlerMap.addPermitGroupByFather(split[0], group.getId());
                }
            } else {
                HandlerMap.addPermitGroupByFather(split[0], Long.parseLong(split[1]));
            }
            message.getEvent().getSubject().sendMessage("添加成功！");
        } catch (Exception e) {
            log.error("添加Handler[{}]群组[{}]权限失败：", split[0], split[1], e);
            message.getEvent().getSubject().sendMessage("添加失败！");
        }
    }

    @BotHandler(
            matchStr = "/delGF ",
            matchRule = 4,
            name = "delPermitGroupFather",
            type = {MessageType.FRIEND},
            role = RoleEnum.ADMIN,
            desc = "通过父节点删除Handler的群组权限"
    )
    public void delHandlerPermitGroupFather(BotMessage<MessageEvent> message) {
        String match = message.getMatch();
        String[] split = match.split(" ");
        try {
            if ("all".equals(split[1])) {
                ContactList<Group> groups = PodencoCore.getBot().getGroups();
                for (Group group : groups) {
                    HandlerMap.delPermitGroupByFather(split[0], group.getId());
                }
            } else {
                HandlerMap.delPermitGroupByFather(split[0], Long.parseLong(split[1]));
            }
            message.getEvent().getSubject().sendMessage("删除成功！");
        } catch (Exception e) {
            log.error("删除Handler[{}]群组[{}]权限失败：", split[0], split[1], e);
            message.getEvent().getSubject().sendMessage("删除失败！");
        }
    }

    @BotHandler(
            matchStr = "/addF ",
            matchRule = 4,
            name = "addPermitFriend",
            type = {MessageType.FRIEND},
            role = RoleEnum.ADMIN,
            desc = "添加Handler的好友权限"
    )
    public void addHandlerPermitFriend(BotMessage<MessageEvent> message) {
        String match = message.getMatch();
        String[] split = match.split(" ");
        try {
            HandlerMap.addPermitFriend(split[0], Long.parseLong(split[1]), true);
            message.getEvent().getSubject().sendMessage("添加成功！");
        } catch (Exception e) {
            log.error("添加Handler[{}]好友[{}]权限失败：", split[0], split[1], e);
            message.getEvent().getSubject().sendMessage("添加失败！");
        }
    }

    @BotHandler(
            matchStr = "/delF ",
            matchRule = 4,
            name = "delPermitFriend",
            type = {MessageType.FRIEND},
            role = RoleEnum.ADMIN,
            desc = "删除Handler的好友权限"
    )
    public void delHandlerPermitFriend(BotMessage<MessageEvent> message) {
        String match = message.getMatch();
        String[] split = match.split(" ");
        try {
            HandlerMap.delPermitFriend(split[0], Long.parseLong(split[1]), true);
            message.getEvent().getSubject().sendMessage("删除成功！");
        } catch (Exception e) {
            log.error("删除Handler[{}]好友[{}]权限失败：", split[0], split[1], e);
            message.getEvent().getSubject().sendMessage("删除失败！");
        }
    }

    @BotHandler(
            matchStr = "/addFF ",
            matchRule = 4,
            name = "addPermitFriendFather",
            type = {MessageType.FRIEND},
            role = RoleEnum.ADMIN,
            desc = "通过父节点添加Handler的好友权限"
    )
    public void addHandlerPermitFriendFather(BotMessage<MessageEvent> message) {
        String match = message.getMatch();
        String[] split = match.split(" ");
        try {
            HandlerMap.addPermitFriendByFather(split[0], Long.parseLong(split[1]));
            message.getEvent().getSubject().sendMessage("添加成功！");
        } catch (Exception e) {
            log.error("添加Handler[{}]好友[{}]权限失败：", split[0], split[1], e);
            message.getEvent().getSubject().sendMessage("添加失败！");
        }
    }

    @BotHandler(
            matchStr = "/delFF ",
            matchRule = 4,
            name = "delPermitFriendFather",
            type = {MessageType.FRIEND},
            role = RoleEnum.ADMIN,
            desc = "通过父节点删除Handler的好友权限"
    )
    public void delHandlerPermitFriendFather(BotMessage<MessageEvent> message) {
        String match = message.getMatch();
        String[] split = match.split(" ");
        try {
            HandlerMap.delPermitFriendByFather(split[0], Long.parseLong(split[1]));
            message.getEvent().getSubject().sendMessage("删除成功！");
        } catch (Exception e) {
            log.error("删除Handler[{}]好友[{}]权限失败：", split[0], split[1], e);
            message.getEvent().getSubject().sendMessage("删除失败！");
        }
    }

    @BotHandler(
            matchStr = "/admin",
            name = "adminList",
            type = {MessageType.FRIEND},
            role = RoleEnum.OWNER,
            desc = "查看管理员"
    )
    public void getAdminList(BotMessage<MessageEvent> message) {
        StringBuilder sb = new StringBuilder("当前管理员有：");
        for (Long id : AuthorityMap.ADMIN_MAP.keySet()) {
            sb.append(id).append("，");
        }
        sb.deleteCharAt(sb.length() - 1);
        message.getEvent().getSubject().sendMessage(sb.toString());
    }

    @BotHandler(
            matchStr = "/addAdmin ",
            matchRule = 4,
            name = "addAdmin",
            type = {MessageType.FRIEND},
            role = RoleEnum.OWNER,
            desc = "添加管理员"
    )
    public void addAdmin(BotMessage<MessageEvent> message) {
        String match = message.getMatch();
        try {
            AuthorityMap.addAdmin(Long.parseLong(match));
            message.getEvent().getSubject().sendMessage("添加成功！");
        } catch (Exception e) {
            log.error("添加Admin[{}]失败：", match, e);
            message.getEvent().getSubject().sendMessage("添加失败！");
        }
    }

    @BotHandler(
            matchStr = "/delAdmin ",
            matchRule = 4,
            name = "delAdmin",
            type = {MessageType.FRIEND},
            role = RoleEnum.OWNER,
            desc = "删除管理员"
    )
    public void delAdmin(BotMessage<MessageEvent> message) {
        String match = message.getMatch();
        try {
            AuthorityMap.delAdmin(Long.parseLong(match));
            message.getEvent().getSubject().sendMessage("删除成功！");
        } catch (Exception e) {
            log.error("删除Admin[{}]失败：", match, e);
            message.getEvent().getSubject().sendMessage("删除失败！");
        }
    }
}

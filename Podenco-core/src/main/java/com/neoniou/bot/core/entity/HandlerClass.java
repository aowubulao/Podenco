package com.neoniou.bot.core.entity;

import com.neoniou.bot.consts.MessageType;
import com.neoniou.bot.consts.RoleEnum;
import com.neoniou.bot.core.annotation.handler.BotHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Neo.Zzj
 * @date 2021/7/13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandlerClass {

    private Class<?> clazz;

    private Method method;

    private String name;

    private String desc;

    private RoleEnum role;

    private MessageType[] type;

    private String matchStr;

    private int matchRule;

    private boolean async;

    private String fatherName;

    private Map<Long, Integer> groups;

    private Map<Long, Integer> friends;

    public void checkName() {
        if ("".equals(this.name)) {
            this.name = this.method.getName();
        }

        this.fatherName = clazz.getAnnotation(BotHandler.class).name();
        if ("".equals(this.fatherName)) {
            this.fatherName = clazz.getName();
        }

        if (this.fatherName.equals(this.name)) {
            throw new RuntimeException("Handler Name重复！");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Handler-").append(name).append("：");
        if (!"".equals(desc)) {
            sb.append("\ndesc: ").append(desc);
        }
        sb.append("\nrole: ").append(role.getRoleName());
        sb.append("\ntype: ").append(Arrays.toString(type));
        sb.append("\nmatchStr: ").append(matchStr);
        sb.append("\nmatchRule: ").append(matchRule);
        sb.append("\ngroups: ").append(groups.size() == 0 ? "无" : groups);
        sb.append("\nfriends: ").append(friends.size() == 0 ? "无" : friends);

        return sb.toString();
    }
}

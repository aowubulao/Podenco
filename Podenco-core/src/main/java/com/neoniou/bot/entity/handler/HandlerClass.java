package com.neoniou.bot.entity.handler;

import com.neoniou.bot.message.annotation.handler.BotHandler;
import com.neoniou.bot.message.consts.MessageType;
import com.neoniou.bot.message.consts.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
}
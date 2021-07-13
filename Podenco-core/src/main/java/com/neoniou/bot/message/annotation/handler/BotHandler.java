package com.neoniou.bot.message.annotation.handler;

import com.neoniou.bot.message.consts.MessageType;
import com.neoniou.bot.message.consts.RoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Neo.Zzj
 * @date 2021/7/8
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BotHandler {

    /**
     * MessageMapping名称，不可重复，指定后可以动态开关接口
     *
     * @return String
     */
    String name() default "";

    /**
     * 访问权限
     * 默认所有人/除陌生人外 会回复
     *
     * @return RoleEnum
     */
    RoleEnum role() default RoleEnum.GUEST;

    /**
     * 接收的消息类型
     * 默认接收好友和群组消息
     */
    MessageType[] type() default {MessageType.GROUP, MessageType.FRIEND};

    /**
     * 匹配此方法的字符串
     *
     * @return String
     */
    String matchStr() default "";

    /**
     * 字符串的匹配方式
     * 1：正则匹配
     * 2：相等匹配
     * 3：包含匹配
     *
     * @return String
     */
    int matchRule() default 2;
}

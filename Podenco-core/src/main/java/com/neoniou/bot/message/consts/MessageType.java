package com.neoniou.bot.message.consts;

import lombok.Getter;

/**
 * @author Neo.Zzj
 * @date 2021/7/8
 */
@Getter
public enum MessageType {

    /**
     * 消息类型
     */
    GROUP(1, "group"),

    FRIEND(2, "friend"),

    STRANGER(3, "stranger")
    ;

    private final Integer id;

    private final String name;

    MessageType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}

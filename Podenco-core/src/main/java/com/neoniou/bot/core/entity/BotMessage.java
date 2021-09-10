package com.neoniou.bot.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Neo.Zzj
 * @date 2021/7/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotMessage<T> {

    private T event;

    private String message;

    private String match;

}

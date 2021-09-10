package com.neoniou.bot.exception;

/**
 * @author Neo.Zzj
 * @date 2021/9/10
 */
public class HandlerDuplicationException extends RuntimeException {

    public HandlerDuplicationException(String message) {
        super(message);
    }
}

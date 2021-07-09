package com.neoniou.bot.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ExecutorService;

/**
 * @author Neo.Zzj
 * @date 2021/7/9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PodencoConfig {

    private Long qq;

    private String password;

    private ExecutorService threadPool;


}

package com.neoniou.bot.entity.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Neo.Zzj
 * @date 2021/7/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandlerPermit {

    private String name;

    private String fatherName;

    private Map<Long, Integer> groups;

    private Map<Long, Integer> friends;
}

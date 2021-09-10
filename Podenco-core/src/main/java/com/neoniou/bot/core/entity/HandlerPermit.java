package com.neoniou.bot.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
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

    private Map<Long, Integer> groups = new HashMap<>();

    private Map<Long, Integer> friends = new HashMap<>();

    public HandlerPermit(String fatherName, String name) {
        this.name = name;
        this.fatherName = fatherName;
    }
}

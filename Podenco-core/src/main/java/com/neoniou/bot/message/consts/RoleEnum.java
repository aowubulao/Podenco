package com.neoniou.bot.message.consts;

import lombok.Getter;

/**
 * @author Neo.Zzj
 * @date 2021/7/8
 */
@Getter
public enum RoleEnum {

    /**
     * 权限
     */
    OWNER(0, "owner"),

    ADMIN(1, "admin"),

    FRIEND(2, "friend"),

    GUEST(3, "guest")
    ;

    private final Integer roleId;

    private final String roleName;

    RoleEnum(Integer roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
}

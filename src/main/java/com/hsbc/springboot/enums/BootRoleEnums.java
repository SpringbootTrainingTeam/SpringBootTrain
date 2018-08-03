package com.hsbc.springboot.enums;

import lombok.Getter;

public enum  BootRoleEnums {

    CUSTOMER("visitor"),
    SYSTEM_USER("system user"),
    ADMIN("administrator");

    BootRoleEnums(String desc) {
        this.desc = desc;
    }
    @Getter
    private String desc;
}

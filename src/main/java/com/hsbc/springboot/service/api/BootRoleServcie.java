package com.hsbc.springboot.service.api;

import com.hsbc.springboot.pojo.entity.BootRole;

import java.util.List;

public interface BootRoleServcie {

    /**
     * find default role by role id
     *
     * @return default roles
     */
    List<BootRole> findDefaultRoles();
}

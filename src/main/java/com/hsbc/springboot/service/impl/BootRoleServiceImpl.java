package com.hsbc.springboot.service.impl;

import com.hsbc.springboot.dao.RoleRepository;
import com.hsbc.springboot.pojo.entity.BootRole;
import com.hsbc.springboot.service.api.BootRoleServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BootRoleServiceImpl implements BootRoleServcie {

    private final RoleRepository roleRepository;

    @Autowired
    public BootRoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<BootRole> findDefaultRoles() {
        List<BootRole> roles = new ArrayList<>(1);
        Optional<BootRole> defaultRole = roleRepository.findById(1);
        BootRole bootRole = defaultRole.get();
        roles.add(bootRole);
        return roles;
    }
}

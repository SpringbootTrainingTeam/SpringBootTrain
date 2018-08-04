package com.hsbc.springboot.service.impl;

import com.hsbc.springboot.dao.UserRepository;
import com.hsbc.springboot.pojo.entity.BootRole;
import com.hsbc.springboot.pojo.entity.BootUser;
import com.hsbc.springboot.pojo.form.BootUserForm;
import com.hsbc.springboot.service.api.BootRoleServcie;
import com.hsbc.springboot.service.api.BootUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BootUserServiceImpl implements BootUserService {

    private final UserRepository userRepository;

    private final BootRoleServcie bootRoleServcie;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public BootUserServiceImpl(UserRepository userRepository, BootRoleServcie bootRoleServcie, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bootRoleServcie = bootRoleServcie;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @see BootUserService#register(BootUserForm)
     */
    @Override
    public Integer register(BootUserForm bootUserForm) {
        String password = passwordEncoder.encode(bootUserForm.getPassword());
        BootUser bootUser = new BootUser();
        bootUser.setPassword(password);
        bootUser.setUsername(bootUserForm.getUsername());
        bootUser.setCreateTime(new Date());
        List<BootRole> roles = bootRoleServcie.findDefaultRoles();
        bootUser.setRoles(roles);
        return userRepository.save(bootUser).getId();
    }

    @Override
    public BootUser findUserById(Integer id) {
        return userRepository.findById(id).get();
    }
}

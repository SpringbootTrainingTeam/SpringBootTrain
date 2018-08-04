package com.hsbc.springboot.config;

import com.hsbc.springboot.dao.UserRepository;
import com.hsbc.springboot.pojo.entity.AuthUser;
import com.hsbc.springboot.pojo.entity.BootRole;
import com.hsbc.springboot.pojo.entity.BootUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class BootUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public BootUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = new AuthUser();
        BootUser bootUser = userRepository.findByUsername(username);
        BeanUtils.copyProperties(bootUser, authUser);
        return authUser;
    }
}

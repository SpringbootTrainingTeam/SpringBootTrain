package com.hsbc.springboot.dao;

import com.hsbc.springboot.pojo.entity.BootUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Cteate by IntelliJ IDEA
 * Author:Demon Q H Chen
 * Date:2018/8/2
 * Time:22:52
 */
@Repository
public interface BootUserRepositoty extends JpaRepository<BootUser,Short> {

    BootUser findByUsername(String username);
}

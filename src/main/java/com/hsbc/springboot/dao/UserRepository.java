package com.hsbc.springboot.dao;

import com.hsbc.springboot.pojo.entity.BootUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends JpaRepository<BootUser, Short>, PagingAndSortingRepository<BootUser, Short> {

    @Override
    <S extends BootUser> S save(S s);

    BootUser findByUsername(String username);
}


package com.hsbc.springboot.dao;

import com.hsbc.springboot.pojo.entity.BootUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<BootUser, Integer>, PagingAndSortingRepository<BootUser, Integer> {


    @Override
    <S extends BootUser> S save(S s);

    BootUser findByUsername(String username);
}


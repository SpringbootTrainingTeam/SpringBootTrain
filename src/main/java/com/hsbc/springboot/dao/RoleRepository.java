package com.hsbc.springboot.dao;

import com.hsbc.springboot.pojo.entity.BootRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoleRepository extends JpaRepository<BootRole, Integer>, PagingAndSortingRepository<BootRole, Integer> {
    @Override
    <S extends BootRole> S save(S s);
}

package com.hsbc.springboot.dao;

import com.hsbc.springboot.pojo.entity.BootFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends JpaRepository<BootFile, Long>, PagingAndSortingRepository<BootFile, Long> {

    @Override
    <S extends BootFile> S save(S s);

    @Override
    void delete(BootFile bootFile);

    @Override
    void deleteById(Long aLong);
}

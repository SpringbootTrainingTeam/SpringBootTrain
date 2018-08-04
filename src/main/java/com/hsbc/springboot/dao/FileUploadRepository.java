package com.hsbc.springboot.dao;

import com.hsbc.springboot.pojo.entity.BootFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface FileUploadRepository extends JpaRepository<BootFile, Long>, PagingAndSortingRepository<BootFile, Long> {

    @Override
    <S extends BootFile> S save(S s);

    @Override
    void delete(BootFile bootFile);

    @Override
    void deleteById(Long aLong);

    @Query(value = "select * from boot_file bf where bf.user_id = ?1", nativeQuery = true)
    List<BootFile> findByUserId(Integer userId);
}

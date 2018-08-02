package com.hsbc.springboot.service.api;

import com.hsbc.springboot.pojo.dto.FileDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

    /**
     * File upload
     *
     * @param file file
     * @return file name
     */
    String storeFile(MultipartFile file);

    /**
     * File download
     *
     * @param fileName file name
     * @return File resource
     */
    Resource loadFileAsResource(String fileName);

    /**
     * find file list by user id
     *
     * @param userId user id
     * @return user's file list
     */
    List<FileDTO> fileList(Short userId);

    /**
     * delete file by file id
     *
     * @param id file id
     */
    void deleteFileById(Long id);
}

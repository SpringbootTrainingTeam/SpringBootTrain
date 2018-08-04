package com.hsbc.springboot.service.api;

import com.hsbc.springboot.pojo.dto.FileDTO;
import com.hsbc.springboot.pojo.entity.BootFile;
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
     *  File show
     * @return
     */
    List<FileDTO> fileListbyUserId();

    /**
     * delete file by file id
     *
     * @param id file id
     */
    void deleteFileById(Long id);
}

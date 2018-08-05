package com.hsbc.springboot.controller;

import com.hsbc.springboot.pojo.dto.FileDTO;
import com.hsbc.springboot.pojo.vo.UploadFileResponse;
import com.hsbc.springboot.service.api.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class FileUploadController {

    private final FileUploadService fileStorageService ;

    @Autowired
    public FileUploadController(FileUploadService fileStorageService, FileUploadService fileUploadService) {
        this.fileStorageService = fileStorageService;
        this.fileUploadService = fileUploadService;
    }

    private final FileUploadService fileUploadService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    public void uploadFile(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        try {
            fileStorageService.storeFile(file);
            response.sendRedirect("/repository");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/uploadMultipleFiles")
    public void uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, HttpServletResponse response) {

        try {
            for (MultipartFile file : files) {
                fileStorageService.storeFile(file);
            }
            response.sendRedirect("/repository");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/findAll")
    @ResponseBody
    public List<FileDTO> fileListbyUserId(){
        List<FileDTO> fileDTOS = fileStorageService.fileListbyUserId();
        return fileDTOS;
    }


    @DeleteMapping("/delete/{fileId}")
    public void deleteFileById(@PathVariable Long fileId){
        fileUploadService.deleteFileById(fileId);
    }
}

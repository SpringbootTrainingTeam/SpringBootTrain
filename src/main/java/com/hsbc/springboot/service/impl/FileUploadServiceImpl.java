package com.hsbc.springboot.service.impl;

import com.hsbc.springboot.config.FileStorageProperties;
import com.hsbc.springboot.dao.FileUploadRepository;
import com.hsbc.springboot.exception.FileStorageException;
import com.hsbc.springboot.exception.MyFileNotFoundException;
import com.hsbc.springboot.pojo.dto.FileDTO;
import com.hsbc.springboot.pojo.entity.BootFile;
import com.hsbc.springboot.pojo.entity.AuthUser;
import com.hsbc.springboot.service.api.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Path fileStorageLocation;

    private final FileStorageProperties fileStorageProperties;

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Autowired
    public FileUploadServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }

        this.fileStorageProperties = fileStorageProperties;
    }

    /**
     * @see FileUploadService#storeFile(MultipartFile)
     */
    @Override
    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            BootFile bootFile = new BootFile();
            bootFile.setName(fileName);
            bootFile.setPath(targetLocation.toString());
            bootFile.setUploadTime(new Date());

            AuthUser authUser  = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            bootFile.setUserId(authUser.getId());

            fileUploadRepository.save(bootFile);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * @see FileUploadService#loadFileAsResource(String)
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    @Override
    public List<FileDTO> fileListbyUserId() {
        AuthUser authUser  = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<BootFile> bootFiles = fileUploadRepository.findByUserId(authUser.getId());
        ArrayList<FileDTO> fileDTOS = new ArrayList<>();
        bootFiles.stream().forEach(bootFile -> {
            FileDTO fileDTO = new FileDTO();
            fileDTO.setId(bootFile.getId());
            fileDTO.setName(bootFile.getName());
            fileDTO.setPath(bootFile.getPath());
            fileDTO.setUploadTime(bootFile.getUploadTime());
            fileDTO.setUserId(bootFile.getUserId());
            fileDTOS.add(fileDTO);
        });

        return fileDTOS;
    }

    /**
     *  delete file by FileId
     * @param id file id
     */
    @Override
    public void deleteFileById(Long id) {
        fileUploadRepository.deleteById(id);
    }
}

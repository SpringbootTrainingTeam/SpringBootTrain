package com.hsbc.springboot.service.impl;

import com.hsbc.springboot.config.FileStorageProperties;
import com.hsbc.springboot.dao.FileUploadRepository;
import com.hsbc.springboot.dao.UserRepository;
import com.hsbc.springboot.exception.FileStorageException;
import com.hsbc.springboot.exception.MyFileNotFoundException;
import com.hsbc.springboot.pojo.entity.BootFile;
import com.hsbc.springboot.pojo.entity.BootUser;
import com.hsbc.springboot.service.api.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Example;
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
import java.util.List;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FileUploadRepository fileUploadRepository;

    private final Path fileStorageLocation;

    private final FileStorageProperties fileStorageProperties;

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

    @Autowired
    private UserRepository userRepository;

    /**
     *  findAll BootFile where userId = userId
     * @return
     */
    @Override
    public List<BootFile> fileList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        BootUser bootUser = userRepository.findByUsername(username);
        BootFile bootFile = new BootFile();
        bootFile.setUserId(bootUser.getId());
        Example<BootFile> example = Example.of(bootFile);
        List<BootFile> bootFiles = fileUploadRepository.findAll( example);
        return bootFiles;
    }

    @Override
    public void deleteFileById(Long id) {

    }
}

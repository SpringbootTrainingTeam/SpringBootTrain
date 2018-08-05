package com.hsbc.springboot.service.impl;

import com.hsbc.springboot.config.FileStorageProperties;
import com.hsbc.springboot.dao.FileUploadRepository;
import com.hsbc.springboot.exception.FileStorageException;
import com.hsbc.springboot.exception.MyFileNotFoundException;
import com.hsbc.springboot.pojo.dto.FileDTO;
import com.hsbc.springboot.pojo.entity.BootFile;
import com.hsbc.springboot.pojo.entity.AuthUser;
import com.hsbc.springboot.pojo.entity.NodeEntity;
import com.hsbc.springboot.service.api.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Path fileStorageLocation;

    private final FileStorageProperties fileStorageProperties;

    private final FileUploadRepository fileUploadRepository;

    AuthUser authUser = null;

    String currentUsername = null;

    @Autowired
    public FileUploadServiceImpl(FileStorageProperties fileStorageProperties, FileUploadRepository fileUploadRepository) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
//        try {
//            Files.createDirectories(this.fileStorageLocation);
//        } catch (Exception ex) {
//            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
//        }

        this.fileStorageProperties = fileStorageProperties;
        this.fileUploadRepository = fileUploadRepository;
    }

    private void getAuthUser() {
        SecurityContext context = SecurityContextHolder.getContext();

        if (context.getAuthentication() != null) {
            authUser = (AuthUser) context.getAuthentication().getPrincipal();
            currentUsername = authUser.getUsername();
        }

    }

    /**
     * @see FileUploadService#storeFile(MultipartFile)
     */
    @Override
    @Transactional(propagation = REQUIRED)
    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        getAuthUser();

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            if (currentUsername == null) {
                throw new FileStorageException("sorry! Current User invalid" + currentUsername);
            }

            try {
                Files.createDirectories(this.fileStorageLocation.resolve(currentUsername));
            } catch (IOException ex) {
                throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(currentUsername).resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            BootFile bootFile = new BootFile();
            bootFile.setName(fileName);
            bootFile.setPath(targetLocation.toString());
            bootFile.setUploadTime(new Date());

            if (authUser != null) {
                bootFile.setUserId(authUser.getId());
            }

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
        getAuthUser();
        try {
            Path filePath = this.fileStorageLocation.resolve(currentUsername).resolve(fileName).normalize();
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
    public List<FileDTO> fileListByUserId() {
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

    /**
     * @see FileUploadService#findDocumentListByUsername()
     */
    @Override
    public List<NodeEntity> findDocumentListByUsername() throws IllegalAccessException {

        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = authUser.getUsername();
        if (username == null) {
            throw new IllegalAccessException("Illegal login status!");
        }

        File userRoot = new File(fileStorageProperties.getUploadDir(), username);
        List<NodeEntity> nodes = new ArrayList<>(16);
        return getNodesByUsername(nodes, userRoot);
    }

    private List<NodeEntity> getNodesByUsername(List<NodeEntity> nodes, File userRoot) {

        if (!userRoot.exists()) {
            return nodes;
        }
        String rootName = userRoot.getName();
        if (userRoot.isFile()) {
            NodeEntity node = new NodeEntity(userRoot.getPath(), userRoot.getParentFile().getPath(), rootName, false, null, null, null, false, null, true);
            nodes.add(node);
            return nodes;
        }

        NodeEntity node = new NodeEntity(userRoot.getPath(), userRoot.getParentFile().getPath(), rootName, false, null, null, null, true, null, true);
        nodes.add(node);
        File[] files = userRoot.listFiles();
        if (files == null || files.length == 0) {
            return nodes;
        }
        for (File file : files) {
            getNodesByUsername(nodes, file);
        }
        return nodes;
    }

    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }
}

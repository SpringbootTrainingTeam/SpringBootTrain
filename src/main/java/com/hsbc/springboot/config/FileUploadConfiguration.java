package com.hsbc.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.MultipartConfigElement;

@Slf4j
@Configuration
public class FileUploadConfiguration {

    private final BootSystemProperties bootSystemProperties;

    @Autowired
    public FileUploadConfiguration(BootSystemProperties bootSystemProperties) {
        this.bootSystemProperties = bootSystemProperties;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
        multipartConfigFactory.setMaxFileSize(-1L);
        multipartConfigFactory.setMaxRequestSize(-1L);
        multipartConfigFactory.setLocation(bootSystemProperties.getUploadFileBaseDir().getPath());
        return multipartConfigFactory.createMultipartConfig();
    }
}

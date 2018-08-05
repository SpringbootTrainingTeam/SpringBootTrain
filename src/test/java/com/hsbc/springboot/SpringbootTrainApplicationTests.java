package com.hsbc.springboot;

import com.hsbc.springboot.controller.FileUploadController;
import com.hsbc.springboot.pojo.entity.AuthUser;
import com.hsbc.springboot.pojo.entity.BootRole;
import com.hsbc.springboot.service.api.FileUploadService;
import com.hsbc.springboot.service.impl.FileUploadServiceImpl;
import org.apache.catalina.webresources.FileResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootTrainApplicationTests {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    private FileUploadServiceImpl fileStorageService;


    public AuthUser authUser;

    @Before
    public void setup() {

        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }

    @Test
    public void testUpload() throws Exception {
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        Path folder = fileStorageService.getFileStorageLocation();

        String fileName = "test.txt";
        File file = new File(folder.resolve(fileName).toString());
        //delete if exits
        file.delete();
        System.out.println(folder.resolve(fileName).toString());

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                fileName, "text/plain", "text body".getBytes());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/uploadFile")
                .file(mockMultipartFile);

        this.mockMvc.perform(builder).andExpect(ok)
                .andDo(MockMvcResultHandlers.print());
        Assert.assertTrue(file.exists());

    }

    @Test
    public void contextLoads() {
    }

}

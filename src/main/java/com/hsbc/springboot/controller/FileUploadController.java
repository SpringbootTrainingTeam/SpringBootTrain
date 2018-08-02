package com.hsbc.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileUploadController {

    @GetMapping("/")
    public String indexUI() {
        return "Hello World";
    }
}

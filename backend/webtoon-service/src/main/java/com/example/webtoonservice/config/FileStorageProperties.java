package com.example.webtoonservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//빈으로 등록
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "file") //application.properties에 선언한 file.upload-dir에 접근
public class FileStorageProperties {
    private String uploadDir;

    public String getUploadDir(){
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
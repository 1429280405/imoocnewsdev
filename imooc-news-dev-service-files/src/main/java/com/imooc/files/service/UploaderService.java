package com.imooc.files.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author liujq
 * @create 2021-08-23 18:10
 */
public interface UploaderService {

    String uploadFdfs(MultipartFile file, String suffix) throws IOException;

}

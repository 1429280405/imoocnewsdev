package com.imooc.files.controller;

import com.imooc.api.controller.files.FileUploaderControllerApi;
import com.imooc.files.service.UploaderService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author liujq
 * @create 2021-08-23 17:54
 */
@RestController
@Slf4j
public class FileUploaderController implements FileUploaderControllerApi {

    @Autowired
    private UploaderService uploaderService;

    @Override
    public GraceJSONResult uploadFace(String userId, MultipartFile file) throws Exception {
        String path = "";
        if (file == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        //获得文件上传名称
        String filename = file.getOriginalFilename();
        //判断文件名不能为空
        if (StringUtils.isBlank(filename)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
        }
        //获得后缀
        String suffix = filename.substring(filename.indexOf("."), filename.length());
        if (!suffix.equalsIgnoreCase("png") &&
                !suffix.equalsIgnoreCase("jpg") &&
                !suffix.equalsIgnoreCase("jpeg")) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
        }
        //执行上传
        path = uploaderService.uploadFdfs(file, suffix);
        log.info("path:{}", path);
        return GraceJSONResult.ok(path);
    }
}

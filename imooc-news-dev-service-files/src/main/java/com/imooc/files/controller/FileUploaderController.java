package com.imooc.files.controller;

import com.imooc.api.controller.files.FileUploaderControllerApi;
import com.imooc.exception.GraceException;
import com.imooc.files.resource.FileResource;
import com.imooc.files.service.UploaderService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.FileUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author liujq
 * @create 2021-08-23 17:54
 */
@RestController
@Slf4j
public class FileUploaderController implements FileUploaderControllerApi {

    @Autowired
    private UploaderService uploaderService;

    @Autowired
    private FileResource fileResource;

    @Autowired
    private GridFSBucket gridFSBucket;

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
        String suffix = filename.substring(filename.indexOf(".") + 1, filename.length());
        if (!suffix.equalsIgnoreCase("png") &&
                !suffix.equalsIgnoreCase("jpg") &&
                !suffix.equalsIgnoreCase("jpeg")) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
        }
        //执行上传
        path = uploaderService.uploadFdfs(file, suffix);
        log.info("path:{}", path);
        if (StringUtils.isBlank(path)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        String finalPath = fileResource.getHost() + path;

        return GraceJSONResult.ok(finalPath);
    }

    @Override
    public GraceJSONResult uploadToGridFS(NewAdminBO newAdminBO) throws Exception {
        //获得图片的base64字符串
        String img64 = newAdminBO.getImg64();
        //将base64字符串转换为byte数组
        byte[] bytes = new BASE64Decoder().decodeBuffer(img64.trim());
        //转换为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        //上传到gridfs中
        ObjectId fileId = gridFSBucket.uploadFromStream(newAdminBO.getUsername() + ".png", inputStream);

        //获得文件在gridfs中的主键id
        String fileIdStr = fileId.toString();

        return GraceJSONResult.ok(fileIdStr);
    }

    @Override
    public void readInGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        //1、判断参数
        if (StringUtils.isBlank(faceId) || faceId.equals("null")) {
            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        //从gridfs中读取
        File adminface = readGridFSByFaceId(faceId);
        //把人脸图片输出到浏览器
        FileUtils.downloadFileByStream(response, adminface);
    }

    @Override
    public GraceJSONResult readFace64InGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获得人脸文件
        File file = readGridFSByFaceId(faceId);
        
        //转换为base64
        String base64Face = FileUtils.fileToBase64(file);
        return GraceJSONResult.ok(base64Face);
    }

    private File readGridFSByFaceId(String faceId) throws FileNotFoundException {
        GridFSFindIterable gridFSFiles = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId)));
        GridFSFile gridFS = gridFSFiles.first();
        if (gridFS == null) {
            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        String filename = gridFS.getFilename();
        log.info("filename:{}", filename);
        //获取文件流，保存文件到本地或者服务器的临时目录
        File file = new File("D:/temp_face");
        if (!file.exists()) {
            file.mkdirs();
        }
        File myFile = new File("D:/temp_face" + filename);
        //创建文件输出流
        FileOutputStream outputStream = new FileOutputStream(myFile);
        //下载到服务器或者本地
        gridFSBucket.downloadToStream(new ObjectId(faceId), outputStream);
        return myFile;
    }

}

package com.imooc.api.controller.files;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.pojo.bo.RegistLoginBO;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author liujinqiang
 * @create 2021-08-17 23:01
 */
@Api(value = "文件上传Controller", tags = {"文件上传Controller"})
@RequestMapping("fs")
public interface FileUploaderControllerApi {


    @ApiOperation(value = "上传单文件", notes = "上传单文件", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public GraceJSONResult uploadFace(@RequestParam String userId,
                                      MultipartFile file) throws Exception;


    /**
     * 文件上传至mongodb的gridfs中
     * @param newAdminBO
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadToGridFS")
    public GraceJSONResult uploadToGridFS(@RequestBody NewAdminBO newAdminBO) throws Exception;



}

package com.imooc.articleHtml.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticleHTMLControllerApi;
import com.mongodb.client.gridfs.GridFSBucket;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author liujinqiang
 * @create 2021-09-07 22:58
 */
@Slf4j
@RestController
public class ArticleHTMLController extends BaseController implements ArticleHTMLControllerApi {

    @Autowired
    private GridFSBucket gridFSBucket;

    @Value("${freemarker.html.article}")
    private String articlePath;

    @Override
    public Integer download(String articleId, String articleMongoId) throws Exception {
        //拼接最终文件的保存地址
        String path = articlePath + File.separator + articleId + ".html";
        //获取文件流
        File file = new File(path);
        //创建输出流
        FileOutputStream outputStream = new FileOutputStream(file);
        //执行下载
        gridFSBucket.downloadToStream(new ObjectId(articleMongoId), outputStream);
        return HttpStatus.OK.value();
    }
}

package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticleControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleCoverType;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liujinqiang
 * @create 2021-08-30 22:52
 */
@RestController
@Slf4j
public class ArticleController extends BaseController implements ArticleControllerApi {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${freemarker.html.article}")
    private String articlePath;

    @Override
    public GraceJSONResult createArticle(@Valid NewArticleBO newArticleBO, BindingResult result) {
        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return GraceJSONResult.errorMap(errorMap);
        }

        //判断文章封面类型，单图必填，纯文字则设置为空
        if (newArticleBO.getArticleType() == ArticleCoverType.ONE_IMAGE.type && StringUtils.isBlank(newArticleBO.getArticleCover())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
        } else if (newArticleBO.getArticleType() == ArticleCoverType.WORDS.type) {
            newArticleBO.setArticleCover("");
        }
        //判断分类id是否存在
        Category temp = null;//记录是否存在
        String categoryJsons = redis.get(REDIS_ALL_CATEGORY);
        if (StringUtils.isBlank(categoryJsons)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        List<Category> categories = JsonUtils.jsonToList(categoryJsons, Category.class);
        for (Category category : categories) {
            if (newArticleBO.getCategoryId() == category.getId()) {
                temp = category;
                break;
            }
        }
        if (temp == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
        }
        articleService.createArticle(newArticleBO, temp);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryAllList(Integer status, Integer page, Integer pageSize) {
        PagedGridResult gridResult = articleService.queryAllList(status, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult queryMyList(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_QUERY_PARAMS_ERROR);
        }

        // 查询我的列表，调用service
        PagedGridResult grid = articleService.queryMyArticleList(userId,
                keyword,
                status,
                startDate,
                endDate,
                page,
                pageSize);

        return GraceJSONResult.ok(grid);
    }

    @Override
    public GraceJSONResult doReview(String articleId, String passOrNot) {
        Integer status = null;
        if (passOrNot.equals("1")) {
            //审核通过
            status = ArticleReviewStatus.SUCCESS.type;
        } else if (passOrNot.equals("0")) {
            //审核失败
            status = ArticleReviewStatus.FAILED.type;
        } else {
            //非法状态
            GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
        articleService.updateArticleStatus(articleId, status);
        if (status == ArticleReviewStatus.SUCCESS.type) {
            //审核通过，生成静态文章详情页
            try {
                createArticleHTMLToGridFS(articleId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
        return GraceJSONResult.ok();
    }

    private void createArticleHTMLToGridFS(String articleId) throws IOException, TemplateException {

        Configuration cfg = new Configuration(Configuration.getVersion());
        String classpath = this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File(classpath + "templates"));

        Template template = cfg.getTemplate("detail.ftl", "utf-8");

        // 获得文章的详情数据
        ArticleDetailVO detailVO = getArticleDetail(articleId);
        Map<String, Object> map = new HashMap<>();
        map.put("articleDetail", detailVO);

        //融合动态数据和ftl生成html
        File file = new File(articlePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileWriter out = new FileWriter(articlePath + File.separator + detailVO.getId() + ".html");
        template.process(map, out);
        out.close();

    }


    // 发起远程调用rest，获得文章详情数据
    public ArticleDetailVO getArticleDetail(String articleId) {
        String url
                = "http://www.imoocnews.com:8001/portal/article/detail?articleId=" + articleId;
        ResponseEntity<GraceJSONResult> responseEntity
                = restTemplate.getForEntity(url, GraceJSONResult.class);
        GraceJSONResult bodyResult = responseEntity.getBody();
        ArticleDetailVO detailVO = null;
        if (bodyResult.getStatus() == 200) {
            String detailJson = JsonUtils.objectToJson(bodyResult.getData());
            detailVO = JsonUtils.jsonToPojo(detailJson, ArticleDetailVO.class);
        }
        return detailVO;
    }

    @Override
    public GraceJSONResult delete(String articleId, String userId) {
        articleService.deleteArticle(articleId, userId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult withdraw(String articleId, String userId) {
        articleService.withdraw(articleId, userId);
        return GraceJSONResult.ok();
    }
}

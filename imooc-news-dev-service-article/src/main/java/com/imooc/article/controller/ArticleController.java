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
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
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
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult delete(String articleId, String userId) {
        articleService.deleteArticle(articleId,userId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult withdraw(String articleId, String userId) {
        articleService.withdraw(articleId,userId);
        return GraceJSONResult.ok();
    }
}
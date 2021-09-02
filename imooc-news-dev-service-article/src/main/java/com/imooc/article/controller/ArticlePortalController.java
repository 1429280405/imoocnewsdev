package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticlePortalControllerApi;
import com.imooc.article.service.ArticlePortalService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.Article;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.pojo.vo.IndexArticleVO;
import com.imooc.utils.IPUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liujq
 * @create 2021-09-01 10:35
 */
@RestController
@Slf4j
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {

    @Autowired
    private ArticlePortalService articlePortalService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GraceJSONResult createArticle(String keyword, Integer category, Integer page, Integer pageSize) {
        PagedGridResult gridResult = articlePortalService.queryIndexArticleList(keyword,
                category,
                page,
                pageSize);

        gridResult = rebuildArticleGrid(gridResult);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult hotList() {
        List<Article> articles = articlePortalService.hotList();
        return GraceJSONResult.ok(articles);
    }

    @Override
    public GraceJSONResult detail(String articleId) {
        ArticleDetailVO detailVO = articlePortalService.queryDetail(articleId);
        Set<String> idSet = new HashSet<>();
        idSet.add(articleId);
        List<AppUserVO> publisherList = getPublisherList(idSet);
        if (!publisherList.isEmpty()) {
            detailVO.setPublishUserName(publisherList.get(0).getNickname());
        }
        detailVO.setReadCounts(getCountsFromRedis(REDIS_ARTICLE_READ_COUNTS + ":" + articleId));
        return GraceJSONResult.ok(detailVO);
    }

    @Override
    public GraceJSONResult readArticle(String articleId, HttpServletRequest request) {
        String ip = IPUtil.getRequestIp(request);
        //设置针对当前用户ip存在的永久key。表示该用户已经阅读过了，无法累加阅读量
        redis.setnx(REDIS_ALREADY_READ + ":" + articleId + ":" + ip, ip);
        redis.increment(REDIS_ARTICLE_READ_COUNTS + ":" + articleId, 1);
        return GraceJSONResult.ok();
    }

    private PagedGridResult rebuildArticleGrid(PagedGridResult gridResult) {
        List<Article> list = (List<Article>) gridResult.getRows();
        //1.构建发布者id列表
        Set<String> idSet = new HashSet<>();
        List<String> idList = new ArrayList<>();

        for (Article a : list) {
            idSet.add(a.getPublishUserId());
            //构建文章id的list
            idList.add(REDIS_ARTICLE_READ_COUNTS + ":" + a.getId());
        }
        //发起redis的mget批量查询api
        List<String> readCountsRedisList = redis.mget(idList);
        List<AppUserVO> publisherList = getPublisherList(idSet);

        //拼接两个list，重组文章列表
        List<IndexArticleVO> indexArticleVOList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            Article article = list.get(i);
            BeanUtils.copyProperties(article, indexArticleVO);
            AppUserVO publisher = getUserIfPublisher(article.getPublishUserId(), publisherList);
            indexArticleVO.setPublisherVO(publisher);

            // 3.2 重新组装设置文章列表中的阅读量
            String redisCountsStr = readCountsRedisList.get(i);
            int readCounts = 0;
            if (StringUtils.isNotBlank(redisCountsStr)) {
                readCounts = Integer.valueOf(redisCountsStr);
            }
            indexArticleVO.setReadCounts(readCounts);

            indexArticleVOList.add(indexArticleVO);
        }

        gridResult.setRows(indexArticleVOList);
// END
        return gridResult;
    }

    private AppUserVO getUserIfPublisher(String publishUserId, List<AppUserVO> publisherList) {
        for (AppUserVO user : publisherList) {
            if (user.getId().equalsIgnoreCase(publishUserId)) {
                return user;
            }
        }
        return null;
    }

    private List<AppUserVO> getPublisherList(Set<String> idSet) {
        String userServerUrl = "http://user.imoocnews.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> responseEntity = restTemplate.getForEntity(userServerUrl, GraceJSONResult.class);
        GraceJSONResult jsonResult = responseEntity.getBody();
        List<AppUserVO> publisherList = null;
        if (jsonResult.getStatus() == 200) {
            String userJson = JsonUtils.objectToJson(jsonResult.getData());
            publisherList = JsonUtils.jsonToList(userJson, AppUserVO.class);
        }
        return publisherList;
    }
}

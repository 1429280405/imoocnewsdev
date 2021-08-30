package com.imooc.admin.controller;

import com.imooc.admin.service.CategoryMngService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.CategoryMngControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.SaveCategoryBO;
import com.imooc.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author liujinqiang
 * @create 2021-08-29 22:31
 */
@RestController
public class CategoryMngController extends BaseController implements CategoryMngControllerApi {

    @Autowired
    private CategoryMngService categoryMngService;

    @Override
    public GraceJSONResult saveOrUpdateCategory(@Valid SaveCategoryBO newCategoryBO, BindingResult result) {
        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return GraceJSONResult.errorMap(errorMap);
        }
        //id为空新增，不为空修改
        if (newCategoryBO.getId() == null) {
            //查询新增的分类名称不能重复存在
            boolean isExist = categoryMngService.queryCatIsExist(newCategoryBO.getName(), null);
            if (!isExist) {
                categoryMngService.modifyCategory(newCategoryBO);
            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }

        }
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getCatList() {
        return GraceJSONResult.ok(categoryMngService.getCatList());
    }

    @Override
    public GraceJSONResult getCats() {
        //先从redis中查询，如果有直接返回，如果没有，先从数据库中获取，放入缓存，然后返回
        String allCatJson = redis.get(REDIS_ALL_CATEGORY);
        List<Category> categories = null;
        if (StringUtils.isBlank(allCatJson)) {
            categories = categoryMngService.getCatList();
            redis.set(REDIS_ALL_CATEGORY, JsonUtils.objectToJson(categories));
        } else {
            categories = JsonUtils.jsonToList(allCatJson, Category.class);
        }
        return GraceJSONResult.ok(categories);
    }
}

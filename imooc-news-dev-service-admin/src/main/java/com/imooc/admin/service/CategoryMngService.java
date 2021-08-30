package com.imooc.admin.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.bo.SaveCategoryBO;

import java.util.List;

/**
 * @author liujinqiang
 * @create 2021-08-29 22:37
 */
public interface CategoryMngService {
    void saveOrUpdateCategory(SaveCategoryBO newCategoryBO);

    List<Category> getCatList();

    boolean queryCatIsExist(String name, String oldCatName);

    void modifyCategory(SaveCategoryBO newCategoryBO);
}

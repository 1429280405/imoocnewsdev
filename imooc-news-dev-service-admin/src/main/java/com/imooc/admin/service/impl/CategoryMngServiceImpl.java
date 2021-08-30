package com.imooc.admin.service.impl;

import com.imooc.admin.mapper.CategoryMapper;
import com.imooc.admin.service.CategoryMngService;
import com.imooc.api.service.BaseService;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.SaveCategoryBO;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
 * @author liujinqiang
 * @create 2021-08-29 22:41
 */
@Service
public class CategoryMngServiceImpl extends BaseService implements CategoryMngService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    @Transactional
    public void saveOrUpdateCategory(SaveCategoryBO newCategoryBO) {
        Category category = new Category();
        BeanUtils.copyProperties(newCategoryBO, category);
        int result = categoryMapper.insert(category);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        redis.del(REDIS_ALL_CATEGORY);
    }

    @Override
    public List<Category> getCatList() {
        return categoryMapper.selectAll();
    }

    @Override
    public boolean queryCatIsExist(String name, String oldCatName) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        if (StringUtils.isNoneBlank(oldCatName)) {
            criteria.andNotEqualTo("name", oldCatName);
        }

        List<Category> categories = categoryMapper.selectByExample(example);
        if (categories != null && !categories.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void modifyCategory(SaveCategoryBO newCategoryBO) {
        Category category = new Category();
        BeanUtils.copyProperties(newCategoryBO, category);
        int result = categoryMapper.updateByPrimaryKeySelective(category);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }

        /**
         * 不建议如下做法：
         * 1. 查询redis中的categoryList
         * 2. 循环categoryList中拿到原来的老的数据
         * 3. 替换老的category为新的
         * 4. 再次转换categoryList为json，并存入redis中
         */

        // 直接使用redis删除缓存即可，用户端在查询的时候会直接查库，再把最新的数据放入到缓存中
        redis.del(REDIS_ALL_CATEGORY);
    }
}

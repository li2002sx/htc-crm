package com.htche.crm.biz;


import com.htche.crm.domain.Category;
import com.htche.crm.mapper.CategoryMapper;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class CategoryBiz {

    @Autowired
    CategoryMapper categoryMapper;

    public Map<Integer, String> getCategoryMap(int type) {

        Map<Integer, String> map = new HashMap<>();
        Map<String, Object> argMap = new HashMap<>();
        argMap.put("type", type);
        List<Category> categories = categoryMapper.selectListByType(argMap);
        categories.forEach(c -> {
            map.put(c.getCategoryId(), c.getName());
        });
        return map;
    }
}

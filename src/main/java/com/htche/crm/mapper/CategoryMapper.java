package com.htche.crm.mapper;

import com.htche.crm.domain.Category;

import java.util.List;
import java.util.Map;

public interface CategoryMapper {

    List<Category> selectListByType(Map map);

}
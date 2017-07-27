package com.htche.crm.mapper;

import com.htche.crm.domain.CarModelTemplate;

import java.util.List;
import java.util.Map;

public interface CarModelTemplateMapper {

    int insertSelective(CarModelTemplate record);

    CarModelTemplate selectByPrimaryKey(Integer templateId);

    int updateByPrimaryKeySelective(CarModelTemplate record);

    List<CarModelTemplate> selectAllList(Map map);

    int updateStatus(Map map);

    int updateAcquiesce(Map map);
}
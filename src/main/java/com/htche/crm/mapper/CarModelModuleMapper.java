package com.htche.crm.mapper;

import com.htche.crm.domain.CarModelModule;

import java.util.List;
import java.util.Map;

public interface CarModelModuleMapper {

    int insertSelective(CarModelModule record);

    CarModelModule selectByPrimaryKey(Integer moduleId);

    int updateByPrimaryKeySelective(CarModelModule record);

    List<CarModelModule> selectAllList(Map map);

    int updateStatus(Map map);

    int updateOrderNo(Map map);
}
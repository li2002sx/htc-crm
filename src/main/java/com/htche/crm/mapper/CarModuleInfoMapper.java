package com.htche.crm.mapper;

import com.htche.crm.domain.CarModuleInfo;

import java.util.List;
import java.util.Map;

public interface CarModuleInfoMapper {

    int insertSelective(CarModuleInfo record);

    CarModuleInfo selectByPrimaryKey(Integer moduleInfoId);

    int updateByPrimaryKeySelective(CarModuleInfo record);

    List<CarModuleInfo> selectAllList(Map map);

    int updateStatus(Map map);

    int updateOrderNo(Map map);
}
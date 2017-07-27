package com.htche.crm.mapper;

import com.htche.crm.domain.CarInfo;

import java.util.List;
import java.util.Map;

public interface CarInfoMapper {

    int insertSelective(CarInfo record);

    CarInfo selectByPrimaryKey(Integer carInfoId);

    int updateByPrimaryKeySelective(CarInfo record);

    List<CarInfo> selectAllList(Map map);

    int updateStatus(Map map);
}
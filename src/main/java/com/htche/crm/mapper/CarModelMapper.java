package com.htche.crm.mapper;

import com.htche.crm.domain.CarInfo;
import com.htche.crm.domain.CarModel;

import java.util.List;
import java.util.Map;

public interface CarModelMapper {

    int insertSelective(CarModel record);

    CarModel selectByPrimaryKey(Integer carModelId);

    int updateByPrimaryKeySelective(CarModel record);

    List<CarModel> selectAllList(Map map);

    int updateStatus(Map map);
}
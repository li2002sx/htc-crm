package com.htche.crm.mapper;

import com.htche.crm.domain.CarTree;

import java.util.List;
import java.util.Map;

public interface CarTreeMapper {

    int insertSelective(CarTree record);

    CarTree selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(CarTree record);

    List<CarTree> selectAllList();

    List<CarTree> selectListByPids(Map map);

    Integer selectMaxId();

    int updateStatus(Map map);
}
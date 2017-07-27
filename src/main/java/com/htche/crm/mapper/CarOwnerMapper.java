package com.htche.crm.mapper;

import com.htche.crm.domain.CarOwner;

import java.util.List;
import java.util.Map;

public interface CarOwnerMapper {

    int insertSelective(CarOwner record);

    CarOwner selectByPrimaryKey(Integer carOwnerId);

    int updateByPrimaryKeySelective(CarOwner record);

    List<CarOwner> selectAllList(Map map);

    int updateStatus(Map map);

    int selectCount(Map map);

    int selectMonthBirthdayCount(Map map);

    int selectDayBirthdayCount(Map map);

    List<CarOwner> selectDayBirthdayList(Map map);

    int updateDayBirthday(Map map);
}
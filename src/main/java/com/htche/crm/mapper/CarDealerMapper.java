package com.htche.crm.mapper;

import com.htche.crm.domain.CarDealer;

import java.util.List;
import java.util.Map;

public interface CarDealerMapper {

    int insertSelective(CarDealer record);

    CarDealer selectByPrimaryKey(Integer carDealerId);

    int updateByPrimaryKeySelective(CarDealer record);

    List<CarDealer> selectAllList(Map map);

    int updateStatus(Map map);

    int selectCount(Map map);

    int selectSmsSum(Map map);

    int selectSendSmsSum(Map map);

    int updateSendSmsNum(Map map);

    int updateCompanyName(Map map);
}
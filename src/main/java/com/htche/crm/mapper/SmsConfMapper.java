package com.htche.crm.mapper;

import com.htche.crm.domain.SmsConf;

import java.util.List;
import java.util.Map;

public interface SmsConfMapper {

    int insertSelective(SmsConf record);

    SmsConf selectByCarDealerId(Integer carDealerId);

    int updateByPrimaryKeySelective(SmsConf record);

    List<SmsConf> selectAllList(Map map);
    
}
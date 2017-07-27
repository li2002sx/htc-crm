package com.htche.crm.mapper;

import com.htche.crm.domain.AllyDealer;

import java.util.List;
import java.util.Map;

public interface AllyDealerMapper {

    int insertSelective(AllyDealer record);

    AllyDealer selectByPrimaryKey(Integer allyDealerId);

    int updateByPrimaryKeySelective(AllyDealer record);

    List<AllyDealer> selectAllList(Map map);

    int updateStatus(Map map);

    int selectCount(Map map);
}
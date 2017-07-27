package com.htche.crm.mapper;

import com.htche.crm.domain.UserRechargeRecord;

import java.util.List;
import java.util.Map;

public interface UserRechargeRecordMapper {

    int insertSelective(UserRechargeRecord record);

    UserRechargeRecord selectByPrimaryKey(Integer userRechargeId);

    int updateByPrimaryKeySelective(UserRechargeRecord record);

    List<UserRechargeRecord> selectAllList(Map map);

    int updateStatus(Map map);

    UserRechargeRecord selectByOrderNumber(String orderNumber);

    int updateStatusByOrderNumber(Map map);
}
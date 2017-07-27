package com.htche.crm.mapper;

import com.htche.crm.domain.RechargeRecord;

import java.util.Map;

public interface RechargeRecordMapper {

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer rechargeId);

    RechargeRecord selectByOrderNumber(String orderNumber);

    int updatePayIdByRechargeId(Map map);

    RechargeRecord selectByPayId(String payId);

    int updatePayResult(Map map);
}
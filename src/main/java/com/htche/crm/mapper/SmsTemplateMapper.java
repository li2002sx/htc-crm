package com.htche.crm.mapper;

import com.htche.crm.domain.SmsTemplate;

import java.util.List;
import java.util.Map;

public interface SmsTemplateMapper {

    int insertSelective(SmsTemplate record);

    SmsTemplate selectByPrimaryKey(Integer smsTemplateId);

    int updateByPrimaryKeySelective(SmsTemplate record);

    List<SmsTemplate> selectAllList(Map map);

    int updateStatus(Map map);
}
package com.htche.crm.mapper;

import com.htche.crm.domain.Sms;

import java.util.List;
import java.util.Map;

public interface SmsMapper {

    int insertSelective(Sms record);

    Sms selectByPrimaryKey(Integer smsId);

    List<Sms> selectAllList(Map map);

    List<Sms> selectCurDateSmsByMobile(Map map);

    Sms selectByMobileAndTempId(Map map);
}
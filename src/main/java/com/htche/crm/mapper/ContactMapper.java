package com.htche.crm.mapper;

import com.htche.crm.domain.Contact;

import java.util.List;
import java.util.Map;

public interface ContactMapper {

    int insertSelective(Contact record);

    Contact selectByPrimaryKey(Integer contactId);

    int updateByPrimaryKeySelective(Contact record);

    List<Contact> selectAllList(Map map);

    int updateStatus(Map map);
}
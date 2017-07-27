package com.htche.crm.mapper;

import com.htche.crm.domain.Notice;

import java.util.List;
import java.util.Map;

public interface NoticeMapper {

    int insertSelective(Notice record);

    Notice selectByPrimaryKey(Integer noticeId);

    int updateByPrimaryKeySelective(Notice record);

    List<Notice> selectAllList(Map map);

    int updateStatus(Map map);
}
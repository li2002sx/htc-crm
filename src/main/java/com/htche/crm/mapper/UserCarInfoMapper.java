package com.htche.crm.mapper;

import com.htche.crm.domain.UserCarInfo;
import com.htche.crm.domain.UserPhoto;

import java.util.List;
import java.util.Map;

public interface UserCarInfoMapper {

    int insertSelective(UserCarInfo record);

    UserCarInfo selectByPrimaryKey(Integer userCarInfoId);

    int updateByPrimaryKeySelective(UserCarInfo record);

    List<UserCarInfo> selectAllList(Map map);

    int updateStatus(Map map);

    UserCarInfo selectByCarModelId(Integer carModelId);
}
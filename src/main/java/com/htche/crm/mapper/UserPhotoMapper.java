package com.htche.crm.mapper;

import com.htche.crm.domain.UserPhoto;

import java.util.List;
import java.util.Map;

public interface UserPhotoMapper {

    int insertSelective(UserPhoto record);

    UserPhoto selectByPrimaryKey(Integer photoId);

    int updateByPrimaryKeySelective(UserPhoto record);

    List<UserPhoto> selectAllList(Map map);

    int updateStatus(Map map);

    int updateStatusByUserId(Map map);
}
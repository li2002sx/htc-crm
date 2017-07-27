package com.htche.crm.mapper;

import com.htche.crm.domain.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    List<User> selectAllList(Map map);

    int updateStatus(Map map);

    User selectByMobile(String mobile);

    User selectByToken(String token);

    int updateLastLoginTime(Map map);

    int updateExpireTime(Map map);

    int updatePasswordAndToken(Map map);

    int updateInfoByUserId(User user);
}
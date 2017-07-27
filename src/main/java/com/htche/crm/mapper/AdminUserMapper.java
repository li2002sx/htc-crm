package com.htche.crm.mapper;

import com.htche.crm.domain.AdminUser;
import com.htche.crm.domain.CarDealer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 16/6/25.
 */
public interface AdminUserMapper {

    int insertSelective(AdminUser record);

    AdminUser selectByPrimaryKey(Integer adminUserId);

    int updateByPrimaryKeySelective(AdminUser record);

    List<AdminUser> selectAllList(Map map);

    int updateStatus(Map map);

    AdminUser selectByName(Map map);

    int updatePassword(Map map);

    int updatePicUrl(Map map);

    int updateLastLoginTime(Map map);
}

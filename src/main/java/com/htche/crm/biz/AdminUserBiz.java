package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.AdminUser;
import com.htche.crm.mapper.AdminUserMapper;
import com.htche.crm.domain.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 16/6/25.
 */
@Component
public class AdminUserBiz {

    @Autowired
    AdminUserMapper adminUserMapper;

    /**
     * 保存
     *
     * @param adminUser
     * @return
     */
    public boolean save(AdminUser adminUser) {
        int effectCount = 0;
        if (adminUser.getAdminUserId() > 0) {
            effectCount = adminUserMapper.updateByPrimaryKeySelective(adminUser);
        } else {
            effectCount = adminUserMapper.insertSelective(adminUser);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @param userName
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public Page<AdminUser> selectAllList(String userName, int pageIndex, int pageSize) {
        Page<AdminUser> adminUserPage = PageHelper.startPage(pageIndex, pageSize);
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        List<AdminUser> adminUsers = adminUserMapper.selectAllList(map);
        return adminUserPage;
    }

    public AdminUser selectByPrimaryKey(int adminUserId) {
        return adminUserMapper.selectByPrimaryKey(adminUserId);
    }

    public boolean updateStatus(int adminUserId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("adminUserId", adminUserId);
        map.put("status", status);
        return adminUserMapper.updateStatus(map) > 0;
    }

    public AdminUser selectByName(String userName) {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", userName);
        return adminUserMapper.selectByName(map);
    }

    public boolean updatePassword(int adminUserId, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("adminUserId", adminUserId);
        map.put("password", password);
        return adminUserMapper.updatePassword(map) > 0;
    }

    public boolean updatePicUrl(int adminUserId, String picUrl) {
        Map<String, Object> map = new HashMap<>();
        map.put("adminUserId", adminUserId);
        map.put("userPic", picUrl);
        return adminUserMapper.updatePicUrl(map) > 0;
    }

    public boolean updateLastLoginTime(int adminUserId) {
        Map<String, Object> map = new HashMap<>();
        map.put("adminUserId", adminUserId);
        map.put("lastLoginTime", new Date());
        return adminUserMapper.updateLastLoginTime(map) > 0;
    }
}

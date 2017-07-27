package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.UserCarInfo;
import com.htche.crm.mapper.UserCarInfoMapper;
import com.htche.crm.model.query.UserCarInfoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class UserCarInfoBiz {

    @Autowired
    UserCarInfoMapper userCarInfoMapper;

    /**
     * 保存
     *
     * @param userCarInfo
     * @return
     */
    public boolean save(UserCarInfo userCarInfo) {
        int effectCount = 0;
        if (userCarInfo.getUserCarInfoId() > 0) {
            effectCount = userCarInfoMapper.updateByPrimaryKeySelective(userCarInfo);
        } else {
            effectCount = userCarInfoMapper.insertSelective(userCarInfo);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     * @return
     */
    public Page<UserCarInfo> selectAllList(UserCarInfoQuery query) {
        Page<UserCarInfo> userCarInfoPage = PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        Map<String, Object> map = new HashMap<>();
        List<UserCarInfo> userCarInfos = userCarInfoMapper.selectAllList(map);
        return userCarInfoPage;
    }

    public UserCarInfo selectByPrimaryKey(int userCarInfoId) {
        return userCarInfoMapper.selectByPrimaryKey(userCarInfoId);
    }

    public boolean updateStatus(int userCarInfoId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("userCarInfoId", userCarInfoId);
        map.put("status", status);
        return userCarInfoMapper.updateStatus(map) > 0;
    }

    public UserCarInfo selectByCarModelId(int userCarInfoId) {
        return userCarInfoMapper.selectByCarModelId(userCarInfoId);
    }
}

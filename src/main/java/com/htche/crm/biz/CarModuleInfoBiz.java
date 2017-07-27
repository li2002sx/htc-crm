package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.CarModuleInfo;
import com.htche.crm.mapper.CarModuleInfoMapper;
import com.htche.crm.model.query.CarModuleInfoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class CarModuleInfoBiz {

    @Autowired
    CarModuleInfoMapper carModuleInfoMapper;

    /**
     * 保存
     *
     * @param carModuleInfo
     * @return
     */
    public boolean save(CarModuleInfo carModuleInfo) {
        int effectCount = 0;
        if (carModuleInfo.getModuleInfoId() > 0) {
            effectCount = carModuleInfoMapper.updateByPrimaryKeySelective(carModuleInfo);
        } else {
            effectCount = carModuleInfoMapper.insertSelective(carModuleInfo);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @return
     */
    public Page<CarModuleInfo> selectAllList(CarModuleInfoQuery query) {
        Page<CarModuleInfo> carModuleInfoPage = PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        Map<String, Object> map = new HashMap<>();
        map.put("moduleIds", Arrays.asList(query.getModuleId()));
        List<CarModuleInfo> carModuleInfos = carModuleInfoMapper.selectAllList(map);
        return carModuleInfoPage;
    }

    public CarModuleInfo selectByPrimaryKey(int carModuleInfoId) {
        return carModuleInfoMapper.selectByPrimaryKey(carModuleInfoId);
    }

    public boolean updateStatus(int moduleInfoId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("moduleInfoId", moduleInfoId);
        map.put("status", status);
        return carModuleInfoMapper.updateStatus(map) > 0;
    }

    public boolean updateOrderNo(int moduleInfoId, int orderNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("moduleInfoId", moduleInfoId);
        map.put("orderNo", orderNo);
        return carModuleInfoMapper.updateOrderNo(map) > 0;
    }

    /**
     * 根据模块ID获取列表
     *
     * @return
     */
    public List<CarModuleInfo> selectListByModuleIds(List<String> moduleIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("moduleIds", moduleIds);
        List<CarModuleInfo> carModuleInfos = carModuleInfoMapper.selectAllList(map);
        return carModuleInfos;
    }
}

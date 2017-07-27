package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.CarModelModule;
import com.htche.crm.mapper.CarModelModuleMapper;
import com.htche.crm.model.query.CarModelModuleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class CarModelModuleBiz {

    @Autowired
    CarModelModuleMapper carModelModuleMapper;

    /**
     * 保存
     *
     * @param carModelModule
     * @return
     */
    public boolean save(CarModelModule carModelModule) {
        int effectCount = 0;
        if (carModelModule.getModuleId() > 0) {
            effectCount = carModelModuleMapper.updateByPrimaryKeySelective(carModelModule);
        } else {
            effectCount = carModelModuleMapper.insertSelective(carModelModule);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @return
     */
    public Page<CarModelModule> selectAllList(CarModelModuleQuery query) {
        Page<CarModelModule> carModelModulePage = PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        Map<String, Object> map = new HashMap<>();
        map.put("carModelId", query.getCarModelId());
        List<CarModelModule> carModelModules = carModelModuleMapper.selectAllList(map);
        return carModelModulePage;
    }

    public CarModelModule selectByPrimaryKey(int carModelModuleId) {
        return carModelModuleMapper.selectByPrimaryKey(carModelModuleId);
    }

    public boolean updateStatus(int moduleId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("moduleId", moduleId);
        map.put("status", status);
        return carModelModuleMapper.updateStatus(map) > 0;
    }

    public boolean updateOrderNo(int moduleId, int orderNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("moduleId", moduleId);
        map.put("orderNo", orderNo);
        return carModelModuleMapper.updateOrderNo(map) > 0;
    }
}

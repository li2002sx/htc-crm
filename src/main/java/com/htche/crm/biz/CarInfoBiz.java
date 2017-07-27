package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.CarInfo;
import com.htche.crm.mapper.CarInfoMapper;
import com.htche.crm.model.query.CarInfoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class CarInfoBiz {

    @Autowired
    CarInfoMapper carInfoMapper;

    /**
     * 保存
     *
     * @param carInfo
     * @return
     */
    public boolean save(CarInfo carInfo) {
        int effectCount = 0;
        if (carInfo.getCarInfoId() > 0) {
            effectCount = carInfoMapper.updateByPrimaryKeySelective(carInfo);
        } else {
            effectCount = carInfoMapper.insertSelective(carInfo);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     * @return
     */
    public Page<CarInfo> selectAllList(CarInfoQuery query) {
        Page<CarInfo> carInfoPage = PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        Map<String, Object> map = new HashMap<>();
        map.put("specId",query.getSpecId());
        map.put("brandId",query.getBrandId());
        map.put("provinceId",query.getProvinceId());
        map.put("cityId",query.getCityId());
        map.put("year",query.getYear());
        map.put("month",query.getMonth());
        List<CarInfo> carInfos = carInfoMapper.selectAllList(map);
        return carInfoPage;
    }

    public CarInfo selectByPrimaryKey(int carInfoId) {
        return carInfoMapper.selectByPrimaryKey(carInfoId);
    }

    public boolean updateStatus(int carInfoId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("carInfoId", carInfoId);
        map.put("status", status);
        return carInfoMapper.updateStatus(map) > 0;
    }
}

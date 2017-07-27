package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.CarDealer;
import com.htche.crm.mapper.CarDealerMapper;
import com.htche.crm.model.query.CarDealerQuery;
import com.htche.crm.model.query.SmsHuyi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class CarDealerBiz {

    @Autowired
    CarDealerMapper carDealerMapper;

    /**
     * 保存
     *
     * @param carDealer
     * @return
     */
    public boolean save(CarDealer carDealer) {
        int effectCount = 0;
        if (carDealer.getCarDealerId() > 0) {
            effectCount = carDealerMapper.updateByPrimaryKeySelective(carDealer);
        } else {
            effectCount = carDealerMapper.insertSelective(carDealer);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @return
     */
    public Page<CarDealer> selectAllList(CarDealerQuery query) {
        Page<CarDealer> carDealerPage = PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        Map<String, Object> map = new HashMap<>();
        map.put("companyName", query.getCompanyName());
        map.put("name", query.getName());
        map.put("provinceId", query.getProvinceId());
        map.put("cityId", query.getCityId());
        List<CarDealer> carDealers = carDealerMapper.selectAllList(map);
        return carDealerPage;
    }

    public CarDealer selectByPrimaryKey(int carDealerId) {
        return carDealerMapper.selectByPrimaryKey(carDealerId);
    }

    public boolean updateStatus(int carDealerId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("carDealerId", carDealerId);
        map.put("status", status);
        return carDealerMapper.updateStatus(map) > 0;
    }

    public Map<Integer, String> getAllMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "超级管理员");
        List<CarDealer> carDealers = carDealerMapper.selectAllList(map);
        carDealers.forEach(item -> {
            map.put(item.getCarDealerId(), item.getCompanyName());
        });
        return map;
    }

    public int selectCount() {
        Map<String, Object> map = new HashMap<>();
        return carDealerMapper.selectCount(map);
    }

    public int selectSmsSum() {
        Map<String, Object> map = new HashMap<>();
        return carDealerMapper.selectSmsSum(map);
    }

    public int selectSendSmsSum() {
        Map<String, Object> map = new HashMap<>();
        return carDealerMapper.selectSmsSum(map);
    }

    public Map<Integer, SmsHuyi> getSmsHuyiMap() {
        Map<Integer, SmsHuyi> map = new HashMap<>();
        List<CarDealer> carDealers = carDealerMapper.selectAllList(map);
        SmsHuyi smsHuyi = new SmsHuyi();
        carDealers.forEach(item -> {
            smsHuyi.setAppId(item.getAppId());
            smsHuyi.setAppKey(item.getAppKey());
            map.put(item.getCarDealerId(), smsHuyi);
        });
        return map;
    }

    public boolean updateSendSmsNum(int carDealerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("carDealerId", carDealerId);
        return carDealerMapper.updateSendSmsNum(map) > 0;
    }

    public boolean updateCompanyName(int carDealerId, String companyName) {
        Map<String, Object> map = new HashMap<>();
        map.put("carDealerId", carDealerId);
        map.put("companyName", companyName);
        return carDealerMapper.updateCompanyName(map) > 0;
    }
}
